//soft distortion

GFXSoft : GFX_Module {

	*ar {|in, mul= 5, add= 0.05|
		^(in*mul+add).softclip*mul.lincurve(1, 99, 1, 0.01, -5)
	}

	*specs {
		^(
			softMul: ControlSpec(1, 99),
			softAdd: ControlSpec(0, 1)
		)
	}
}

/*
{GFXSoft.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
