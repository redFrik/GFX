//low-pass filter with resonance

GFXRLPF : GFX_Module {

	*ar {|in, freq= 440, rq= 0.1, gain= 6|
		^RLPF.ar(in, freq, rq, gain.dbamp)
	}

	*specs {
		^(
			rlpfFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			rlpfRQ: ControlSpec(0.001, 1, 'exp'),
			rlpfGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXRLPF.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
