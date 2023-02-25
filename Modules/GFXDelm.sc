//modulated delaylines - phasor

GFXDelm : GFX_Module {
	classvar <>maxDel= 0.1;

	*ar {|in, rate= 0.3, del= 0.01|
		var lfos= LFTri.ar(rate, (0..in.numChannels-1)/in.numChannels*4);
		^DelayC.ar(in, maxDel, lfos.range(0, del.clip(0, maxDel)))
	}

	*specs {
		^(
			delmRate: ControlSpec(0.001, 100, 'exp', units: " Hz"),
			delmDel: ControlSpec(0, maxDel, units: " secs")
		)
	}
}
