package generators.splay;

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
import de.lere.vaad.animation.splaytree.SplayTreeAnimation;
import de.lere.vaad.utils.TextLoaderUtil;

public class SplayTreeGenerator implements Generator {
    private Language lang;
	private TextLoaderUtil textLoader;

    public SplayTreeGenerator() {
		this.textLoader = new TextLoaderUtil(getClass(), "resources");
	}
    
    @Override
	public void init(){
        lang = new AnimalScript("Der Splay-Baum", "Rene Hertling, Leo Roos", 1024, 600);
    }

    @Override
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        TextProperties TextProps = (TextProperties)props.getPropertiesByName("TextProperties");
        SourceCodeProperties sourceCode = (SourceCodeProperties)props.getPropertiesByName("SourceCodeProperties");
        GraphProperties graphProps = (GraphProperties)props.getPropertiesByName("GraphProperties");
        int[] searchNodes = (int[])primitives.get("SearchKnoten");
        int[] insertionNodes = (int[])primitives.get("InsertionKnoten");
        int[] deleteKnoten = (int[])primitives.get("DeleteKnoten");        
        int[] initialBaum = (int[])primitives.get("InitialBaumViaInsertion");     
        Boolean showIntro = (Boolean)primitives.get("ShowIntro");        
        BinaryTreeProperties btprops = new BinaryTreeProperties();
        btprops.setTextProperties(TextProps);
        btprops.setSourceCodeProperties(sourceCode);
        btprops.setGraphProperties(graphProps);
        btprops.authors = getAnimationAuthor();
        btprops.title = getAlgorithmName();
       
        SplayTreeAnimation<Integer> splayAnimation = new SplayTreeAnimation<Integer>(lang, btprops, toIntegerArray(initialBaum));
        splayAnimation.setSearchAnimation(toIntegerArray(searchNodes));
        splayAnimation.setInsertionAnimation(toIntegerArray(insertionNodes));
        splayAnimation.setDeleteAnimation(toIntegerArray(deleteKnoten));
        splayAnimation.setShowIntro(showIntro);
        splayAnimation.buildAnimation();
        
        return lang.toString();
    }

    private Integer[] toIntegerArray(int[] knotenzahl) {
		Integer[] result = new Integer[knotenzahl.length];
		for (int i = 0; i < knotenzahl.length; i++) {
			result[i] = knotenzahl[i];
		}
		return result;
	}

	@Override
	public String getName() {
        return "Splay-Baum";
    }

    @Override
	public String getAlgorithmName() {
        return "Splay-Baum";
    }

    @Override
	public String getAnimationAuthor() {
        return "Rene Hertling, Leo Roos";
    }

    @Override
	public String getDescription(){
        return textLoader.getText("generatorDescription.html");
    }

    @Override
	public String getCodeExample(){
        return textLoader.getText("codeBeispiele.html");
    }

    @Override
	public String getFileExtension(){
        return "asu";
    }

    @Override
	public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    @Override
	public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    @Override
	public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public static void main(String[] args) {
		SplayTreeGenerator generator = new SplayTreeGenerator();
		generator.init();
		PropertiesTreeModel ptm = new PropertiesTreeModel();
		String cleanString = PropertiesTreeModel.cleanString("generators/splay/"+SplayTreeGenerator.class.getSimpleName()+".xml", "xml");
		ptm.loadFromXMLFile(cleanString, true);
		AnimationPropertiesContainer propertiesContainer = ptm.getPropertiesContainer();
	    Hashtable<String, Object> primitives = ptm.getPrimitivesContainer();
		String generate = generator.generate(propertiesContainer, primitives);
		System.out.println(generate);
        try {
			FileUtils.writeStringToFile(new File("/tmp/splaytree.asu"), generate);
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}

}