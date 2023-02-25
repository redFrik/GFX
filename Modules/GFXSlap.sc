//simple slap-back echo

GFXSlap : GFX_Module {
	classvar <>maxDel= 0.5;

	*ar {|in, dly= 0.23|
		^DelayN.ar(in, maxDel, dly)
	}

	*specs {
		^(
			slapDly: ControlSpec(0.0001, maxDel, 'exp', units: " secs")
		)
	}
}
