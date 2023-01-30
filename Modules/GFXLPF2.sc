//double low-pass filter

GFXLPF2 : GFX_Module {

	*ar {|in, freq= 440, gain= 6|
		^LPF.ar(LPF.ar(in, freq), freq, gain.dbamp)
	}

	*specs {
		^(
			lpf2Freq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			lpf2Gain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXLPF2.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
