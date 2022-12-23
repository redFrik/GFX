//bitcrusher with ringmodulation

GFXBit8 : GFX_Module {

	*ar {|in, rate= 0.5, bits= 4, freq= 150|
		var ring= in*FSinOsc.ar(freq);
		var crux= (ring*bits+bits).floor-bits/bits;
		^Latch.ar(crux, Impulse.ar(SampleRate.ir*0.5*rate))
	}

	*specs {
		^(
			bit8Rate: ControlSpec(0, 1),
			bit8Bits: ControlSpec(0, 12),
			bit8Freq: ControlSpec(20, 20000, 'exp')
		)
	}
}

/*
{GFXBit8.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
