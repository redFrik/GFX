//related: GFX_ModuleGUI, GFX_Rack, RedEffectModule

GFX_Module : AbstractGFX {

	var <prefix;
	var controllers;

	*new {|target, bus= 0, lags= 0.1, numChannels= 2, addAction= \addToTail, args|
		var trg= target.asTarget;
		if(trg.server.serverRunning.not, {
			"server % not running".format(trg.server).warn;
			^nil
		});
		^super.new(trg, numChannels).initGFX_Module(bus, lags, addAction, args)
	}

	*newModule {|efx, target, bus= 0, lags= 0.1, numChannels= 2, addAction= \addToTail, args|
		^efx.new(target, bus, lags, numChannels, addAction, args)
	}

	initGFX_Module {|bus, lags, addAction, args|
		var arArgs, synthArgs;

		prefix= this.class.asString.replace("GFX").toLower;

		//--all modules automatically get a mix and a pause
		specs= specs++((prefix++"Mix").asSymbol -> ControlSpec(0, 1, 'lin', 0, 0));
		cvs.put(specs[0].key, Ref(0));
		lookup.put(specs[0].key, \mix);
		cvs.put(\pause, Ref(false));

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
					if(k!=\pause, {
						v= this.specForKey(k).constrain(v);
						synthArgs.put(lookup[k], v);
						this.specForKey(k).default= v;  //update spec default from args
					});
					cvs[k].value= v;  //override Ref value
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
				{|val|
					if(synth.notNil, {
						if(val, {
							cvs[this.mixKeys[0]].value= 0;
						});
						target.server.makeBundle(target.server.latency, {
							synth.run(val.not);
						});
					});
				}
			}
			{
				{|val| synth.set(name, val)}
			};

			controllers.add(
				CV(v, spec).addAction({|cv, val|
					if(val!=lastVal, {
						updateFunc.value(val);
						lastVal= val;
					});
				})
			);

			this.prAddMethod(k, v, spec);
		};

		//--generate synthdef
		def= this.prBuildDef(lags.dup(specs.size));

		//--start synth
		synth= Synth.basicNew(def.name, target.server);
		def.doSend(target.server, synth.newMsg(target, synthArgs.asKeyValuePairs, addAction));

		if(this.pause, {  //can be true when pause set in args
			target.server.makeBundle(target.server.latency, {
				synth.run(false);
			});
		});

		synth.onFree({synth= nil; this.free});
	}

	*ar {^this.subclassResponsibility(thisMethod)}

	*specs {^this.subclassResponsibility(thisMethod)}

	//--synth

	free {
		super.free;
		controllers.do{|x| x.remove};
	}

	lags_ {|val|
		synth.set(\lags, val);
	}

	//--convenience

	code {  //does not include target, bus, lags, addAction
		var str= "";
		this.values.pairsDo{|k, v|
			if(str.size>0, {str= str++", "});
			str= str++"%: %".format(k, v)
		};
		if(str.size>0, {str= "args: [%]".format(str)});
		if(numChannels!=2, {
			if(str.size>0, {str= ", "++str});
			str= "numChannels: %%".format(numChannels, str);
		});
		str= "%(%)".format(this.class.name, str);
		if(this.pause, {
			str= str++".pause_(true)";
		});
		^str
	}

	gui {|position, version= 0|
		^GFX_ModuleGUI(this, version:version).moveTo(*position.asRect.asArray.drop(2))
	}

	//--private

	prBuildDef {|argLags|
		^SynthDef((this.class.asString++$_++numChannels).asSymbol, {|bus|
			var lags= NamedControl.kr(\lags, argLags);
			var mix, args= List.new;
			specs.do{|assoc, i|
				var name= lookup[assoc.key];
				var c= NamedControl.kr(name, nil, lags.asArray[i], false, assoc.value);
				if(i==0, {mix= c}, {args.add(c.max(-3.4e38))});
			};
			XOut.ar(bus, mix, this.class.ar(In.ar(bus, numChannels), *args));
		})
	}
}
