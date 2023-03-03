//safety
//to protect against inf use as first module in rack. mix to 1.0

GFXAaaa : GFX_Module {

	*ar {|in|
		CheckBadValues.ar(in, 9999);
		^Sanitize.ar(in)
	}

	*specs {
		^()
	}
}
