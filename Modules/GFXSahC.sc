//sample and hold with leaky capacitor

GFXSahC : GFX_Module {

	*ar {|in, freq= 7500, charge= 50, discharge= 500, curve= -3|
		var trg= Impulse.ar(freq);
		var sah= Latch.ar(in, trg);
		var cap= EnvGen.ar(Env(#[0, 1, 0], [charge, discharge]/freq*0.01, curve), trg);
		^sah*cap
	}

	*specs {
		^(
			sahCFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			sahCCharge: ControlSpec(0, 100, units: " %"),
			sahCDisCharge: ControlSpec(0, 1000, units: " %"),
			sahCCurve: ControlSpec(-8, 8)
		)
	}
}

/*
{GFXSahC.ar(Line.ar(-1, 1, 0.01), 1500)}.plotAudio
*/
