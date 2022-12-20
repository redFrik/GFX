//redFrik

//related: GFX_ModuleGUI, GFX_RackGUI

AbstractGFXGUI : SCViewHolder {

	var <efx;  //a GFX_Module or a GFX_Rack

	*new {|efx, position|
		^super.new.initAbstractGFXGUI(efx, position)
	}

	initAbstractGFXGUI {|argEfx, position|
		var skin= GUI.skins.guiCV;
		efx= argEfx;
		position= position ?? {Point(300, 100)};
		this.prInit(skin);
		view.background_(skin.background);
		view.front.moveTo(position.x, position.y);
		CmdPeriod.doOnce({view.close});
	}
}
