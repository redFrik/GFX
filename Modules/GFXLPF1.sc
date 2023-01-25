//simple low-pass filter

GFXLPF1 : GFX_Module {

	*ar {|in, freq= 440, gain= 3|
		^LPF.ar(in, freq, gain.dbamp)
	}

	*specs {
		^(
			lpf1Freq: \freq.asSpec,
			lpf1Gain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXLPF1.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
