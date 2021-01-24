:: --------------------------------------------------------------------------------
::
:: This script demonstrates how to use the command-line interface cmd to compile
:: and run Java programs quickly without the overhead of higher level concepts
:: like projects in Eclipse.
::
:: Ensure that CLASSPATH environment variable on windows includes the current
:: directory .\
::
:: --------------------------------------------------------------------------------

cd src\main\java\templates

:: Compile and run a hello world program
javac HelloWorld.java
java HelloWorld

:: Illustrates looping, conditionals, arrays and functions
javac Basic.java
java Basic

:: Shows implementation of the Iterable and Iterator interface
javac IterableTest.java
java IterableTest

:: Demonstrates basic recursion in Java
javac BasicRecursion.java
java BasicRecursion

:: Compile and invoke a program within a package in the current directory
:: NOTE: The package1 directory is created in the current directory
javac -d . Package1Class.java
java package1.Package1Class

:: Compile into a different directory and invoke the program in a package located
:: in another directory using the CLASSPATH environment variabl.e
:: NOTE: CLASSPATH must include the absolute path of the ..\bin\ directory!
:: NOTE: The package2 directory is created in ..\bin\
javac -d ..\..\..\..\bin\ Package2Class.java
java package2.Package2Class

:: Compile and add to a jar. Delete the package3 directory and invoke the
:: program through the jar.
:: NOTE: The aboslute path of ..\jar\package3jar.jar must be included in CLASSPATH.
javac -d . Package3Class.java
jar cvf ..\..\..\..\jar\package3jar.jar .\package3\Package3Class.class
rmdir /Q/S package3
java package3.Package3Class

:: Demonstrates the use of Reflection in Java
javac BasicReflection.java
java BasicReflection

:: Shows how to create an array with a type that uses generics
javac GenericArrayCreation.java
java GenericArrayCreation

:: Shows a simple class with generic types
javac GenericClass.java
java GenericClass

:: Clean all files and return to the directory above
del /s *.class
cd ..\..\..\..