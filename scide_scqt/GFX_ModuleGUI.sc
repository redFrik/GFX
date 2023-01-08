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
		controller= CV(efx.cvs[\pause]).addAction({|cv, val|
			label.stringColor_(if(val, {skin.foreground}, {skin.fontColor}));
		});
		hl.add(label, 1, \right);
		label.mouseDownAction_({|v, x, y, mod, num, cnt|
			if(cnt==2, {efx.pause_(efx.pause.not)});
		});

		Routine({
			efx.target.server.sync;
			efx.cvs.do{|ref| ref.changed(\value)};
		}).play(AppClock);

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
				slider= GUICVSliderLabel(nil, nil, ref, spec, false)
				.string_(assoc.key);
				hl.add(slider);

			}, {

				//--knob, numberbox and parameter name
				vl= VLayout().spacing_(1);

				knob= GUICVKnob(nil, nil, ref, spec, false);

				number= GUICVNumberBox(nil, nil, ref, spec, false)
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

			slider= GUICVSliderLabel(nil, nil, ref, spec, false)
			.fixedHeight_(skin.buttonHeight)
			.minWidth_(skin.knobWidth*2)
			.orientation_(\horizontal)
			.string_(assoc.key);

			number= GUICVNumberBox(nil, nil, ref, spec, false)
			.fixedHeight_(skin.buttonHeight)
			.minWidth_(skin.knobWidth);

			vl.add(HLayout(slider, number));
		};
		hl.add(vl);
	}
}
