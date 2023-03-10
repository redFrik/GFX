//redFrik

//related: GFX_ModuleGUI, GFX_RackGUI

AbstractGFXGUI : SCViewHolder {

	var <efx;  //a GFX_Module or a GFX_Rack

	*new {|efx, parent, bounds, version= 0|
		if(efx.isKindOf(AbstractGFX).not, {
			"efx argument % not a proper module or rack".format(efx).warn;
			^nil
		});
		^super.new.initAbstractGFXGUI(efx, parent, bounds, version)
	}

	initAbstractGFXGUI {|argEfx, parent, bounds, version|
		var skin= GUI.skins.guiCV;
		efx= argEfx;
		this.prInit(parent, bounds, version, skin);
		view.background_(skin.background);
		view.front;
		CmdPeriod.doOnce({view.close});
	}

	//--private

	prInit {^this.subclassResponsibility(thisMethod)}
}
