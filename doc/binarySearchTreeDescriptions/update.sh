#!/bin/bash

target="../../src/de/lere/vaad/binarysearchtree/resources/generatorDescription.html"
codebeispiele="../../src/de/lere/vaad/binarysearchtree/resources/codeBeispiele.html"

cmd="pandoc -t html  -s --bibliography=biblio.bib"

${cmd} AlgorithmDescription.md > ${target} 
echo "written to ${target}"

${cmd} CodeBeispiele.md > ${codebeispiele} 
echo "written to ${codebeispiele}"