//related: GFX_RackGUI, GFX_Module, RedEffectsRack

GFX_Rack : AbstractGFX {

	var <efxs;  //Array of GFX_Module instances
	var <group;  //internal Group
	var <outbus;  //will initially be set to same as bus argument

	*new {|efxs, target, bus= 0, lags= 0.1, numChannels= 2, action|
		var trg= target.asTarget;
		if(trg.server.serverRunning.not, {
			"server % not running".format(trg.server).warn;
			^nil
		});
		^super.new(trg, numChannels).initGFX_Rack(efxs, bus, lags, action)
	}

	initGFX_Rack {|argEfxs, bus, lags, action|
		var reorder= false;

		efxs= argEfxs.asArray;
		if(efxs.isEmpty, {
			efxs= [GFXComb, GFXFreq, GFXRvrs, GFXTank, GFXTanh, GFXZzzz];
			"%: no efxs given - using default classes: %".format(
				this.class.name,
				efxs.collect{|x| x.name}
			).postln;
		});

		outbus= bus;

		group= Group.tail(target);

		efxs= efxs.collect{|x|
			if(x.isKindOf(GFX_Module), {
				reorder= true;
				if(x.numChannels!=numChannels, {
					"%: module % wrong numChannels".format(this.class.name, x.class.name).warn;
				});
				target.server.makeBundle(target.server.latency?0.05*2, {
					x.bus= bus;
					x.lags= lags;
				});
			}, {
				x= x.new(group, bus, lags, numChannels, \addToTail);
			});
			x
		};
		if(reorder, {
			target.server.makeBundle(target.server.latency?0.05*2, {
				efxs.do{|x, i|
					if(i==0, {
						x.synth.moveToHead(group);
					}, {
						x.synth.moveAfter(efxs[i-1].synth);
					});
				};
			});
		});

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
		synth= Synth.basicNew(def.name, target.server);
		def.doSend(target.server, synth.newMsg(group, [\bus, bus], \addToTail));
		synth.onFree({synth= nil; this.free});

		{action.value(this)}.defer;
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

	//--convenience

	code {  //does not include target, lags, action
		var str= "GFX_Rack([";
		efxs.do{|efx| str= str++"\n\t%,".format(efx.code)};
		str= str++"\n]";
		if(outbus!=0, {
			str= str++", bus: %".format(outbus);
		});
		if(numChannels!=2, {
			str= str++", numChannels: %".format(numChannels);
		});
		str= str++");";
		^str
	}

	gui {|position, version= 0|
		^GFX_RackGUI(this, version:version).moveTo(*position.asRect.asArray.drop(2))
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
