//from featureCreep (sustainer)

GFXSust : GFX_Module {

	*ar {|in, thresh= 0.2, gain= -9|
		^CompanderD.ar(in, thresh, 0.1, 1, 0.01, 0.01, gain.dbamp)
	}

	*specs {
		^(
			sustThresh: ControlSpec(0, 1),
			sustGain: ControlSpec(-inf, 0.0, 'db', units: " dB")
		)
	}
}
