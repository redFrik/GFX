//related: GFX_Module, GFX_RackGUI, RedEffectModuleGUI

GFX_ModuleGUI : AbstractGFXGUI {

	prInit {|version, skin|
		var label, controller;
		var hl= HLayout().margins_(skin.margin.asArray).spacing_(skin.spacing);

		switch(version,
			1, {this.prV1(hl, skin)},
			{this.prV0(hl, skin)}
		);

		label= GUICV.staticText.string_(efx.prefix.join(Char.nl));
		controller= SimpleController(efx.cvs[\pause]).put(\value, {|ref|
			label.stringColor_(if(ref.value, {skin.foreground}, {skin.fontColor}));
		});
		efx.cvs[\pause].changed(\value);
		hl.add(label, 1, \right);

		view= View().layout_(hl);
		view.name_(efx.def.name);

		view.onClose_({controller.remove});
	}

	prDkey {|guiCV|
		guiCV.keyDownAction_({|v ...args|
			v.defaultKeyDownAction(*args);
			if(args[0]==$d, {
				guiCV.value_(guiCV.spec.default)
			});
		});
	}

	prV0 {|hl, skin|
		efx.specs.do{|assoc|
			var ref= efx.cvs[assoc.key];
			var spec= assoc.value;
			var slider, vl, knob;

			//--mix slider
			if(efx.mixKeys.includes(assoc.key), {
				slider= GUICVSliderLabel(ref, spec, (string: assoc.key));
				this.prDkey(slider);
				hl.add(slider);

			}, {

				//--knob, numberbox and label
				vl= VLayout().spacing_(1);
				knob= GUICVKnob(ref, spec, update:false);
				this.prDkey(knob);
				vl.add(knob, align: \center);
				vl.add(GUICVNumberBox(ref, spec), align: \center);
				vl.add(GUICV.staticText.string_(assoc.key), align: \center);
				hl.add(vl);
			});
		};
	}

	prV1 {|hl, skin|
		var vl= VLayout().spacing_(skin.spacing);

		efx.specs.do{|assoc|
			var ref= efx.cvs[assoc.key];
			var spec= assoc.value;
			var v, n;

			v= GUICVSliderLabel(ref, spec, (string: assoc.key));
			v.orientation_(\horizontal);
			v.asView.minWidth_(skin.sliderHeight).fixedHeight_(skin.buttonHeight);
			v.minWidth_(skin.sliderHeight).fixedHeight_(skin.buttonHeight);
			this.prDkey(v);

			n= GUICVNumberBox(ref, spec);
			n.fixedSize_(Size(skin.knobWidth, skin.buttonHeight));

			vl.add(HLayout(v, n));
		};
		hl.add(vl);
	}
}
