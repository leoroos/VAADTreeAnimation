package de.lere.vaad;

import algoanim.primitives.Group;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

public class ZigMacroStep extends MacroStep {

	public ZigMacroStep(Language l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doPerform(SourceCode sc) {
		// TODO Auto-generated method stub
		
	}

//	public ZigMacroStep(Language l) {
//		super(l);		
//	}

//	@Override
//	protected void doPerform(SourceCode sc) {	
//		sc.highlight(0);
//		String macrosteptext1 = "Wenn p die Wurzel ist wird der Zig-Step ausgeführt.\n" + 
//				"Dieser Schritt wird nur ausgeführt wenn der Baum eine\n" + 
//				"ungerade Knotenanzahl hat und es sich um die letzte\n" + 
//				"Splay-Operation einer Transformation handelt. ";		
//		Group algoDescLine0 = GroupFactory.getTextGroupForResource(StringHelper.getStringToLinesAtDelimiter(macrosteptext1), getMacroStepLocation());		
//		getLanguage().nextStep();
//		String microstep1Text = "Führe Rechtsrotation um p aus.";		
//		Text microstep1 = getLanguage().newText(getMicroStepLocation(), microstep1Text, "microstep1", null);		
//		getLanguage().nextStep();
//	}
}
