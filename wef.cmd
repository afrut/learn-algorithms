cls

:: --------------------------------------------------
::  Testing and Experimentation
:: --------------------------------------------------
::cd templates
::javac HelloWorld.java
::java HelloWorld
::cd ..

::cd templates
::javac Basic.java
::java Basic
::cd ..

::cd templates
::javac IteratorTest.java
::java IteratorTest
::cd ..

::cd templates
::javac BinarySearch.java
::java BinarySearch 8KRangeOfInts.txt 4000 5000 6000 8000 0
::cd ..



:: --------------------------------------------------
::  Testing Textbook Code
:: --------------------------------------------------
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
::  Exercises
:: --------------------------------------------------
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

cd exercises\2.2.08
javac TopDownMergeSort.java
java TopDownMergeSort < input2.txt
cd ..\..