//slewed sample and hold
//TODO: somehow fix hickup when changing time

GFXSahS : GFX_Module {

	*ar {|in, freq= 7500, time= 0.001, curve= -5|
		var trg= Impulse.ar(freq);
		var sah= Latch.ar(in, trg);
		^VarLag.ar(sah, time, curve)
	}

	*specs {
		^(
			sahSFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			sahSTime: ControlSpec(0, 10, units: " secs"),
			sahSCurve: ControlSpec(-8, 8)
		)
	}
}

/*
{GFXSahS.ar(Line.ar(-1, 1, 0.01), 1500)}.plotAudio
*/
