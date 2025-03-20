//waveshaping / exciter

GFXPoly : GFX_Module {

	*ar {|in, brightness= 0.5|
		^in+
		(in*in*brightness)+
		(in*in*in*brightness*brightness)+
		(in*in*in*in*brightness*brightness*brightness)+
		(in*in*in*in*in*brightness*brightness*brightness*brightness)+
		(in*in*in*in*in*in*brightness*brightness*brightness*brightness*brightness)
	}

	*specs {
		^(
			polyBrightness: ControlSpec(0, 1)
		)
	}
}

/*
{GFXPoly.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
