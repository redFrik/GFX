//frequency shifter

GFXFreq : GFX_Module {

	*ar {|in, shift= -50|
		^FreqShift.ar(in, shift)
	}

	*specs {
		^(
			freqShift: ControlSpec(-1000, 1000)
		)
	}
}

/*
{GFXFreq.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
