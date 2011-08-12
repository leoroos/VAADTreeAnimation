# Der Binäre Suchbaum

Die Suchbaum Datenstruktur ermöglicht viele dynamische Operationen.
Unter anderem werden *Suche*, *Minimum*, *Maximum*, *Vorgänger* und
*Nachfolger*, *Einfügen* und *Löschen* unterstützt.\
So kann ein binärer Suchbaum als *Dictionary* und als *Prioritätswarteschlange*
verwendet werden.

Grundlegende Operationen auf einem binären Suchbaum benötigen Zeit proportional
zu seiner Höhe. Für einen vollständigen binären Suchbaum mit $n$ Knoten, benötigen
solche Operationen im schlechtesten Fall eine Laufzeit von $\Theta(lg~n)$.
Ist der Baum allerdings eine lineare Liste benötigen dieselben Operationen $\Theta(n)$
Zeit.\
Im Durchschnitt benötigen die Operation aber tatsächlich $\Theta(lg~n)$ Zeit.

Alle Beschreibungen und Codebeispiele orientieren sich an Cormen [@cormen09, pp 286-298].

## Animationen

Die Animation unterstützt drei Operationen:

* Suchen
* Einfügen
* Löschen

# Referenzen