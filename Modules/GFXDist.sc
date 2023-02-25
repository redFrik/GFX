//adapted from example in Tour_of_UGens helpfile

GFXDist : GFX_Module {

	*ar {|in, depth= 0.8, rate= 0.3, gain= -12|
		^SinOsc.ar(rate, in*(1+(depth*(8pi-1))))*gain.dbamp
	}

	*specs {
		^(
			distDepth: ControlSpec(0, 1),
			distRate: ControlSpec(0.001, 1000, 'exp', units: " Hz"),
			distGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXDist.ar(Line.ar(-1, 1, 0.01), gain:0)}.plotAudio
*/
