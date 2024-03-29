a [Quark](https://supercollider-quarks.github.io/quarks/) for [SuperCollider](https://supercollider.github.io)

# GFX

Green effects module system with optional GUI. Modules automatically adapt to any number of channels.

Based on the older redModule in the [redSys](https://github.com/redFrik/redSys) quark.

Provides SynthDef+Synth construction, pseudo UGen support, code<->GUI linkage and automatic GUI generation with minimal code (see Modules folder - basically just an `*ar` method and some specs).

### Basic example:

```supercollider
s.boot;
a= {SinOsc.ar([400, 404], 0, 0.25)}.play;

b= GFXPch2();
b.pch2Mix= 0.5;
b.pch2Ratio= 2;

c= b.gui;  //optional GUI. Notice the code<->gui linkage

r= GFX_Rack();
t= r.gui;

c.close; b.free; a.free; r.free; t.close;
```

## Requirements

[SuperCollider](https://supercollider.github.io) version 3.9 or newer running under macOS, Linux or Windows.

## Dependencies

* **GUICV** - quark https://github.com/redFrik/GUICV  
Automatically installed when running the `Quarks.install` here below.

## Installation

```supercollider
//install
Quarks.install("https://github.com/redFrik/GFX")
//recompile
"Overviews/GFX_Overview".help
```

## Optional GUI

A GUI is provided. For effect modules it is generated dynamically, and the effects rack is stacking modules in a scrolling window.

There are a couple of different versions, and one can also set the look and feel by editing the skin (`GUI.skins.guiCV` provided by [GUICV](https://github.com/redFrik/GUICV))

![GFX_RackGUI version 0 screenshot](HelpSource/Images/GFX_RackGUI_v0.png)
![GFX_RackGUI version 1 screenshot](HelpSource/Images/GFX_RackGUI_v1.png)

![GFXPch2GUI version 0 screenshot](HelpSource/Images/GFXPch2GUI_v0.png)
![GFXPch2GUI version 1 screenshot](HelpSource/Images/GFXPch2GUI_v1.png)

![GFXTankGUI version 0 screenshot](HelpSource/Images/GFXTankGUI_v0.png)
![GFXTankGUI version 1 screenshot](HelpSource/Images/GFXTankGUI_v1.png)
