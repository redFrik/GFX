//redFrik fragments

GFXFold : GFX_Module {

	*ar {|in, thresh= 0.15|
		^in.fold2(thresh)
	}

	*specs {
		^(
			foldThresh: ControlSpec(0, 1)
		)
	}
}

/*
{GFXFold.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
