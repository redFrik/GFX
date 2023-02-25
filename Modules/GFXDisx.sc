//from http://www.spinsemi.com/knowledge_base/effects.html

GFXDisx : GFX_Module {

	*ar {|in, thresh= 0.5, gain= 0|
		^Select.ar(in.abs>thresh, [
			in,
			2*in.sign-(1/(in/thresh))*thresh
		])*gain.dbamp
	}

	*specs {
		^(
			disxThresh: ControlSpec(0, 1),
			disxGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXDisx.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
