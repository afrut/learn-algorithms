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

:: Compile into bytecode and put resulting class files into .\
:: Since these belong to the mylibs package, a .\mylibs\ directory will be created
javac src\main\java\mylibs\CountingTechniques.java -d .\
javac src\main\java\mylibs\Util.java -d .\
javac src\main\java\mylibs\Bag.java -d .\
javac src\main\java\mylibs\List.java -d .\
javac src\main\java\mylibs\Pair.java -d .\
javac src\main\java\mylibs\MaxPQ.java -d .\
javac src\main\java\mylibs\MinPQ.java -d .\
javac src\main\java\mylibs\IndexMaxPQ.java -d .\
javac src\main\java\mylibs\QuickFindUF.java -d .\
javac src\main\java\mylibs\QuickUnionUF.java -d .\
javac src\main\java\mylibs\WeightedQuickUnionUF.java -d .\
javac src\main\java\mylibs\QuickUnionPCUF.java -d .\
javac src\main\java\mylibs\Heapsort.java -d .\
javac src\main\java\mylibs\BinarySearch.java -d .\
javac src\main\java\mylibs\SymbolTable.java -d .\
javac src\main\java\mylibs\LinkedListSequentialSearchST.java -d .\
javac src\main\java\mylibs\SequentialSearchST.java -d .\
javac src\main\java\mylibs\BinarySearchST.java -d .\
javac src\main\java\mylibs\LinkedListOrderedSequentialSearchST.java -d .\
javac src\main\java\mylibs\RecursiveBST.java -d .\
javac src\main\java\mylibs\BST.java -d .\
javac src\main\java\mylibs\RedBlackBST.java -d .\
javac src\main\java\mylibs\STint.java -d .\
javac src\main\java\mylibs\STdouble.java -d .\
javac src\main\java\mylibs\SeparateChainingHashST.java -d .\
javac src\main\java\mylibs\LinearProbingHashST.java -d .\
javac src\main\java\mylibs\HashSTint.java -d .\
javac src\main\java\mylibs\HashSTdouble.java -d .\
javac src\main\java\mylibs\Set.java -d .\
javac src\main\java\mylibs\Setint.java -d .\
javac src\main\java\mylibs\Setdouble.java -d .\
javac src\main\java\mylibs\HashSet.java -d .\
javac src\main\java\mylibs\HashSetint.java -d .\
javac src\main\java\mylibs\HashSetdouble.java -d .\
javac src\main\java\mylibs\SequentialSearchSet.java -d .\
javac src\main\java\mylibs\BinarySearchSet.java -d .\
javac src\main\java\mylibs\Graph.java -d .\
javac src\main\java\mylibs\GraphSearchUF.java -d .\
javac src\main\java\mylibs\GraphSearchRecursiveDFS.java -d .\
javac src\main\java\mylibs\GraphPathsRecursiveDFS.java -d .\
javac src\main\java\mylibs\GraphPathsBFS.java -d .\
javac src\main\java\mylibs\GraphProperties.java -d .\
javac src\main\java\mylibs\GraphConnectDFS.java -d .\
javac src\main\java\mylibs\GraphConnectRecursiveDFS.java -d .\
javac src\main\java\mylibs\GraphCyclesRecursiveDFS.java -d .\
javac src\main\java\mylibs\GraphCyclesBFS.java -d .\
javac src\main\java\mylibs\GraphTwoColorRecursiveDFS.java -d .\
javac src\main\java\mylibs\SymbolGraph.java -d .\
javac src\main\java\mylibs\ProcessMovies.java -d .\

:: Create a jar in .\jar\ from bytecode .class files in .\mylibs
del .\jar\mylibs.jar
jar cvf .\jar\mylibs.jar .\mylibs\*.class

