//tanh distortion

GFXTanh : GFX_Module {

	*ar {|in, mul= 25, add= 0.05|
		^(in*mul+add).tanh*mul.lincurve(1, 99, 1, 0.01, -5)
	}

	*specs {
		^(
			tanhMul: ControlSpec(1, 99),
			tanhAdd: ControlSpec(0, 1)
		)
	}
}

/*
{GFXTanh.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
