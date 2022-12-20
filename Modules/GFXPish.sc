//pitch shifter

GFXPish : GFX_Module {

	*ar {|in, dly= 0.2, ps= 0.5, pd= 0.001, td= 0.1|
		^PitchShift.ar(in, dly, ps, pd, td.min(dly))
	}

	*specs {
		^(
			pishDly: ControlSpec(0.0001, 1, 'exp', units: " secs"),
			pishPS: ControlSpec(0, 4),
			pishPD: ControlSpec(0, 1),
			pishTD: ControlSpec(0, 1)
		)
	}
}
