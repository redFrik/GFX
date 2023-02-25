//ring modulation

GFXRing : GFX_Module {

	*ar {|in, freq= 400, mul= 1, det= 0|
		var detune= ((0..in.numChannels-1)*det).midiratio;
		^in*SinOsc.ar(freq*detune, 0, mul)
	}

	*specs {
		^(
			ringFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			ringMul: ControlSpec(0, 10),
			ringDet: ControlSpec(-12, 12)
		)
	}
}

/*
{GFXRing.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
