//delayline modulated with amplitude follower
//negative del will reverse the amp-pitch mapping

GFXDela : GFX_Module {
	classvar <>maxDel= 1;

	*ar {|in, atk= 0.01, rel= 0.01, del= 0.05, lag= 0.1|
		var amp= Amplitude.ar(in, atk, rel, del, 0-del.min(0)).min(maxDel).lag(lag);
		^DelayC.ar(in, maxDel, amp)
	}

	*specs {
		^(
			delaAtk: ControlSpec(0, 10),
			delaRel: ControlSpec(0, 10),
			delaDel: ControlSpec(0-maxDel, maxDel, units: " secs"),
			delaLag: ControlSpec(0, 10),
		)
	}
}
