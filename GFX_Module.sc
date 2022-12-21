//related: GFX_ModuleGUI, GFX_Rack, RedEffectModule

GFX_Module : AbstractGFX {

	var <prefix;
	var controllers;

	*new {|target, bus= 0, lags= 0.1, numChannels= 2, addAction= \addToTail, args|
		^super.new.initGFX_Module(target, bus, lags, numChannels, addAction, args, false)
	}

	*newPaused {|target, bus= 0, lags= 0.1, numChannels= 2, addAction= \addToTail, args|
		^super.new.initGFX_Module(target, bus, lags, numChannels, addAction, args, true)
	}

	*newModule {|efx, target, bus= 0, lags= 0.1, numChannels= 2, addAction= \addToTail, args|
		^efx.new(target, bus, lags, numChannels, addAction, args, false)
	}

	initGFX_Module {|target, bus, lags, argNum, addAction, args, argPaused|
		var server, arArgs, synthArgs;

		numChannels= argNum;
		prefix= this.class.asString.replace("GFX").toLower;

		target= target.asTarget;
		server= target.server;
		if(server.serverRunning.not, {
			"%: server not running".format(this.class.name).warn;
		});

		//--all modules automatically get a mix and a pause
		specs= specs++((prefix++"Mix").asSymbol -> ControlSpec(0, 1, 'lin', 0, 0));
		cvs.put(specs[0].key, Ref(0));
		lookup.put(specs[0].key, \mix);
		cvs.put(\pause, Ref(argPaused));

		//--add *ar arguments and defaults in order to specs and cvs
		arArgs= this.class.class.findMethod(\ar).keyValuePairsFromArgs;
		if(arArgs[0]==\in, {
			arArgs= arArgs.drop(2);
		}, {
			"%: first *ar argument is not 'in'".format(this.class.name).warn;
		});
		arArgs.pairsDo{|k, v|
			var found, kGuess;
			if(k==\mix or:{k==\pause or:{k==\bus}}, {
				"%: ignoring reserved *ar argument '%'".format(this.class.name, k).warn;
			}, {
				found= false;
				kGuess= k.asString.toLower;
				this.class.specs.keysValuesDo{|key, spec|
					if(kGuess==key.asString.toLower.replace(prefix), {
						if(found, {
							"%: ignoring spec duplicate for '%'".format(this.class.name, k).warn;
						}, {
							found= true;
							if(spec.default!=spec.minval and:{spec.default!=v}, {
								"%: spec default value mismatch for '%'".format(this.class.name, k).warn;
							});
							specs= specs++(key -> spec.default_(v));
							cvs.put(key, Ref(v));
							lookup.put(key, k);
						});
					});
				};
				if(found.not, {
					"%: spec key mismatch or missing for '%'".format(this.class.name, k).warn;
				});
			});
		};

		//--build synth arguments from specs and additional args
		synthArgs= ().put(\bus, bus);
		args.asArray.keysValuesDo{|k, v|  //add additional args passed in
			if(k==\bus, {
				if(synthArgs[\bus]!=v, {
					"%: overriding 'bus' argument with args".format(this.class.name).warn;
				});
				synthArgs.put(k, v);
			}, {
				if(cvs[k].notNil, {
					v= this.specForKey(k).constrain(v);
					synthArgs.put(lookup[k], v);
					cvs[k].value= v;  //override Ref value
					this.specForKey(k).default= v;  //update spec default from args
				}, {
					"%: argument '%' not found".format(this.class.name, k).warn;
				});
			});
		};

		//--set up controllers and methods from cvs
		controllers= List.new;
		cvs.keysValuesDo{|k, v|
			var lastVal;
			var name= lookup[k];  //e.g. bitcRate -> rate
			var spec= this.specForKey(k);
			var updateFunc= case
			{name==\mix} {
				{|val|
					if(this.pause and:{val>0}, {
						this.pause_(false);
					});
					synth.set(name, val);
				}
			}
			{k==\pause} {
				{|val| synth.run(val.not)}
			}
			{
				{|val| synth.set(name, val)}
			};

			controllers.add(
				SimpleController(v).put(\value, {|ref|
					if(ref.value!=lastVal, {
						updateFunc.value(ref.value);
						lastVal= ref.value;
					});
				})
			);

			this.prAddMethod(k, v, spec);
		};

		//--generate synthdef
		def= this.prBuildDef(lags.dup(specs.size));

		//--start synth
		synth= Synth.basicNew(def.name, server);
		def.doSend(server, synth.newMsg(target, synthArgs.asKeyValuePairs, addAction));
		if(argPaused, {
			server.makeBundle(server.latency, {synth.run(false)});  //TODO this will fail with latency=0
		});
	}

	gui {|position, version= 0|
		^GFX_ModuleGUI(this, version:version).moveTo(*position.asRect.asArray.drop(2))
	}

	//--synth

	free {
		super.free;
		controllers.do{|x| x.remove};
	}

	lags_ {|val|
		synth.set(\lags, val);
	}

	//--required methods

	*ar {^this.subclassResponsibility(thisMethod)}

	*specs {^this.subclassResponsibility(thisMethod)}

	//--private

	prBuildDef {|argLags|
		^SynthDef((this.class.asString++$_++numChannels).asSymbol, {|bus|
			var lags= NamedControl.kr(\lags, argLags);
			var mix, args= List.new;
			specs.do{|assoc, i|
				var name= lookup[assoc.key];
				var c= NamedControl.kr(name, nil, lags.asArray[i], false, assoc.value);
				if(i==0, {mix= c}, {args.add(c)});
			};
			XOut.ar(bus, mix, this.class.ar(In.ar(bus, numChannels), *args));
		})
	}
}
