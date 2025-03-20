//by Partice Tarrabia and Bram de Jong, musicdsp.org
//similar to GFXWave

GFXOver : GFX_Module {

	*ar {|in, amount= 0.5, gain= 0|
		var k= 2*amount/(1-amount.min(0.99999));
		^(1+k)*in/(1+(k*in.abs))*gain.dbamp
	}

	*specs {
		^(
			overAmount: ControlSpec(0, 1),
			overGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXOver.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
