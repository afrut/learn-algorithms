cls

:::: --------------------------------------------------
::::  Testing and Experimentation
:::: --------------------------------------------------
::cd templates
::javac HelloWorld.java
::javac Basic.java
::javac IterableTest.java
::javac BasicRecursion.java
::javac -d . Package1Class.java
::javac -d ..\bin\ Package2Class.java
::javac -d . Package3Class.java
::jar cvf ..\bin\package3jar.jar .\package3\Package3Class.class
::rmdir /Q/S package3
::javac BasicReflection.java
::javac GenericArrayCreation.java
::javac GenericClass.java
::
::java HelloWorld
::java Basic
::java IterableTest
::java BasicRecursion
::java package1.Package1Class
::java package2.Package2Class
::java package3.Package3Class
::java BasicReflection
::java GenericArrayCreation
::java GenericClass
::
::del /s .\*.class
::cd ..


:::: --------------------------------------------------
::::  Testing Textbook Code
:::: --------------------------------------------------
::cd algs4
::javac BinarySearch.java
::java edu.princeton.cs.algs4.BinarySearch tinyW.txt < tinyT.txt
::cd ..

::cd algs4
::javac ThreeSum.java
::java edu.princeton.cs.algs4.ThreeSum 1Kints.txt
::java edu.princeton.cs.algs4.ThreeSum 2Kints.txt
::java edu.princeton.cs.algs4.ThreeSum 4Kints.txt
::java edu.princeton.cs.algs4.ThreeSum 8Kints.txt
::cd ..

::cd algs4
::javac DoublingTest.java
::java edu.princeton.cs.algs4.DoublingTest
::cd ..

::cd algs4
::javac DoublingRatio.java
::java edu.princeton.cs.algs4.DoublingRatio
::cd ..


:: --------------------------------------------------
::  Recompile Packages
:: --------------------------------------------------
:: Compile
javac libs\combinatorics\CountingTechniques.java
javac libs\util\Util.java
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
javac libs\algs\Bag.java
javac libs\algs\List.java
javac libs\algs\Pair.java
javac libs\algs\st\BST.java
javac libs\algs\st\RedBlackBST.java
javac libs\algs\st\SymbolTable.java
javac libs\algs\st\SeparateChainingHashST.java

:: Archive
del .\bin\libs.jar
jar cvf ^
.\bin\libs.jar ^
.\libs\combinatorics\*.class ^
.\libs\util\*.class ^
.\libs\algs\pq\*.class ^
.\libs\algs\st\*.class ^
.\libs\algs\*.class

:: Test
java -ea libs.combinatorics.CountingTechniques
java -ea libs.util.Util
java -ea libs.algs.pq.MaxPQ < libs\algs\input.txt
java -ea libs.algs.pq.MinPQ < libs\algs\input.txt
java -ea libs.algs.pq.IndexMaxPQ < libs\algs\input.txt
java -ea libs.algs.Heapsort
java -ea libs.algs.BinarySearch
java -ea libs.algs.st.LinkedListSequentialSearchST -test
java -ea libs.algs.st.LinkedListSequentialSearchST < libs\algs\STSampleinput.txt
java -ea libs.algs.st.ResizingArraySequentialSearchST -test
java -ea libs.algs.st.ResizingArraySequentialSearchST < libs\algs\STSampleinput.txt
java -ea libs.algs.st.ResizingArrayBinarySearchST -test
java -ea libs.algs.st.ResizingArrayBinarySearchST < libs\algs\STSampleinput.txt
java -ea libs.algs.st.LinkedListOrderedSequentialSearchST -test
java -ea libs.algs.st.LinkedListOrderedSequentialSearchST < libs\algs\STSampleinput.txt
java -ea libs.algs.st.RecursiveBST -test
java -ea libs.algs.st.RecursiveBST < libs\algs\STSampleinput.txt
java -ea libs.algs.Bag
java -ea libs.algs.List
java -ea libs.algs.Pair
java -ea libs.algs.st.BST -test
java -ea libs.algs.st.BST < libs\algs\STSampleinput.txt
java -ea libs.algs.st.RedBlackBST -test
java -ea libs.algs.st.RedBlackBST < libs\algs\STSampleinput.txt
java -ea libs.algs.st.SeparateChainingHashST -test

:: Clean
del /s .\libs\*.class



:::: --------------------------------------------------
::::  Exercises
:::: --------------------------------------------------
::cd exercises\1.3.32
::javac Steque.java
::java Steque
::cd ..\..

::cd exercises\1.3.32
::javac ResizingArraySteque.java
::java ResizingArraySteque
::cd ..\..

::cd exercises\1.3.33
::javac Deque.java
::java Deque
::cd ..\..

::cd exercises\1.3.33
::javac ResizingArrayDeque.java
::java ResizingArrayDeque
::cd ..\..

::cd exercises\1.3.37
::javac Josephus.java
::java Josephus 7 2
::java Josephus 10 3
::java Josephus 37 7
::cd ..\..

