//based on RedDestroyer

GFXDest : GFX_Module {

	*ar {|in, thresh= 0.25, lag= 0.001, gain= 6|
		^in*(Lag.ar(in.abs, lag)<thresh)*gain.dbamp
	}

	*specs {
		^(
			destThresh: ControlSpec(0, 1),
			destLag: ControlSpec(0, 0.01, units: " secs"),
			destGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}
