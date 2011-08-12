package de.lere.vaad.binarysearchtree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.properties.tree.PropertiesTreeModel;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.utils.TextLoaderUtil;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public class BinarySearchTreeGenerator implements Generator {
    private Language lang;
	private TextLoaderUtil textLoader;

    public BinarySearchTreeGenerator() {
		this.textLoader = new TextLoaderUtil(getClass(), "resources");
	}
    
    public void init(){
        lang = new AnimalScript("Aufgabe 4 Generator", "Rene Hertling, Leo Roos", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        TextProperties TextProps = (TextProperties)props.getPropertiesByName("TextProps");
        SourceCodeProperties sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        GraphProperties graphProps = (GraphProperties)props.getPropertiesByName("graphProperties");
        int[] knotenzahl = (int[])primitives.get("Knotenzahl");
        
        
        BinaryTreeProperties btprops = new BinaryTreeProperties();
        btprops.textProperties = TextProps;
        btprops.sourceCodeProperties = sourceCode;
        btprops.graphProperties = graphProps;
        btprops.authors = getAnimationAuthor();
        btprops.title = getAlgorithmName();
        
        new BinarySearchTreeAnimation(lang, btprops);
        
        return lang.toString();
    }

    public String getName() {
        return "Animation eines binären Suchbaumes";
    }

    public String getAlgorithmName() {
        return "Binary Search Tree";
    }

    public String getAnimationAuthor() {
        return "Rene Hertling, Leo Roos";
    }

    public String getDescription(){
        return textLoader.getText("generatorDescription.html");
    }

    public String getCodeExample(){
        return textLoader.getText("codeBeispiele.html");
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public static void main(String[] args) {
		BinarySearchTreeGenerator generator = new BinarySearchTreeGenerator();
		generator.init();
		PropertiesTreeModel ptm = new PropertiesTreeModel();
		String cleanString = PropertiesTreeModel.cleanString("de/lere/vaad/binarysearchtree/resources/binarySearchTreeProperties.xml", "xml");
		ptm.loadFromXMLFile(cleanString, true);
		AnimationPropertiesContainer propertiesContainer = ptm.getPropertiesContainer();
	    Hashtable<String, Object> primitives = ptm.getPrimitivesContainer();
//	    primitives.put(arrayName, array);?
		String generate = generator.generate(propertiesContainer, primitives);
		System.out.println(generate);
		
	}

}