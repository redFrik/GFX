//redFrik

//related: GFX_Module, GFX_Rack

AbstractGFX {

	var <specs;  //Array (key -> ControlSpec)
	var <cvs;  //Event (key: Ref)
	var <lookup;  //IdentityDictionary for looking up *ar argument names
	var <def;
	var <synth;
	var <numChannels;
	var <target;

	*new {|target, numChannels|
		^super.new.initAbstractGFX(target, numChannels)
	}

	initAbstractGFX {|argTarget, argNumChannels|
		specs= [];
		cvs= ();
		lookup= IdentityDictionary[];
		numChannels= argNumChannels;
		target= argTarget;
	}

	//--synth

	bus_ {|val|
		synth.set(\bus, val);
	}

	free {
		synth.free;
	}

	//--normalized access

	get {|key|  //return normalized value or nil
		var ref= cvs[key];
		if(ref.notNil, {
			^this.specForKey(key).unmap(ref.value)
		}, {
			"%: get key '%' not found".format(this.class.name, key).warn;
		});
		^nil
	}

	set {|key, val|  //val should be normalized
		var ref= cvs[key];
		var spec;
		if(ref.notNil, {
			spec= this.specForKey(key);
			ref.value_(spec.map(val)).changed(\value);
		}, {
			"%: set key '%' not found".format(this.class.name, key).warn;
		});
	}

	softSet {|key, val, within= 0.05|  //val should be normalized
		var ref= cvs[key];
		var spec;
		if(ref.notNil, {
			spec= this.specForKey(key);
			if((val-spec.unmap(ref.value)).abs<=within, {
				ref.value_(spec.map(val)).changed(\value);
				^true
			});
		}, {
			"%: softSet key '%' not found".format(this.class.name, key).warn;
		});
		^false
	}

	//--introspection and helper methods

	postActive {  //just post active modules (mix > 0)
		"%: active modules:".format(this.class.name).postln;
		this.active.do{|x| "\t%: %".format(x.key, x.value).postln};
	}

	postDiff {  //just post what differ from defaults
		"%: parameters that differ:".format(this.class.name).postln;
		cvs.keysValuesDo{|k, v|
			var spec;
			if(k==\pause, {
				if(v.value, {
					"\t%: % (default false)".format(k, v.value).postln;
				});
			}, {
				spec= this.specForKey(k);
				if(spec.default!=v.value, {
					"\t%: % (default %)".format(k, v.value, spec.default).postln;
				});
			});
		}
	}

	active {  //return any mix > 0
		var res= [];
		this.mixKeys.do{|k|
			var v= cvs[k].value;
			if(v>0, {res= res++(k -> v)});
		};
		^res
	}

	diff {  //return values that differ from spec default
		var res= [];
		var defaults= this.defaults;
		this.values.pairsDo{|k, v|
			defaults.pairsDo{|kk, vv|
				if(kk==k, {
					if(vv!=v, {
						res= res++k++v;
					});
				});
			};
		};
		^res
	}

	mixKeys {  //return mix key(s)
		^lookup.select{|v, k| v==\mix}.keys.asArray
	}

	specForKey {|key|  //return ControlSpec
		^specs.detect{|assoc| key==assoc.key}.value
	}

	defaults {  //return spec default values
		^specs.collect{|assoc| [assoc.key, assoc.value.default]}.flat
	}

	values {  //return current ref values
		^specs.collect{|assoc| [assoc.key, cvs[assoc.key].value]}.flat
	}

	bypass {  //turn off active (set mix key(s) to 0)
		this.active.do{|assoc| cvs[assoc.key].value_(0).changed(\value)};
	}

	restoreDefaults {|skipMix= false|
		cvs.keysValuesDo{|k, v|
			if((skipMix and:{lookup[k]==\mix}).not and:{k!=\pause}, {
				v.value_(this.specForKey(k).default).changed(\value);
			});
		};
	}

	//--required methods

	gui {|position, version= 0| ^this.subclassResponsibility(thisMethod)}

	prBuildDef {^this.subclassResponsibility(thisMethod)}

	//--private

	prAddMethod {|key, ref, spec|
		var func= if(spec.notNil, {
			{|obj, val| ref.value_(spec.constrain(val)).changed(\value); this}
		}, {
			{|obj, val| ref.value_(val).changed(\value); this}
		});
		this.addUniqueMethod(key.asSetter, func);
		this.addUniqueMethod(key, {|obj| ref.value});
	}
}
