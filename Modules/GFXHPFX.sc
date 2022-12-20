//simple high-pass filter

GFXHPFX : GFX_Module {

	*ar {|in, freq= 440, gain= 3|
		^HPF.ar(in, freq, gain.dbamp)
	}

	*specs {
		^(
			hpfxFreq: \freq.asSpec,
			hpfxGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXHPFX.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
