//safety
//to protect against inf use as first module in rack. mix to 1.0

GFXAaaa : GFX_Module {

	*ar {|in, gain= 0|
		CheckBadValues.ar(in, 9999);
		^Sanitize.ar(in)*gain.dbamp
	}

	*specs {
		^(
			aaaaGain: ControlSpec(-inf, 12, 'db', units: " dB")
		)
	}
}
