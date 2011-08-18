#!/bin/bash

#Erzeugt die Algorithmus-Beschreibung und die Code-Beispiele

mdDescription="AlgorithmDescription.md"
mdCode="CodeBeispiele.md"

pathToRoot="../.."
relPath="animaladdition/generators/tree/resources"
pathToTargetFolder="${pathToRoot}/${relPath}"

target="${pathToTargetFolder}/generatorDescription.html"
codebeispiele="${pathToTargetFolder}/codeBeispiele.html"

cmd="pandoc -t html  -s --bibliography=biblio.bib"

#${cmd} AlgorithmDescription.md > ${target} 
#echo "written to ${target}"

#Da animal keine einzelnen abgeschlossenen html Dateien unterscheiden kann hier alles in einer ... 
${cmd} ${mdDescription} ${mdCode} > ${target}
echo "written to ${target}"

#Und damit trotzdem über getCodeExample was zurückkommt nochmal separat
${cmd} ${mdCode} > ${codebeispiele} 
echo "written to ${codebeispiele}"