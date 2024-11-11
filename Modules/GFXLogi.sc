//logistic bipolar

GFXLogi : GFX_Module {

	*ar {|in, mul= 10, add= 0.05|
		in= in*mul+add;
		in= exp(-2*in);
		^(1-in/(in+1))*mul.lincurve(1, 99, 1, 0.01, -5)
	}

	*specs {
		^(
			logiMul: ControlSpec(1, 99),
			logiAdd: ControlSpec(0, 1)
		)
	}
}

/*
{GFXLogi.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
