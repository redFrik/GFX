//related: GFX_Module, GFX_RackGUI, RedEffectModuleGUI

GFX_ModuleGUI : AbstractGFXGUI {

	prInit {|parent, bounds, version, skin|
		var label, controller;
		var hl= HLayout().margins_(skin.margin.asArray).spacing_(skin.spacing);

		//--build GUI widgets - different versions
		switch(version,
			1, {this.prV1(hl, skin)},
			{this.prV0(hl, skin)}
		);

		//--label and pause
		label= GUICV.staticText.string_(efx.prefix.join(Char.nl));
		hl.add(label, 1, \right);
		label.mouseDownAction_({|v, x, y, mod, num, cnt|
			if(cnt==2, {efx.pause_(efx.pause.not)});
		});
		controller= {|cv, val|
			label.stringColor_(if(val, {skin.foreground}, {skin.fontColor}));
		};
		efx.cvs[\pause].addAction(controller).update;

		//--create View
		view= View(parent, bounds).layout_(hl)
		.name_(efx.def.name)
		.onClose_({efx.cvs[\pause].removeAction(controller)});
	}

	//--versions

	prV0 {|hl, skin|
		efx.order.do{|key|
			var cv= efx.cvs[key];
			var vl, slider, knob, number, text;

			//--mix slider
			if(efx.mixKeys.includes(key), {
				slider= GUICVSliderLabel(nil, nil, cv.ref, cv.spec, false)
				.string_(key);
				hl.add(slider);

			}, {

				//--knob, numberbox and parameter name
				vl= VLayout().spacing_(1);

				knob= GUICVKnob(nil, nil, cv.ref, cv.spec, false);

				number= GUICVNumberBox(nil, nil, cv.ref, cv.spec, false)
				.fixedSize_(Size(skin.knobWidth, skin.buttonHeight));

				text= GUICV.staticText.string_(key);

				vl.add(knob, align: \center);
				vl.add(number, align: \center);
				vl.add(text, align: \center);
				hl.add(vl);
			});
		};
	}

	prV1 {|hl, skin|
		var vl= VLayout().spacing_(skin.spacing);

		efx.order.do{|key|
			var cv= efx.cvs[key];
			var slider, number;

			slider= GUICVSliderLabel(nil, nil, cv.ref, cv.spec, false)
			.fixedHeight_(skin.buttonHeight)
			.minWidth_(skin.knobWidth*2)
			.orientation_(\horizontal)
			.string_(key);

			number= GUICVNumberBox(nil, nil, cv.ref, cv.spec, false)
			.fixedHeight_(skin.buttonHeight)
			.minWidth_(skin.knobWidth);

			vl.add(HLayout(slider, number));
		};
		hl.add(vl);
	}
}
