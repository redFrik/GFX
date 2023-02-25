//Quarter-Circle Distortion from Zlatko https://www.youtube.com/watch?v=6BmZGlW8kqA

GFXDisq : GFX_Module {

	*ar {|in, gain= -9|
		^Select.ar(in>0, [0-(1-(in+1**2)).sqrt, (1-(1-in**2)).sqrt])*gain.dbamp
	}

	*specs {
		^(
			disqGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXDisq.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
