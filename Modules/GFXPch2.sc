//only two overlapping windows

GFXPch2 : GFX_Module {
	classvar <>windowDur= 0.1;

	*ar {|in, ratio= 4|
		var phasor1= Phasor.ar(1, ratio*SampleDur.ir);
		var phasor2= phasor1+0.5%1;
		var read1= DelayC.ar(in, windowDur, phasor1*windowDur);
		var read2= DelayC.ar(in, windowDur, phasor2*windowDur);
		^(read1*sin(phasor1*pi))+(read2*sin(phasor2*pi))
	}

	*specs {
		^(
			pch2Ratio: ControlSpec(0.01, 10, 'exp')
		)
	}
}
