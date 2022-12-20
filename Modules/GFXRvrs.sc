//buffer duration hardcoded. also work as pitch shifter if rate is positive
//NOTE: will click at times

GFXRvrs : GFX_Module {
	classvar <>bufferDur= 8;

	*ar {|in, rate= -1|
		var buffer= LocalBuf(SampleRate.ir*bufferDur, in.numChannels);
		buffer.clear;
		BufWr.ar(in, buffer, Phasor.ar(1, 1, 0, BufFrames.ir(buffer)));
		^PlayBuf.ar(in.numChannels, buffer, rate, loop:1)
	}

	*specs {
		^(
			rvrsRate: ControlSpec(-8, 8)
		)
	}
}