:::: Try running the main() function
::java -ea mylibs.CountingTechniques
::java -ea mylibs.Util .\src\main\resources\PQSampleInput.txt
::java -ea mylibs.Bag
::java -ea mylibs.List
::java -ea mylibs.Pair
::java -ea mylibs.MaxPQ .\src\main\resources\PQSampleInput.txt
::java -ea mylibs.MinPQ .\src\main\resources\PQSampleInput.txt
::java -ea mylibs.IndexMaxPQ .\src\main\resources\PQSampleInput.txt
::java -ea mylibs.QuickFindUF .\src\main\resources\UnionFindSampleInput.txt
::java -ea mylibs.QuickUnionUF .\src\main\resources\UnionFindSampleInput.txt
::java -ea mylibs.WeightedQuickUnionUF .\src\main\resources\UnionFindSampleInput.txt
::java -ea mylibs.QuickUnionPCUF .\src\main\resources\UnionFindSampleInput.txt
::java -ea mylibs.Heapsort
::java -ea mylibs.BinarySearch
::java -ea mylibs.LinkedListSequentialSearchST -test
::java -ea mylibs.LinkedListSequentialSearchST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.SequentialSearchST -test
::java -ea mylibs.SequentialSearchST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.BinarySearchST -test
::java -ea mylibs.BinarySearchST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.LinkedListOrderedSequentialSearchST -test
::java -ea mylibs.LinkedListOrderedSequentialSearchST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.RecursiveBST -test
::java -ea mylibs.RecursiveBST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.BST -test
::java -ea mylibs.BST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.RedBlackBST -test
::java -ea mylibs.RedBlackBST .\src\main\resources\STSampleInput.txt
::java -ea mylibs.STint -test
::java -ea mylibs.STint .\src\main\resources\8KIntStringPairs.txt
::java -ea mylibs.STdouble -test
::java -ea mylibs.STdouble .\src\main\resources\8KDoubleStringPairs.txt
::java -ea mylibs.SeparateChainingHashST -test
::java -ea mylibs.SeparateChainingHashST algs4-data\tale.txt
::java -ea mylibs.SeparateChainingHashST -resizeTest algs4-data\tale.txt
::java -ea mylibs.LinearProbingHashST algs4-data\tale.txt
::java -ea mylibs.HashSTint .\src\main\resources\8KIntStringPairs.txt
::java -ea mylibs.HashSTdouble .\src\main\resources\8KDoubleStringPairs.txt
::java -ea mylibs.Set -test
::java -ea mylibs.Set .\src\main\resources\STSampleInput.txt
::java -ea mylibs.Setint -test
::java -ea mylibs.Setint .\src\main\resources\8Kints.txt
::java -ea mylibs.Setdouble -test
::java -ea mylibs.Setdouble .\src\main\resources\8Kdoubles.txt
::java -ea mylibs.HashSet algs4-data\tale.txt
::java -ea mylibs.HashSetint src\main\resources\8Kints.txt
::java -ea mylibs.HashSetdouble src\main\resources\8Kdoubles.txt
::java -ea mylibs.SequentialSearchSet -test
::java -ea mylibs.SequentialSearchSet .\src\main\resources\STSampleInput.txt
::java -ea mylibs.BinarySearchSet -test
::java -ea mylibs.BinarySearchSet .\src\main\resources\STSampleInput.txt
::java -ea mylibs.Graph -test src\main\resources\myMediumG.txt " "
::java -ea mylibs.Graph src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphSearchUF src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphSearchRecursiveDFS src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphSearchRecursiveDFS src\main\resources\tinyGex2.txt " " -trace
::java -ea mylibs.GraphPathsRecursiveDFS src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphPathsRecursiveDFS src\main\resources\tinyGex2.txt " " -trace
::java -ea mylibs.GraphPathsBFS src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphPathsBFS src\main\resources\tinyGex2.txt " " -trace
::java -ea mylibs.GraphProperties src\main\resources\cycleG.txt " "
::java -ea mylibs.GraphProperties src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphProperties src\main\resources\tinyGex2.txt " "
::java -ea mylibs.GraphConnectDFS src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphConnectDFS src\main\resources\tinyGex2.txt " " -trace
::java -ea mylibs.GraphConnectRecursiveDFS src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphConnectRecursiveDFS src\main\resources\tinyGex2.txt " " -trace
::java -ea mylibs.GraphCyclesRecursiveDFS src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphCyclesRecursiveDFS src\main\resources\tinyGex2.txt " "
::java -ea mylibs.GraphCyclesBFS src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphCyclesBFS src\main\resources\tinyGex2.txt " "
::java -ea mylibs.GraphTwoColorRecursiveDFS src\main\resources\myMediumG.txt " "
::java -ea mylibs.GraphTwoColorRecursiveDFS src\main\resources\tinyGex2.txt " " -trace
::java -ea mylibs.SymbolGraph src\main\resources\moviesStringEdges.txt "/"
::java -ea mylibs.ProcessMovies algs4-data\movies.txt "/"

:: Clean
del /s .\mylibs\*.class