::cd exercises\1.4.02
::javac BigThreeSum.java
::java BigThreeSum BigInts.txt
::cd ..\..

::cd exercises\1.4.04
::javac TwoSum.java
::Java TwoSum ..\..\algs4\2Kints.txt
::cd ..\..

::cd exercises\1.4.08
::javac PairCounter.java
::Java PairCounter input.txt
::cd ..\..

::cd exercises\1.4.10
::javac BinarySearchSmallestIndex.java
::java BinarySearchSmallestIndex ..\..\templates\8KIntsWithEvenReps.txt 2 4 6 8 1998
::cd ..\..

::cd exercises\1.5.01
::javac QuickFindUF.java
::java QuickFindUF < input.txt
::cd ..\..

::cd exercises\1.5.02
::javac QuickUnionUF.java
::java QuickUnionUF < input.txt
::cd ..\..

::cd exercises\1.5.03
::javac WeightedQuickUnionUF.java
::java WeightedQuickUnionUF < input.txt
::cd ..\..

::cd exercises\1.5.12
::javac QuickUnionPCUF.java
::java QuickUnionPCUF < input.txt
::cd ..\..

::cd exercises\2.1.01
::javac SelectionSort.java
::java SelectionSort < input.txt
::java SelectionSort < input2.txt
::java SelectionSort < ..\2.1.03\input.txt
::cd ..\..

::cd exercises\2.1.04
::javac InsertionSort.java
::java InsertionSort < input.txt
::java InsertionSort < ..\2.1.05\input.txt
::cd ..\..

::cd exercises\2.1.09
::javac ShellSort.java
::java ShellSort < input.txt
::java ShellSort < input2.txt
::cd ..\..

::cd exercises\2.2.01
::javac Merge.java
::java Merge < input.txt
::cd ..\..

::cd exercises\2.2.02
::javac TopDownMergeSort.java
::java TopDownMergeSort < input.txt
::java TopDownMergeSort < input2.txt
::cd ..\..

::cd exercises\2.2.03
::javac BottomUpMergeSort.java
::java BottomUpMergeSort < input.txt
::java BottomUpMergeSort < input2.txt
::cd ..\..

::cd exercises\2.2.06
::javac MergeSortAAC.java
::java MergeSortAAC
::cd ..\..

::cd exercises\2.2.08
::javac TopDownMergeSort.java
::java TopDownMergeSort < input2.txt
::cd ..\..

::cd exercises\2.3.01
::javac Partition.java
::java Partition < input.txt
::java Partition < input2.txt
::cd ..\..

::cd exercises\2.3.02
::javac QuickSort.java
::java QuickSort < input.txt
::cd ..\..

::cd exercises\2.3.03
::javac QuickSortMEE.java
::java QuickSortMEE
::del QuickSortMEE.class
::cd ..\..

::cd exercises\2.3.05
::javac SortDistinct2.java
::java SortDistinct2 < input.txt
::del SortDistinct2.class
::cd ..\..

::cd exercises\2.3.06
::javac Cn.java
::java Cn
::del Cn.class
::cd ..\..

::cd exercises\2.4.01
::javac PQClient.java
::java PQClient < input.txt
::del PQClient.class
::cd ..\..

::cd exercises\2.4.01
::javac PQClient.java
::java PQClient < input.txt
::del PQClient.class
::cd ..\..

::cd exercises\2.4.03
::javac MaxPQUA.java
::java MaxPQUA < input.txt
::del MaxPQUA.class
::javac MaxPQOA.java
::java MaxPQOA < input.txt
::del MaxPQOA.class
::javac MaxLLUO.java
::java MaxLLUO < input.txt
::del MaxLLUO.class
::del MaxLLUO$1.class
::del MaxLLUO$Node.class
::javac MaxLL.java
::java MaxLL < input.txt
::del MaxLL.class
::del MaxLL$1.class
::del MaxLL$Node.class
::cd ..\..

::cd exercises\2.4.05
::javac MaxPQClient.java
::java MaxPQClient < input.txt
::del MaxPQClient.class
::cd ..\..

::cd exercises\2.4.06
::javac MaxPQClient.java
::java MaxPQClient < input.txt
::del MaxPQClient.class
::cd ..\..

::cd exercises\2.4.07
::javac MaxPQClient.java
::java MaxPQClient input.txt
::del MaxPQClient.class
::cd ..\..

::cd exercises\2.5.01
::javac Main.java
::java Main
::del Main.class
::cd ..\..

::cd exercises\2.5.02
::javac CompoundWords.java
::java CompoundWords < input.txt
::del CompoundWords.class
::cd ..\..

::cd exercises\2.5.04
::javac Main.java
::java Main < ..\2.5.02\input.txt
::del Main.class
::cd ..\..

::cd exercises\3.1.01
::javac LetterGrades.java
::java LetterGrades < input.txt
::del LetterGrades.class
::cd ..\..

::cd exercises\3.1.04
::javac Time.java
::javac Event.java
::javac Client.java
::java Time
::java Event
::java Client
::del *.class
::cd ..\..
