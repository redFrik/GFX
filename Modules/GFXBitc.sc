//bitcrusher adapted from adc's bit_reduction example found in examples folder

GFXBitc : GFX_Module {

	*ar {|in, rate= 0.5, bits= 4|
		^Latch.ar(in, Impulse.ar(SampleRate.ir*0.5*rate)).round(0.5**bits)
	}

	*specs {
		^(
			bitcRate: ControlSpec(0, 1),
			bitcBits: ControlSpec(0, 12)
		)
	}
}

/*
{GFXBitc.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
