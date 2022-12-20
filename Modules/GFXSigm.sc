//sigma distortion

GFXSigm : GFX_Module {

	*ar {|in, mul= 10, add= 0.05|
		^(in*mul+add).distort*mul.lincurve(1, 99, 1, 0.01, -5)
	}

	*specs {
		^(
			sigmMul: ControlSpec(1, 99),
			sigmAdd: ControlSpec(0, 1)
		)
	}
}

/*
{GFXSigm.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
