//exciter

GFXPoly2 : GFX_Module {

	*ar {|in, a= -3, b= -6, c= -9, d= -12, e= -15, f= -18|
		^in+
		(in*in*a.dbamp)+
		(in*in*in*b.dbamp)+
		(in*in*in*in*c.dbamp)+
		(in*in*in*in*in*d.dbamp)+
		(in*in*in*in*in*in*e.dbamp)+
		(in*in*in*in*in*in*in*f.dbamp)
	}

	*specs {
		^(
			poly2A: ControlSpec(-inf, 12, 'db', units: " dB"),
			poly2B: ControlSpec(-inf, 12, 'db', units: " dB"),
			poly2C: ControlSpec(-inf, 12, 'db', units: " dB"),
			poly2D: ControlSpec(-inf, 12, 'db', units: " dB"),
			poly2E: ControlSpec(-inf, 12, 'db', units: " dB"),
			poly2F: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXPoly2.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
