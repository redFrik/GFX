//chorus

GFXChor : GFX_Module {
	classvar <>maxDel= 0.1;
	classvar <>numLFOs= 4;

	*ar {|in, rate= 0.05, dMin= 0.03, dMax= 0.04|
		var arr= {|i|
			var lfos= LFNoise2.ar(2**(0..numLFOs-1)*rate, 1/(1..numLFOs)).linlin(-1, 1, dMin, dMax);
			DelayC.ar(in.asArray[i], maxDel, lfos, 1/numLFOs)
		}.dup(in.numChannels).flat;
		^{|i| {|j| arr.wrapAt(j*in.numChannels+i)}.dup(numLFOs).sum}.dup(in.numChannels)
	}

	*specs {
		^(
			chorRate: ControlSpec(0.001, 100, 'exp', units: " Hz"),
			chorDMin: ControlSpec(0, maxDel, units: " secs"),
			chorDMax: ControlSpec(0, maxDel, units: " secs")
		)
	}
}
