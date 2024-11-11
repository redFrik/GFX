//sigma distortion v2

GFXSig2 : GFX_Module {

	*ar {|in, mul= 10, add= 0.05|
		in= in*mul+add;
		^in/(in*in+1).sqrt*mul.lincurve(1, 99, 1, 0.01, -5)
	}

	*specs {
		^(
			sig2Mul: ControlSpec(1, 99),
			sig2Add: ControlSpec(0, 1)
		)
	}
}

/*
{GFXSig2.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
