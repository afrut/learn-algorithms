:: --------------------------------------------------------------------------------
::
:: This script is used to compile and maintain libs.jar. libs.jar contains
:: implementations created for studying and exercises.
::
:: --------------------------------------------------------------------------------
cls

:: Compile
javac libs\combinatorics\CountingTechniques.java
javac libs\util\Util.java
javac libs\algs\Bag.java
javac libs\algs\List.java
javac libs\algs\Pair.java
javac libs\algs\pq\MaxPQ.java
javac libs\algs\pq\MinPQ.java
javac libs\algs\pq\IndexMaxPQ.java
javac libs\algs\Heapsort.java
javac libs\algs\BinarySearch.java
javac libs\algs\st\LinkedListSequentialSearchST.java
javac libs\algs\st\ResizingArraySequentialSearchST.java
javac libs\algs\st\ResizingArrayBinarySearchST.java
javac libs\algs\st\LinkedListOrderedSequentialSearchST.java
javac libs\algs\st\RecursiveBST.java
javac libs\algs\st\BST.java
javac libs\algs\st\RedBlackBST.java
javac libs\algs\st\SymbolTable.java
javac libs\algs\st\SeparateChainingHashST.java
::javac libs\algs\st\LinearProbingHashST.java

:: Archive
del .\jar\libs.jar
jar cvf ^
.\jar\libs.jar ^
.\libs\combinatorics\*.class ^
.\libs\util\*.class ^
.\libs\algs\pq\*.class ^
.\libs\algs\st\*.class ^
.\libs\algs\*.class

:: Test
java -ea libs.combinatorics.CountingTechniques
java -ea libs.util.Util txt\input.txt
java -ea libs.algs.Bag
java -ea libs.algs.List
java -ea libs.algs.Pair
java -ea libs.algs.pq.MaxPQ txt\input.txt
java -ea libs.algs.pq.MinPQ txt\input.txt
java -ea libs.algs.pq.IndexMaxPQ txt\input.txt
java -ea libs.algs.Heapsort
java -ea libs.algs.BinarySearch
java -ea libs.algs.st.LinkedListSequentialSearchST -test
java -ea libs.algs.st.LinkedListSequentialSearchST txt\STSampleinput.txt
java -ea libs.algs.st.ResizingArraySequentialSearchST -test
java -ea libs.algs.st.ResizingArraySequentialSearchST txt\STSampleinput.txt
java -ea libs.algs.st.ResizingArrayBinarySearchST -test
java -ea libs.algs.st.ResizingArrayBinarySearchST txt\STSampleinput.txt
java -ea libs.algs.st.LinkedListOrderedSequentialSearchST -test
java -ea libs.algs.st.LinkedListOrderedSequentialSearchST txt\STSampleinput.txt
java -ea libs.algs.st.RecursiveBST -test
java -ea libs.algs.st.RecursiveBST txt\STSampleinput.txt
java -ea libs.algs.st.BST -test
java -ea libs.algs.st.BST txt\STSampleinput.txt
java -ea libs.algs.st.RedBlackBST -test
java -ea libs.algs.st.RedBlackBST txt\STSampleinput.txt
java -ea libs.algs.st.SeparateChainingHashST -test
java -ea libs.algs.st.SeparateChainingHashST algs4-data\tale.txt
java -ea libs.algs.st.SeparateChainingHashST -resizeTest algs4-data\tale.txt
::java -ea libs.algs.st.LinearProbingHashST -test

:: Clean
del /s .\libs\*.class