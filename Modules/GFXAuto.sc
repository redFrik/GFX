//freq is target frequency

GFXAuto : GFX_Module {
	classvar <>windowDur= 0.1;

	*ar {|in, freq= 400, lag= 0.01|
		var pitch= Pitch.kr(in, freq).flop[0].lag(lag);
		var ratio= (1-(freq/pitch))/windowDur;
		var phasor1= Phasor.ar(1, ratio*SampleDur.ir);
		var phasor2= phasor1+0.5%1;
		var read1= DelayC.ar(in, windowDur, phasor1*windowDur);
		var read2= DelayC.ar(in, windowDur, phasor2*windowDur);
		^(read1*sin(phasor1*pi))+(read2*sin(phasor2*pi))
	}

	*specs {
		^(
			autoFreq: ControlSpec(20, 20000, 'exp', units: " Hz"),
			autoLag: ControlSpec(0, 4)
		)
	}
}
