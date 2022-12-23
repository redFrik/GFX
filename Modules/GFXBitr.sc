//simple bitdepth reduction

GFXBitr : GFX_Module {

	*ar {|in, bits= 4|
		^in.round(0.5**bits)
	}

	*specs {
		^(
			bitrBits: ControlSpec(0, 16)
		)
	}
}

/*
{GFXBitr.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
