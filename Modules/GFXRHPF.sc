//high-pass filter with resonance

GFXRHPF : GFX_Module {

	*ar {|in, freq= 440, rq= 0.1, gain= 6|
		^RHPF.ar(in, freq, rq, gain.dbamp)
	}

	*specs {
		^(
			rhpfFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			rhpfRQ: ControlSpec(0.001, 1, 'exp'),
			rhpfGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXRHPF.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
