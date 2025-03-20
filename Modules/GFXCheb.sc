//n Chebyshev+LFO

GFXCheb : GFX_Module {

	*ar {|in, rate= 0.05, gain= 0, i_num= 6|
		var s2= in*2;
		var lfo= LFNoise2.kr(rate!in.numChannels!i_num, 0.5, 0.5);
		var arr= {|a, b, i|
			if(i<=1, {
				a
			}, {
				thisFunction.value(a++(a[a.size-1]*s2-b), a[a.size-1], i-1)
			});
		}.([in], 1, i_num);
		^LeakDC.ar(Mix(arr*lfo))/lfo.sum*gain.dbamp
	}

	*specs {
		^(
			chebRate: ControlSpec(0.0001, 10, 'exp'),
			chebGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}
