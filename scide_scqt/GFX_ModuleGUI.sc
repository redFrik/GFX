//related: GFX_Module, GFX_RackGUI, RedEffectModuleGUI

GFX_ModuleGUI : AbstractGFXGUI {

	prInit {|parent, bounds, version, skin|
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

		view= View(parent, bounds).layout_(hl)
		.name_(efx.def.name)
		.onClose_({controller.remove});
	}

	prV0 {|hl, skin|
		efx.specs.do{|assoc|
			var ref= efx.cvs[assoc.key];
			var spec= assoc.value;
			var vl, slider, knob, number, text;

			//--mix slider
			if(efx.mixKeys.includes(assoc.key), {
				slider= GUICVSliderLabel(nil, nil, ref, spec, (string: assoc.key));
				hl.add(slider);

			}, {

				//--knob, numberbox and label
				vl= VLayout().spacing_(1);

				knob= GUICVKnob(nil, nil, ref, spec, update:false);

				number= GUICVNumberBox(nil, nil, ref, spec)
				.fixedSize_(Size(skin.knobWidth, skin.buttonHeight));

				text= GUICV.staticText.string_(assoc.key);

				vl.add(knob, align: \center);
				vl.add(number, align: \center);
				vl.add(text, align: \center);
				hl.add(vl);
			});
		};
	}

	prV1 {|hl, skin|
		var vl= VLayout().spacing_(skin.spacing);

		efx.specs.do{|assoc|
			var ref= efx.cvs[assoc.key];
			var spec= assoc.value;
			var slider, number;

			slider= GUICVSliderLabel(nil, nil, ref, spec, (string: assoc.key))
			.fixedHeight_(skin.buttonHeight)
			.minWidth_(skin.knobWidth*2)
			.orientation_(\horizontal);

			number= GUICVNumberBox(nil, nil, ref, spec)
			.fixedHeight_(skin.buttonHeight)
			.minWidth_(skin.knobWidth);

			vl.add(HLayout(slider, number));
		};
		hl.add(vl);
	}
}
