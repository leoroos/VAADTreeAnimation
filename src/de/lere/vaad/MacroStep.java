package de.lere.vaad;

import java.util.LinkedList;
import java.util.List;

import generators.helpers.OffsetCoords;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;
import algoanim.util.Offset;

public abstract class MacroStep {

	private Language l;
	
	private OffsetCoords macroStepLocation;

	private OffsetCoords microStepLocation;

	public MacroStep(Language l){
		this.l = l;
	}
	
	public void performStep(Node anchor, SourceCode sc){
		this.macroStepLocation = new OffsetCoords(anchor, 0, 60+10);
		microStepLocation = new OffsetCoords(macroStepLocation, 0, 100);		
		doPerform(sc);
	}	
	
	protected abstract void doPerform(SourceCode sc);

	protected OffsetCoords getMacroStepLocation() {
		return macroStepLocation;
	}
	
	protected OffsetCoords getMicroStepLocation() {
		return microStepLocation;
	}	
	
	protected Language getLanguage() {
		return l;
	}
}
