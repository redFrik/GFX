//simple high-pass filter

GFXHPF1 : GFX_Module {

	*ar {|in, freq= 440, gain= 3|
		^HPF.ar(in, freq, gain.dbamp)
	}

	*specs {
		^(
			hpf1Freq: \freq.asSpec,
			hpf1Gain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXHPF1.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
