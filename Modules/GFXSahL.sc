//lagged sample and hold
//standard sah when time = 0

GFXSahL : GFX_Module {

	*ar {|in, freq= 7500, time= 0|
		var trg= Impulse.ar(freq);
		var sah= Latch.ar(in, trg);
		^Lag.ar(sah, time)
	}

	*specs {
		^(
			sahLFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			sahLTime: ControlSpec(0, 10, units: " secs")
		)
	}
}

/*
{GFXSahL.ar(Line.ar(-1, 1, 0.01), 1500)}.plotAudio
*/
