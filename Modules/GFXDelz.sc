//single delay with resonant filter

GFXDelz : GFX_Module {
	classvar <>maxDel= 1;

	*ar {|in, dly= 0.3, freq= 500, rq= 0.5, gain= 0|
		^DelayC.ar(BPF.ar(in, freq, rq, gain.dbamp), maxDel, dly.min(maxDel))
	}

	*specs {
		^(
			delzDly: ControlSpec(0.0001, maxDel, 'exp', units: " secs"),
			delzFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			delzRQ: ControlSpec(0.001, 1, 'exp'),
			delzGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}
