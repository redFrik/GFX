//simple delay

GFXComb : GFX_Module {
	classvar <>maxDel= 1;

	*ar {|in, dly= 0.3, dec= 1|
		^CombC.ar(in, maxDel, dly.min(maxDel), dec)
	}

	*specs {
		^(
			combDly: ControlSpec(0, maxDel, 'exp', units: " secs"),
			combDec: ControlSpec(0, 100)
		)
	}
}
