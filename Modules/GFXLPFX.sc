//simple low-pass filter

GFXLPFX : GFX_Module {

	*ar {|in, freq= 440, gain= 3|
		^LPF.ar(in, freq, gain.dbamp)
	}

	*specs {
		^(
			lpfxFreq: \freq.asSpec,
			lpfxGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXLPFX.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
