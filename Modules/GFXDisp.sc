//simple polynomial distortion

GFXDisp : GFX_Module {

	*ar {|in, power= 2, gain= 0|
		^in.pow(power)*gain.dbamp
	}

	*specs {
		^(
			dispPower: ControlSpec(0, 40),
			dispGain: ControlSpec(-40, 40, 'db', units: " dB")
		)
	}
}

/*
{GFXDisp.ar(Line.ar(-1, 1, 0.01), 2, gain:0)}.plotAudio
*/
