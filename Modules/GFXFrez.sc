//frequency shifterz / phasor / flanger

GFXFrez : GFX_Module {

	*ar {|in, shift= 0.01, shift2= -0.013|
		^FreqShift.ar(in+FreqShift.ar(in, shift), shift2)
	}

	*specs {
		^(
			frezShift: ControlSpec(-10, 10),
			frezShift2: ControlSpec(-10, 10)
		)
	}
}

/*
{GFXFrez.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
