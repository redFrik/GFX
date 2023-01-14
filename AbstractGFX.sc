//redFrik

//related: GFX_Module, GFX_Rack, CV

AbstractGFX {

	var <cvs;  //Event (key: CV)
	var <lookup;  //IdentityDictionary for looking up *ar argument names
	var <def;
	var <synth;
	var <numChannels;
	var <target;

	*new {|target, numChannels|
		^super.new.initAbstractGFX(target, numChannels)
	}

	initAbstractGFX {|argTarget, argNumChannels|
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

	lags_ {|val| ^this.subclassResponsibility(thisMethod)}

	//--normalized access

	get {|key|  //return normalized value or nil
		var cv= cvs[key];
		if(cv.notNil, {
			^cv.get
		}, {
			"%: get key '%' not found".format(this.class.name, key).warn;
		});
		^nil
	}

	set {|key, val|  //val should be normalized
		var cv= cvs[key];
		if(cv.notNil, {
			cv.set(val);
		}, {
			"%: set key '%' not found".format(this.class.name, key).warn;
		});
	}

	softSet {|key, val, within= 0.05|  //val should be normalized
		var cv= cvs[key];
		if(cv.notNil, {
			^cv.softSet(val, within)
		}, {
			"%: softSet key '%' not found".format(this.class.name, key).warn;
		});
		^false
	}

	//--introspection

	active {  //return any mix > 0
		var res= [];
		this.mixKeys.do{|k|
			var v= cvs[k].value;
			if(v>0, {res= res.add((k -> v))});
		};
		^res
	}

	defaults {  //return spec defaults
		var res= Array(cvs.size*2);
		cvs.keysValuesDo{|k, cv| res.add(k).add(cv.default)};
		^res
	}

	diff {  //return values that differ from spec default
		var res= [];
		var defaults= this.defaults;
		this.values.pairsDo{|k, v|
			defaults.pairsDo{|kk, vv|
				if(kk==k and:{vv!=v}, {
					res= res.add(k).add(v);
				});
			};
		};
		^res
	}

	mixKeys {  //return mix key(s)
		^lookup.select{|v| v==\mix}.keys.asArray
	}

	postActive {  //just post active modules (mix > 0)
		"%: active modules:".format(this.class.name).postln;
		this.active.do{|a| "\t%: %".format(a.key, a.value).postln};
	}

	postDiff {  //just post what differ from defaults
		"%: parameters that differ:".format(this.class.name).postln;
		cvs.keysValuesDo{|k, cv|
			if(cv.default!=cv.value, {
				"\t%: % (default %)".format(k, cv.value, cv.default).postln;
			});
		}
	}

	values {  //return current CV values
		var res= Array(cvs.size*2);
		cvs.keysValuesDo{|k, cv| res.add(k).add(cv.value)};
		^res
	}

	//--convenience

	bypass {  //turn off active (set mix key(s) to 0)
		this.active.do{|assoc| cvs[assoc.key].value_(0).changed(\value)};
	}

	code {|verbose= false| ^this.subclassResponsibility(thisMethod)}

	gui {|position, version= 0| ^this.subclassResponsibility(thisMethod)}

	restoreDefaults {|skipMix= false|
		cvs.keysValuesDo{|k, cv|
			if((skipMix and:{lookup[k]==\mix}).not, {
				cv.value_(cv.default).changed(\value);
			});
		};
	}

	//--private

	prAddMethod {|key, cv|
		this.addUniqueMethod(key.asSetter, {|obj, v| cv.value_(v); this});
		this.addUniqueMethod(key, {|obj| cv.value});
	}

	prBuildDef {|lags| ^this.subclassResponsibility(thisMethod)}
}
