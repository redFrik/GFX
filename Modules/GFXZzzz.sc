//basic eq

GFXZzzz : GFX_Module {

	*ar {|in, loFreq= 258, loGain= 6.75, hiFreq= 8249, hiGain= 2.75|
		in= BLowShelf.ar(in, loFreq, 1, loGain);
		in= BHiShelf.ar(in, hiFreq, 1, hiGain);
		^in
	}

	*specs {
		^(
			zzzzLoFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			zzzzLoGain: ControlSpec(-12, 12, units: " dB"),
			zzzzHiFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			zzzzHiGain: ControlSpec(-12, 12, units: " dB")
		)
	}
}
