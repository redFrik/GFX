//from http://www.spinsemi.com/knowledge_base/effects.html

GFXDisx : GFX_Module {

	*ar {|in, thresh= -6, gain= 0|
		var a= in.abs;
		var t= thresh.dbamp;
		^Select.ar(a>t, [in, 2-(1/(a/t))*t*in.sign])*gain.dbamp
	}

	*specs {
		^(
			disxThresh: ControlSpec(-inf, 12, 'db', units: " dB"),
			disxGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXDisx.ar(Line.ar(-1, 1, 0.01), -6)}.plotAudio
*/
