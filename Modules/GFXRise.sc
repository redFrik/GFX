//from memento - similar to GFXFall

GFXRise : GFX_Module {
	classvar <>maxDel= 1, <>numOverlaps= 4;

	*ar {|in, rate= -0.05|
		var phasors= LFSaw.ar(rate*(1..numOverlaps), (1..numOverlaps)/numOverlaps*2)+1/2;
		var windows= sin(phasors*pi)**2;
		var c= 3*rate.sign;
		^phasors.sum{|p, i| DelayC.ar(in, maxDel, p.lincurve(0, 1, 0, maxDel, c), windows[i])}
	}

	*specs {
		^(
			riseRate: ControlSpec(-10, 10)
		)
	}
}
