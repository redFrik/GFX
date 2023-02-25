//based on RedDestroyer

GFXDest : GFX_Module {

	*ar {|in, thresh= -12, lag= 0.001, gain= 6|
		^in*(Lag.ar(in.abs, lag)<thresh.dbamp)*gain.dbamp
	}

	*specs {
		^(
			destThresh: ControlSpec(-inf, 0, 'db', units: " dB"),
			destLag: ControlSpec(0, 0.01, units: " secs"),
			destGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}
