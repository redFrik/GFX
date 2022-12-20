//redFrik

//related: GFX_ModuleGUI, GFX_RackGUI

AbstractGFXGUI : SCViewHolder {

	var <efx;  //a GFX_Module or a GFX_Rack

	*new {|efx, position, version= 0|
		^super.new.initAbstractGFXGUI(efx, position, version)
	}

	initAbstractGFXGUI {|argEfx, position, version|
		var skin= GUI.skins.guiCV;
		efx= argEfx;
		position= position ?? {Point(300, 100)};
		this.prInit(skin, version);
		view.background_(skin.background);
		view.front.moveTo(position.x, position.y);
		CmdPeriod.doOnce({view.close});
	}
}
