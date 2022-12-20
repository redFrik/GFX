//related: GFX_Module, GFX_RackGUI, RedEffectModuleGUI

GFX_ModuleGUI : AbstractGFXGUI {

	prInit {|skin|
		var label, controller;
		var hl= HLayout().margins_(skin.margin.asArray).spacing_(skin.spacing);

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
				vl.add(GUICVStaticText(assoc.key), align: \center);
				hl.add(vl);
			});
		};

		label= StaticText().string_(efx.prefix.join(Char.nl))
		.font_(Font(skin.fontSpecs[0], skin.fontSpecs[1]));
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
}
