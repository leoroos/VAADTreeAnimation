package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;

import java.util.Vector;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new EffectiveBinaryTree());
    generators.add(new HeapTree());
    generators.add(new EffectiveAVLTree());
    generators.add(new TournamentSort());
    generators.add(new IgnallSchrage());
    generators.add(new BinarySearchTreeGenerator());
    return generators;
  }
}
