#Abschlussbericht Draft

### was Dir am Praktikum gut oder weniger gut gefallen hat;

Sehr gut hat mir die Betreuung gefallen, wie nicht anders bei einer Veranstaltung bei Dr. Rössling 
zu erwarten ist. Im Forum wurde soweit ich es verfolgt habe schnell und kompetent geantwortet.

Obwohl wir mit unserer Abgabe etwas an dem typischen Szenario vorbeigeschlittert sind, war dies 
dann doch kein großes Problem bei der Bewertung. 


Negativ aufgefallen ist hauptsächlich die fehlerverursachende und umständliche Verwendung der API.

Mehrfach sind wir in Situationen gekommen bei denen wir auf Fehler oder unerwartetes Verhalten
von Seiten des Frameworks gestoßen sind für die wir aufwendige Workarounds schaffen mussten.

So führt zum Beispiel das Erzeugen eines Text Objektes das auf einem Offset basiert, der wiederum eine Coordinate als Ursprung hat
immer zu einem NullPointer. (Siehe dazu `de.lere.vaad.utils.CorrectedOffsetTest#testOffsetOfCoordinateFails()`)
Das ist in so fern ärgerlich da es eine einheitliche Benutzung über Node erschwert.

Für die fließenden Animation des Graphen ist es nicht möglich die Standard API zu benutzen (also `translateNodes`).
Möchte man zum Beispiel einen Knoten von einer Position zur anderen bewegen ist dies lediglich 
mit der `moveVia` Methode zufriedenstellend möglich, für die man für jede Bewegung eine eigene 
PolyLine erzeugen muss entlang der dann ein Knoten zu bewegen ist.
Ebenso ist hinzufügen und entfernen von Knoten ledliglich durch neuerzeugen von Graphen möglich.   
  
### Was Du gelernt hast
  - Nach langer Zeit mal wieder ein bisschen mit Baumalgorithmen auseinandergesetzt und 
    festgestellt, dass es selbst bei so einer einfach anmutentenden Struktur wie der des Binärbaums,
    die verschiedenen Algorithmen in Betrachtung aller Sonderfälle garnicht so simple sind
    wie sie auf den ersten Blick erscheinen. Zumindest beim Löschalgorithmus war ich erleichtert
    den Cormen zum Nachschlagen zu haben.
    
  - Effektiv mit einem Java Decompiler den Animal Code nachzuvollziehen.
    [JadClipse][jadclipse] basierend auf [Jad][jad] hat sich als sehr nützlich erwiesen.
    Besonders mit der Option *Align Code for debugging*.
 

### was in Zukunft an Animal, AnimalScript, AlgoAnim-API oder den Übungen geändert oder verbessert werden sollte
  - Eine gut getestete API wäre schön
  - Das AnimalScript Plug-in für Eclipse führt häufiger mal zum Livelock. Ich konnte teilweise nur 10 Minuten arbeiten
    bevor es wieder abgestürzt ist. Irgendwann habe ich glaube ich festgestellt dass es mit dem Code Completion zusammenhing.
    Das ist jetzt aber 3 Monate her und ich bin mir nicht mehr ganz sicher. Beim debuggen hat sich herausgestellt das sich  
    ein synchronized aufgerufenes Objekt über einige weitere Objekte wieder selber aufrufen wollte aber nicht konnte, 
    weil es auf sich selber gelockt war.     
  - Es wäre hilfreich den Source komplett mit zur Verfügung gestellt zu bekommen damit leichter nachvollziehen kann was
    eine Methode wirklich tut, da dies nicht immer aus der Benennung und dem Javadoc hervorgeht.
  
    
### wie zufrieden Du mit der Betreuung hier im Lernportal Informatik bist
    - Moodle gefällt mir gut, besser als webreg oder tucan.
    - Mit der Betreuung bin ich wie oben erwähnt zufrieden. 


[jad]: http://www.varaneckas.com/jad 
[jadclipse]: http://sourceforge.net/projects/jadclipse/