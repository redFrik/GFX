//adapted from jmc's tank example in LocalIn helpfile

GFXTank : GFX_Module {

	*ar {|in, fb= 0.98, dec= 1, damp= 1, angle= 0.55|
		var z;
		4.do{
			in= AllpassN.ar(in, 0.02, {Rand(0.005, 0.02)}.dup(in.numChannels), dec, damp);
		};
		z= LocalIn.ar(in.numChannels).asArray*fb;
		z= OnePole.ar(z, 0.5);

		//z= Rotate2.ar(z[0], z[1], angle);
		z= {|i|
			{|j|
				z.wrapAt(i+j)*cos(j*(pi/z.numChannels)+angle)
			}.dup(z.numChannels).sum;
		}.dup(z.numChannels)*z.numChannels.reciprocal.sqrt;

		z= AllpassN.ar(z, 0.05, {Rand(0.01, 0.05)}.dup(in.numChannels), 2*dec, damp);
		z= DelayN.ar(z, 0.26, {Rand(0.1, 0.26)}.dup(in.numChannels));
		z= AllpassN.ar(z, 0.05, {Rand(0.03, 0.05)}.dup(in.numChannels), 2*dec, damp);

		z= LeakDC.ar(z);
		z= z+in;
		LocalOut.ar(z);
		^z
	}

	*specs {
		^(
			tankFB: ControlSpec(0, 2),
			tankDec: ControlSpec(0, 10),
			tankDamp: ControlSpec(0, 10),
			tankAngle: ControlSpec(-pi, pi)
		)
	}
}
