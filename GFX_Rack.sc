//related: GFX_RackGUI, GFX_Module, RedEffectsRack

GFX_Rack : AbstractGFX {

	var <group;
	var <efxs;
	var <outbus;  //will initially be set to same as bus argument

	*new {|efxs, target, bus= 0, lags= 0.1, numChannels= 2, action|
		^super.new.initGFX_Rack(efxs, target, bus, lags, numChannels, action, false)
	}

	*newPaused {|efxs, target, bus= 0, lags= 0.1, numChannels= 2, action|
		^super.new.initGFX_Rack(efxs, target, bus, lags, numChannels, action, true)
	}

	initGFX_Rack {|argEfxs, target, bus, lags, argNum, action, paused|
		numChannels= argNum;

		efxs= argEfxs.asArray;
		if(efxs.isEmpty, {
			efxs= [GFXComb, GFXFreq, GFXRvrs, GFXTank, GFXTanh, GFXZzzz];
			"%: no efxs given - using default classes: %".format(
				this.class.name,
				efxs.collect{|x| x.name}
			).postln;
		});

		fork{
			var server;

			outbus= bus;
			target= target.asTarget;
			server= target.server;
			server.bootSync;
			group= Group.tail(target);

			efxs= efxs.collect{|x|
				if(x.isKindOf(GFX_Module), {
					if(x.numChannels!=numChannels, {
						"%: module % wrong numChannels".format(this.class.name, x.class.name).warn;
					});
					x.bus= bus;
					x.lags= lags;
					x.synth.moveToTail(group);
				}, {
					x= x.new(group, bus, lags, numChannels, \addToTail);
				});
				server.sync;
				if(paused, {x.pause_(true)});
				x
			};

			//--hijack all modules cvs, specs and lookup
			efxs.do{|x|
				x.specs.do{|assoc|
					var keyStr, suffix;
					var key= assoc.key;
					var spec= assoc.value;
					var ref= x.cvs[key];

					//--add suffix for duplicate efx modules
					if(cvs[key].notNil, {
						keyStr= key.asString;
						suffix= 0;
						cvs.keysDo{|kk|
							if(kk.asString.contains(keyStr), {
								suffix= suffix+1;
							});
						};
						key= (keyStr++$_++suffix).asSymbol;
					});

					specs= specs++(key -> spec);
					cvs.put(key, ref);
					lookup.put(key, x.lookup[assoc.key]);
					this.prAddMethod(key, ref, spec);
				};
			};

			//--generate synthdef
			def= this.prBuildDef;

			//--start synth
			synth= Synth.basicNew(def.name, server);
			def.doSend(server, synth.newMsg(group, [\bus, bus], \addToTail));
			server.sync;

			action.value(this);
		};
	}

	gui {|position, version= 0|
		^GFX_RackGUI(this, position, version)
	}

	//--synth

	bus_ {|val|
		super.bus_(val);
		efxs.do{|efx| efx.bus_(val)};
	}

	free {
		super.free;
		group.free;
	}

	lags_ {|val|
		efxs.do{|efx| efx.lags_(val)};
	}

	outbus_ {|val|
		synth.set(\outbus, val);
	}

	pause_ {|bool|
		efxs.do{|efx| efx.pause_(bool)};
	}

	vol_ {|val|
		synth.set(\vol, val);
	}

	//--introspection and helper methods

	diff {  //just post what differ from defaults
		"%: parameters that differ:".format(this.class.name).postln;
		cvs.keysValuesDo{|k, v|
			var spec= this.specForKey(k);
			if(spec.default!=v.value, {
				"\t%: % (default %)".format(k, v.value, spec.default).postln;
			});
		}
	}

	which {  //just post active modules (mix > 0)
		"%: active modules:".format(this.class.name).postln;
		this.active.do{|x| "\t%: %".format(x.key, x.value).postln};
	}

	//--private

	prBuildDef {
		^SynthDef((this.class.asString++"_Out_"++numChannels).asSymbol, {|bus|
			var out= \outbus.kr(outbus, spec: \audiobus.asSpec);
			var in= In.ar(bus, numChannels);
			in= Sanitize.ar(in);
			in= LeakDC.ar(in);
			in= in*\vol.kr(0, 0.05, spec: ControlSpec(-inf, 12, 'db', units: " dB")).dbamp;
			ReplaceOut.ar(bus, in);
			Out.ar(out, in*BinaryOpUGen('!=', out, bus));  //send if different outbus and bus
		})
	}
}
