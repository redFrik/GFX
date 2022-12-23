//bitcrusher

GFXBitx : GFX_Module {

	*ar {|in, rate= 0.5, bits= 4|
		var crux= (in*bits+bits).floor-bits/bits;
		^Latch.ar(crux, Impulse.ar(SampleRate.ir*0.5*rate))
	}

	*specs {
		^(
			bitxRate: ControlSpec(0, 1),
			bitxBits: ControlSpec(0, 12)
		)
	}
}

/*
{GFXBitx.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
