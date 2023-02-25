//filterbank

GFXBPFs : GFX_Module {
	classvar <>numBands= 15;

	*ar {|in, loFreq= 80, hiFreq= 8000, rq= 0.1, rate= 0.1, gain= 3|
		var freqs= (0..numBands-1).linexp(0, numBands-1, loFreq, hiFreq);
		var lfos= LFNoise2.kr(rate!numBands, 5);
		^in.collect{|i| BPF.ar(i, freqs, rq, lfos).sum*gain.dbamp}
	}

	*specs {
		^(
			bpfsLoFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			bpfsHiFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			bpfsRQ: ControlSpec(0.001, 1, 'exp'),
			bpfsRate: ControlSpec(0.001, 10, 'exp', units: " Hz"),
			bpfsGain: ControlSpec(-24, 24, units: " dB")
		)
	}
}
