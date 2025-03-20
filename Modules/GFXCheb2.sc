//6 Chebyshev+LFO

GFXCheb2 : GFX_Module {

	*ar {|in, a1= 1, a2= 0.5, a3= 0.4, a4= 0.3, a5= 0.2, a6= 0.1, rate= 0.2, gain= 0|
		var s2= in*2;
		var p1= in;
		var p2= p1*s2-1;
		var p3= p2*s2-p1;
		var p4= p3*s2-p2;
		var p5= p4*s2-p3;
		var p6= p5*s2-p4;
		rate= rate!in.numChannels;
		^LeakDC.ar(
			Mix([
				p1*a1*LFNoise2.kr(rate, 0.5, 0.5),
				p2*a2*LFNoise2.kr(rate, 0.5, 0.5),
				p3*a3*LFNoise2.kr(rate, 0.5, 0.5),
				p4*a4*LFNoise2.kr(rate, 0.5, 0.5),
				p5*a5*LFNoise2.kr(rate, 0.5, 0.5),
				p6*a6*LFNoise2.kr(rate, 0.5, 0.5)
			])
		)/(a1+a2+a3+a4+a5+a6).abs*gain.dbamp
	}

	*specs {
		^(
			cheb2A1: ControlSpec(0, 2),
			cheb2A2: ControlSpec(0, 2),
			cheb2A3: ControlSpec(0, 2),
			cheb2A4: ControlSpec(0, 2),
			cheb2A5: ControlSpec(0, 2),
			cheb2A6: ControlSpec(0, 2),
			cheb2Rate: ControlSpec(0.0001, 10, 'exp'),
			cheb2Gain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}
