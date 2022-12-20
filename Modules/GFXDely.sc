//from featureCreep (delay)

GFXDely : GFX_Module {
	classvar <>maxDel= 4;

	*ar {|in, dur1= 801, dur2= 800, dec1= 1, dec2= 1, dec3= 50, dd= 2|
		var d1= AllpassC.ar(in, 1, 1/dur1, dec1);
		var d2= AllpassC.ar(in, 1, 1/dur2, dec2);
		^AllpassC.ar(d1+d2*0.4, maxDel, dd, dec3.lag)
	}

	*specs {
		^(
			delyDur1: ControlSpec(1, 9999, 'exp'),
			delyDur2: ControlSpec(1, 9999, 'exp'),
			delyDec1: ControlSpec(0, 100),
			delyDec2: ControlSpec(0, 100),
			delyDec3: ControlSpec(0, 100),
			delyDD: ControlSpec(0.0001, maxDel, 'exp', units: " secs")
		)
	}
}
