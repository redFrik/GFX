//simple echo

GFXAllp : GFX_Module {
	classvar <>maxDel= 1;

	*ar {|in, dly= 0.3, dec= 1|
		^AllpassC.ar(in, maxDel, dly.min(maxDel), dec)
	}

	*specs {
		^(
			allpDly: ControlSpec(0.0001, maxDel, 'exp', units: " secs"),
			allpDec: ControlSpec(0, 100)
		)
	}
}
