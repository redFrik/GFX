//atan distortion

GFXAtan : GFX_Module {

	*ar {|in, mul= 25, add= 0.05|
		^(in*mul+add).atan*mul.lincurve(1, 99, 1, 0.01, -5)
	}

	*specs {
		^(
			atanMul: ControlSpec(1, 99),
			atanAdd: ControlSpec(0, 1)
		)
	}
}

/*
{GFXAtan.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
