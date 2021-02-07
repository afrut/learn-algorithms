:: --------------------------------------------------------------------------------
::
:: This script is used to compile and maintain libs.jar. libs.jar contains
:: implementations created for studying and exercises.
::
:: It also serves as the quickest path to running the programs. All that is
:: expected to run these is to have:
:: - a java sdk
:: - include the path of .\jar\mylibs.jar in CLASSPATH environment variable
:: - include the javac and java programs in the PATH variable
::
:: There are other viable IDE/project-based solutions for managing, building, and
:: running source code but these usually require the user to learn the paradigm
:: of a specific IDE. In contrast, this simple cmd script can be run with
:: minimal overhead. This allows the user to focus on the code that expresses
:: algorithms and data structures versus setting up a project in an IDE.
::
:: --------------------------------------------------------------------------------
cls

:::: Compile into bytecode and put resulting class files into .\
:::: Since these belong to the mylibs package, a .\mylibs\ directory will be created
::javac src\main\java\mylibs\CountingTechniques.java -d .\
::javac src\main\java\mylibs\Util.java -d .\
::javac src\main\java\mylibs\Bag.java -d .\
::javac src\main\java\mylibs\List.java -d .\
::javac src\main\java\mylibs\Pair.java -d .\
::javac src\main\java\mylibs\MaxPQ.java -d .\
::javac src\main\java\mylibs\MinPQ.java -d .\
::javac src\main\java\mylibs\IndexMaxPQ.java -d .\
::javac src\main\java\mylibs\Heapsort.java -d .\
::javac src\main\java\mylibs\BinarySearch.java -d .\
::javac src\main\java\mylibs\SymbolTable.java -d .\
::javac src\main\java\mylibs\LinkedListSequentialSearchST.java -d .\
::javac src\main\java\mylibs\ResizingArraySequentialSearchST.java -d .\
::javac src\main\java\mylibs\ResizingArrayBinarySearchST.java -d .\
::javac src\main\java\mylibs\LinkedListOrderedSequentialSearchST.java -d .\
::javac src\main\java\mylibs\RecursiveBST.java -d .\
::javac src\main\java\mylibs\BST.java -d .\
::javac src\main\java\mylibs\RedBlackBST.java -d .\
::javac src\main\java\mylibs\SeparateChainingHashST.java -d .\
::javac src\main\java\mylibs\LinearProbingHashST.java -d .\
javac src\main\java\mylibs\Graph.java -d .\
::
:::: Create a jar in .\jar\ from bytecode .class files in .\mylibs
::del .\jar\mylibs.jar
::jar cvf .\jar\mylibs.jar .\mylibs\*.class
::
:::: Try running the main() function
::java -ea mylibs.CountingTechniques
::java -ea mylibs.Util .\src\main\resources\PQSampleInput.txt
::java -ea mylibs.Bag
::java -ea mylibs.List
::java -ea mylibs.Pair
::java -ea mylibs.MaxPQ .\src\main\resources\PQSampleInput.txt
::java -ea mylibs.MinPQ .\src\main\resources\PQSampleInput.txt
::java -ea mylibs.IndexMaxPQ .\src\main\resources\PQSampleInput.txt
::java -ea mylibs.Heapsort
::java -ea mylibs.BinarySearch
::java -ea mylibs.LinkedListSequentialSearchST -test
::java -ea mylibs.LinkedListSequentialSearchST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.ResizingArraySequentialSearchST -test
::java -ea mylibs.ResizingArraySequentialSearchST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.ResizingArrayBinarySearchST -test
::java -ea mylibs.ResizingArrayBinarySearchST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.LinkedListOrderedSequentialSearchST -test
::java -ea mylibs.LinkedListOrderedSequentialSearchST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.RecursiveBST -test
::java -ea mylibs.RecursiveBST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.BST -test
::java -ea mylibs.BST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.RedBlackBST -test
::java -ea mylibs.RedBlackBST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.SeparateChainingHashST -test
::java -ea mylibs.SeparateChainingHashST algs4-data\tale.txt
::java -ea mylibs.SeparateChainingHashST -resizeTest algs4-data\tale.txt
::java -ea mylibs.LinearProbingHashST algs4-data\tale.txt
java -ea mylibs.Graph algs4-data\mediumG.txt
java -ea mylibs.Graph -test algs4-data\mediumG.txt

:: Clean
del /s .\mylibs\*.class