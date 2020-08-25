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

::cd templates
::javac BasicRecursion.java
::java BasicRecursion
::cd ..

:: Test creating a package in the same directory and running it from there
::cd templates
::javac -d . Package1Class.java
::java package1.Package1Class
::cd ..

:: Test creating a package in a directory in CLASSPATH and run it from there
::cd templates
::javac -d ..\bin\ Package2Class.java
::java package2.Package2Class
::cd ..

:::: Test creating a package as a jar file in CLASSPATH
::cd templates
::javac -d . Package3Class.java
::jar cvf ..\bin\package3jar.jar .\package3\Package3Class.class
::rmdir /Q/S package3
::cd ..
::java package3.Package3Class


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
::  Recompile Packages
:: --------------------------------------------------
::javac mylibs\combinatorics\CountingTechniques.java
::javac mylibs\util\Util.java
::javac mylibs\ds\MaxPQ.java
::javac mylibs\ds\MinPQ.java
::javac mylibs\ds\IndexMaxPQ.java
::jar cvf .\bin\mylibs.jar .\mylibs\combinatorics\CountingTechniques.class .\mylibs\util\Util.class .\mylibs\ds\MaxPQ.class .\mylibs\ds\MinPQ.class .\mylibs\ds\IndexMaxPQ.class
::del .\mylibs\combinatorics\CountingTechniques.class
::del .\mylibs\util\Util.class
::del .\mylibs\ds\MaxPQ.class
::del .\mylibs\ds\MinPQ.class
::del .\mylibs\ds\IndexMaxPQ.class
::java -ea mylibs.combinatorics.CountingTechniques
::java -ea mylibs.util.Util
::java -ea mylibs.ds.MaxPQ
::java -ea mylibs.ds.MinPQ
::java -ea mylibs.ds.IndexMaxPQ



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

cd exercises\2.4.05
javac MaxPQClient.java
java MaxPQClient < input.txt
del MaxPQClient.class
cd ..\..