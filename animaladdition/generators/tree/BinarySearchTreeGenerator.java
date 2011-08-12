package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.properties.tree.PropertiesTreeModel;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.binarysearchtree.BinarySearchTreeAnimation;
import de.lere.vaad.utils.TextLoaderUtil;

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
        TextProperties TextProps = (TextProperties)props.getPropertiesByName("TextProperties");
        SourceCodeProperties sourceCode = (SourceCodeProperties)props.getPropertiesByName("SourceCodeProperties");
        GraphProperties graphProps = (GraphProperties)props.getPropertiesByName("GraphProperties");
        int[] insertionKnoten = (int[])primitives.get("InsertionKnoten");
        int[] initialBaum = (int[])primitives.get("InitialBaumViaInsertion");        
        
        BinaryTreeProperties btprops = new BinaryTreeProperties();
        btprops.setTextProperties(TextProps);
        btprops.setSourceCodeProperties(sourceCode);
        btprops.setGraphProperties(graphProps);
        btprops.authors = getAnimationAuthor();
        btprops.title = getAlgorithmName();
        
        BinarySearchTreeAnimation<Integer> treeAnimation = new BinarySearchTreeAnimation<Integer>(lang, btprops, toIntegerArray(initialBaum));
        treeAnimation.setInsertionAnimation(toIntegerArray(insertionKnoten));
        treeAnimation.setShowIntro(true);
        treeAnimation.buildAnimation();
        
        String string = lang.toString();
        try {
			FileUtils.writeStringToFile(new File("/tmp/generatedbst.asu"), string);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return string;
    }

    private Integer[] toIntegerArray(int[] knotenzahl) {
		Integer[] result = new Integer[knotenzahl.length];
		for (int i = 0; i < knotenzahl.length; i++) {
			result[i] = knotenzahl[i];
		}
		return result;
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
		String cleanString = PropertiesTreeModel.cleanString("generators/tree/"+BinarySearchTreeGenerator.class.getSimpleName()+".xml", "xml");
		ptm.loadFromXMLFile(cleanString, true);
		AnimationPropertiesContainer propertiesContainer = ptm.getPropertiesContainer();
	    Hashtable<String, Object> primitives = ptm.getPrimitivesContainer();
//	    primitives.put(arrayName, array);?	
		String generate = generator.generate(propertiesContainer, primitives);
		System.out.println(generate);
		
	}

}