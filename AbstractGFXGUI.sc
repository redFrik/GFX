//redFrik

//related: GFX_ModuleGUI, GFX_RackGUI

AbstractGFXGUI : SCViewHolder {

	var <efx;  //a GFX_Module or a GFX_Rack

	*new {|efx, position, version= 0, skin|
		^super.new.initAbstractGFXGUI(efx, position, version, skin ? GUI.skins.guiCV)
	}

	initAbstractGFXGUI {|argEfx, position, version, skin|
		efx= argEfx;
		position= position ?? {Point(300, 100)};
		this.prInit(version, skin);
		view.background_(skin.background);
		view.front.moveTo(position.x, position.y);
		CmdPeriod.doOnce({view.close});
	}
}
