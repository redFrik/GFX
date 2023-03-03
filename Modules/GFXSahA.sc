//sample and hold with rate mapped to amplitude

GFXSahA : GFX_Module {

	*ar {|in, min= 0, max= 1, curve= -3|
		var rate= in.abs.lincurve(0, 1, max, min, curve)*SampleRate.ir*0.5;
		var trig= Impulse.ar(rate.max(1));
		^Latch.ar(in, trig)
	}

	*specs {
		^(
			sahAMin: ControlSpec(0, 1),
			sahAMax: ControlSpec(0, 1),
			sahACurve: ControlSpec(-24, 4)
		)
	}
}

/*
{GFXSahA.ar(Line.ar(-1, 1, 0.01))}.plotAudio
*/
