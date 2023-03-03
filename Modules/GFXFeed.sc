//rack feedback

GFXFeed : GFX_Module {
	classvar <>maxDel= 1;

	*ar {|in, dly= 0.3, gain= 0, loFreq= 258, loGain= 0, hiFreq= 8249, hiGain= 0|
		in= InFeedback.ar(\feedback.kr, in.numChannels);
		in= BLowShelf.ar(in, loFreq, 1, loGain);
		in= BHiShelf.ar(in, hiFreq, 1, hiGain);
		^DelayC.ar(in, maxDel, dly, gain.dbamp)
	}

	*specs {
		^(
			feedDly: ControlSpec(0.0001, maxDel, 'exp', units: " secs"),
			feedGain: ControlSpec(-inf, 12, 'db', units: " dB"),
			feedLoFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			feedLoGain: ControlSpec(-12, 12, units: " dB"),
			feedHiFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			feedHiGain: ControlSpec(-12, 12, units: " dB")
		)
	}
}
