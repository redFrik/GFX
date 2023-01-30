//double high-pass filter

GFXHPF2 : GFX_Module {

	*ar {|in, freq= 440, gain= 6|
		^HPF.ar(HPF.ar(in, freq), freq, gain.dbamp)
	}

	*specs {
		^(
			hpf2Freq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			hpf2Gain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXHPF2.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
