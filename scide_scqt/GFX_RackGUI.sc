//related: GFX_Rack, GFX_ModuleGUI, RedEffectsRackGUI

GFX_RackGUI : AbstractGFXGUI {
	classvar <>numModulesBeforeScroll= 8;

	prInit {|parent, bounds, version, skin|
		var hl, header, canvas;
		var macros, macroMenu;
		var viewWidth= 0, viewHeight;

		macros= [
			"_macros_" -> {},
			"defaults" -> {efx.restoreDefaults},
			"defaults skipMix" -> {efx.restoreDefaults(true)},
			"active" -> {efx.postActive},
			"diff" -> {efx.postDiff},
			"bypass" -> {efx.bypass},
			"pause all" -> {efx.pause= true},
			"random mix" -> {efx.mixKeys.do{|k| efx.set(k, if(0.5.coin, {1.0.linrand}, 0))}},
			"randomize some" -> {efx.cvs.keysDo{|k| if(0.2.coin, {efx.set(k, 1.0.rand)})}},
			"randomize many" -> {efx.cvs.keysDo{|k| if(0.5.coin, {efx.set(k, 1.0.rand)})}},
			"vary some" -> {efx.cvs.keysDo{|k| if(0.2.coin, {efx.set(k, efx.get(k)+0.gauss(0.05))})}},
			"vary many" -> {efx.cvs.keysDo{|k| if(0.5.coin, {efx.set(k, efx.get(k)+0.gauss(0.05))})}},
			"surprise" -> {efx.efxs.do{|x| x.set(x.cvs.keys.choose, #[0, 0.5, 1].choose)}},
			"post code" -> {efx.code.postln},
			"save preset" -> {Dialog.savePanel{|x| efx.values.writeArchive(x)}},
			"load preset" -> {Dialog.openPanel{|x|
				Object.readArchive(x).pairsDo{|k, v| efx.cvs[k].value_(v).changed(\value)}
			}}
		];

		//--header
		hl= HLayout();
		header= View().layout_(VLayout(hl).margins_(0));
		hl.add(GUICV.staticText.string_("vol:"));
		hl.add(GUICV.numberBox.action_({|v| efx.vol_(v.value)}));
		hl.add(GUICV.staticText.string_("lags:"));
		hl.add(GUICV.numberBox.action_({|v| efx.lags_(v.value)}).clipLo_(0).value_(efx.lags));
		hl.add(nil);
		hl.add(
			macroMenu= GUICV.popUpMenu
			.action_({|v| macros[v.value].value.value})
			.canFocus_(true)
			.items_(macros.collect{|assoc| assoc.key})
		);
		hl.add(
			GUICV.button
			.action_({macroMenu.doAction})
			.focus(true)  //initially give button focus as it is harmless
			.states_([["<"]])
		);
		header.children.do{|v| v.maxSize_(Size(skin.knobWidth, skin.buttonHeight))};
		macroMenu.maxWidth_(skin.knobWidth*2);
		header.layout.add(
			View().background_(skin.fontColor).fixedHeight_(1)  //thin separator
		);
		viewHeight= header.sizeHint.height+(skin.margin.y*2);

		//--scrolling canvas
		canvas= View().background_(skin.foreground).layout_(
			VLayout().margins_(skin.margin.asArray).spacing_(skin.spacing)
		)
		.keyDownAction_({true});  //block arrow keys scrolling

		efx.efxs.do{|x, i|
			var mod= GFX_ModuleGUI(x, version: version);
			var modHeight= mod.bounds.height;
			mod.fixedHeight_(modHeight);
			if(mod.sizeHint.width>viewWidth, {viewWidth= mod.sizeHint.width});
			if(i<numModulesBeforeScroll, {viewHeight= viewHeight+modHeight+skin.spacing});
			canvas.layout.add(mod);
		};

		//--window / view
		view= View(parent, bounds).layout_(
			VLayout(
				header,
				ScrollView().canvas_(canvas).hasBorder_(false).canFocus_(false)
			).margins_(skin.margin.asArray).spacing_(skin.spacing)
		)
		.name_(this.class.name++$_++efx.numChannels)
		.resizeTo(
			viewWidth+(skin.margin.x*2)+(skin.spacing*2),
			viewHeight+(skin.margin.y*2)
		);
	}
}
