//rack feedback

GFXAaaa : GFX_Module {
	classvar <>maxDel= 1;

	*ar {|in, dly= 0.3, gain= 0, loFreq= 258, loGain= 0, hiFreq= 8249, hiGain= 0|
		in= InFeedback.ar(\feedback.kr, in.numChannels);
		in= BLowShelf.ar(in, loFreq, 1, loGain);
		in= BHiShelf.ar(in, hiFreq, 1, hiGain);
		^DelayC.ar(in, maxDel, dly, gain.dbamp)
	}

	*specs {
		^(
			aaaaDly: ControlSpec(0.0001, maxDel, 'exp', units: " secs"),
			aaaaGain: ControlSpec(-inf, 12, 'db', units: " dB"),
			aaaaLoFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			aaaaLoGain: ControlSpec(-12, 12, units: " dB"),
			aaaaHiFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			aaaaHiGain: ControlSpec(-12, 12, units: " dB")
		)
	}
}
