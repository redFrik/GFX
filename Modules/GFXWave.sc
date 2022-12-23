//adapted from example by Bram de Jong, musicdsp.org

GFXWave : GFX_Module {

	*ar {|in, amount= 25, gain= -8|
		^in*(in.abs+amount)/(in*in+(amount-1)*in.abs+1)*gain.dbamp
	}

	*specs {
		^(
			waveAmount: ControlSpec(0, 999),
			waveGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}

/*
{GFXWave.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
