//absolute with hipass filter

GFXAbsh : GFX_Module {

	*ar {|in, freq= 9|
		^HPF.ar(in.abs, freq)
	}

	*specs {
		^(
			abshFreq: ControlSpec(1, 1000, 'exp', units: " Hz")
		)
	}
}
