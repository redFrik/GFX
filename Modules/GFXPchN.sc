//but numWindows is hardcoded

GFXPchN : GFX_Module {
	classvar <>windowDur= 0.1, <>numWindows= 3;

	*ar {|in, ratio= 4|
		var phasor= Phasor.ar(1, ratio*SampleDur.ir);
		var readHeads= {|i| (phasor+(i/numWindows)%1)}.dup(numWindows);
		^{|i|
			DelayC.ar(in[i], windowDur, readHeads*windowDur, sin(readHeads*pi)).sum
		}.dup(in.numChannels)
	}

	*specs {
		^(
			pchNRatio: ControlSpec(0.01, 10, 'exp')
		)
	}
}
