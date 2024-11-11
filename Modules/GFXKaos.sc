//from featureCreep (chaos)

GFXKaos : GFX_Module {

	*ar {|in, thresh= 0.4, amp= 0.75, diff= 0.1, loFreq= 2000, hiFreq= 12000|
		var a= LFNoise2.kr(1!in.numChannels, 0.67);
		var d= PitchShift.ar(in, 1, LFNoise2.kr(0.01!in.numChannels)*diff+1, diff/2, 0.5, a);
		var o= CompanderD.ar(d, thresh, 0.1, 1, 0.01, 0.01, amp).tanh;
		^LPF.ar(o, LFNoise2.kr(0.01!in.numChannels).expange(loFreq, hiFreq), a).tanh
	}

	*specs {
		^(
			kaosThresh: ControlSpec(0, 1),
			kaosAmp: ControlSpec(0, 4),
			kaosDiff: ControlSpec(0, 2),
			kaosLoFreq: ControlSpec(0.1, 9999, 'exp', units: " Hz"),
			kaosHiFreq: ControlSpec(999, 19999, 'exp', units: " Hz")
		)
	}
}
