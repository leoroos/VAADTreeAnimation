# Binary Tree Animation Framework für Animal

Dieses Repository beinhaltet ein Framework um Binary Tree Animationen mit
Guido Rösslings [Animal Framework][animal1] einfacher animieren zu können.

## Allgemeine Strukur 

Eine kurze Beschreibung wo sich was findet.

### Schnell mal starten
Um Animal einfach mal mit den Generatoren zu starten kann die
vorgefertigte launch configuration verwendet werden:

     StartAnimalWithTreeAnimations.launch

### Die Quellcode Ordner 

Die Sourcen sind nach Inhalt in verschiedenen Ordner enthalten.
  
   - **treebuilder** Im *treebuilder* befindet sich das grundlegende model. Es beinhaltet die Binary Tree Logik und das Event handling.
   - **junit** Die Tests, die den größten Teil des tree modells abdecken, jedoch exklusiv der meisten event logik,
   stecken im *junit* folder.
   - **src** Die Animal-spezifischen die Animationen basierend auf dem Binary Tree Modell sind im *src* folder
   - **animaladdition** beinhaltet die Generatoren für die Binary-Tree und die Splay-Tree-Animation mit den dazugehörigen xmls und einer konfigurierten DummyGenerator Klasse.

Der Quellcode ohne die Tests hat Abhängigkeiten zu

   - Animal-current.jar
   - annotations.jar, jsr305.jar (Findbugs annotationen Eweiterung, müssen lediglich zur compile Zeit vorhanden sein)
   - commons-io (2.0.1)
   - commons-lang (2.6)

Die Testklassen haben zusätzlich Abhängigkeiten zu

   - hamcrest (1.3RC2)
   - mockito (1.8.5)


[animal1]: http://www.algoanim.info/Animal/download.php3?lang=en

### Etwas Dokumentation

Im `doc` Ordner sind teilweise die Beschreibungen der Algorithmen enthalten, diese sind inkonsistent auch noch über 
die `resources` packages der Animationen und den `animaladditions` Ordner verteilt.