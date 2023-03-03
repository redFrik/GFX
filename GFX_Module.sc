//related: GFX_ModuleGUI, GFX_Rack, RedEffectModule

GFX_Module : AbstractGFX {

	var <order;  //Array with symbols
	var <prefix;  //String

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
		var controllers;
		var mixKey;

		prefix= this.class.asString.replace("GFX").toLower;

		//--all modules automatically get a mix CV and a pause CV
		mixKey= (prefix++"Mix").asSymbol;
		cvs.put(mixKey, CV(Ref(0)));
		lookup.put(mixKey, \mix);
		cvs.put(\pause, CV(Ref(false)));

		//--use *ar arguments and defaults to create CVs
		arArgs= this.class.class.findMethod(\ar).keyValuePairsFromArgs;
		if(arArgs[0]==\in, {
			arArgs= arArgs.drop(2);
		}, {
			"%: first *ar argument is not 'in'".format(this.class.name).warn;
		});

		order= [mixKey];  //parameter order from *ar args - mainly for GUI

		arArgs.pairsDo{|k, v|
			var found, kGuess;
			if(#[\mix, \pause, \bus, \lags].includes(k), {
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

							cvs.put(key, CV(Ref(v), spec.default_(v)));
							lookup.put(key, k);
							order= order.add(key);
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
				if(bus!=v, {
					"%: overriding bus argument with args".format(this.class.name).warn;
				});
				synthArgs.put(k, v);
			}, {
				if(cvs[k].notNil, {
					if(k==\pause, {
						cvs[k].spec.default= v.asInteger;  //update spec default
					}, {
						v= cvs[k].spec.constrain(v);
						cvs[k].spec.default= v;  //update spec default
						synthArgs.put(lookup[k], v);
					});
					cvs[k].value= v;  //override CV value
				}, {
					"%: argument '%' not found".format(this.class.name, k).warn;
				});
			});
		};

		//--set up controllers and methods from CVs
		controllers= List.new;
		cvs.keysValuesDo{|k, cv|
			var lastVal;
			var name= lookup[k];  //e.g. bitcRate -> rate

			var updateFunc= case
			{name==\mix} {
				{|val|
					if(this.pause and:{val>0}, {
						this.pause_(false);  //unpause if mix > 0
					});
					synth.set(name, val);
				}
			}
			{k==\pause} {
				{|val|
					target.server.makeBundle(target.server.latency, {
						synth.run(val.not);
					});
				}
			}
			{
				{|val|
					synth.set(name, val);
				}
			};

			controllers.add(
				cv.addAction({|cv, val|
					if(val!=lastVal, {
						updateFunc.value(val);
						lastVal= val;
					});
				})
			);

			this.prAddMethod(k, cv);
		};

		//--generate synthdef
		def= this.prBuildDef(lags.asArray);

		//--start synth
		synth= Synth.basicNew(def.name, target.server);
		def.doSend(target.server, synth.newMsg(target, synthArgs.asKeyValuePairs, addAction));

		if(this.pause, {  //true when pause set in args
			target.server.makeBundle(target.server.latency, {
				synth.run(false);
			});
		});

		synth.onFree({
			synth= nil;
			controllers.do{|x| x.remove};
		});
	}

	*ar {^this.subclassResponsibility(thisMethod)}

	*specs {^this.subclassResponsibility(thisMethod)}

	//--synth

	lags_ {|val|
		synth.set(\lags, val);
	}

	//--convenience

	code {|verbose= false|
		//does not include target, bus, lags, addAction
		var str= "";
		if(verbose, {this.values}, {this.diff}).pairsDo{|k, v|
			if(str.size>0, {str= str++", "});
			str= str++"%: %".format(k, v)
		};
		if(str.size>0, {str= "args: [%]".format(str)});
		//TODO bus somehow?
		if(verbose or:{numChannels!=2}, {
			if(str.size>0, {str= ", "++str});
			str= "numChannels: %%".format(numChannels, str);
		});
		str= "%(%)".format(this.class.name, str);
		^str
	}

	gui {|position, version= 0|
		^GFX_ModuleGUI(this, version:version).moveTo(*position.asRect.asArray.drop(2))
	}

	openCodeFile {this.class.openCodeFile}

	*allSubclasses {
		^subclasses.asList.sort{|a, b| a.name<=b.name}.asArray
	}

	//--private

	prBuildDef {|lags|
		^SynthDef((this.class.asString++$_++numChannels).asSymbol, {|bus|
			var l= NamedControl.kr(\lags, {|i| lags.wrapAt(i)}.dup(order.size)).asArray;
			var args= Array.newClear(order.size);
			cvs.keysValuesDo{|k, cv, i|
				var index, name, c;
				if(k!=\pause, {
					index= order.indexOf(k);
					name= lookup[k];
					c= NamedControl.kr(name, nil, l[index], false, cv.spec);
					args.put(index, c.max(-3.4e38));
				});
			};
			XOut.ar(bus, args[0], this.class.ar(In.ar(bus, numChannels), *args[1..]));
		})
	}
}
