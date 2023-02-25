//cross-over distortion

GFXDiod : GFX_Module {

	*ar {|in, thresh= 0.1, factor= 1|
		var i= in.abs;
		^Select.ar(i>thresh, [in/thresh**factor.reciprocal*i, in])
	}

	*specs {
		^(
			diodThresh: ControlSpec(0, 2),
			diodFactor: ControlSpec(0.001, 10, 'exp')
		)
	}
}

/*
{GFXDiod.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
