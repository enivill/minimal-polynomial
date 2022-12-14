

# [ENG]
JavaFX application 

**Didactic application to determine the minimal polynomial of a matrix over R, C, Zp.**

*The goal of this project is the creation of a didactic application, which displays 
the calculation of the minimal polynomial of a matrix simply and comprehensible. 
The application enables the user to choose from three grades of a matrix and from four number fields. 
The grades of the matrix are 3, 4, 5 and the number fields are R, C, Z2 and Z3.*

The implementation of the application is realised through the form of a **desktop app**.
For the implementation we have used the programming language **Java**, the platform **JavaFX**, 
and the visual layout tool **Scene Builder** in the IntelliJ IDEA environment.

## Before running the jar file, make sure of having the above requirements and settings:
	1. You must to have installed the Java JDK, version 11 and above
	2. You must to set JAVA_HOME as environment variable, which should point to the JDK directory
	3. Update PATH system variable - add a new environment variable '  %JAVA_HOME%\bin '


To run the project from the command line, go to the dist folder and
type the following:

`java -jar "minimalny_polynom.jar"` 

Another way to run the project is by clicking on the file `start.bat`,
but it's crucial to have the files *minimalny_polynom.jar* and *start.bat* at the same directory.

The *src* folder contains the source code of the app.
The application was built using **Maven**. The `pom.xml` file is the core of the Maven project's configuration.

# [SK]
JavaFX aplikácia 

**Didaktická aplikácia na určenie minimálneho polynómu matice nad poľami R, C, Zp.**

*Cieľom práce je vytvorenie didaktickej aplikácie, ktorá jednoducho a zrozumiteľne zobrazí výpočet minimálneho polynómu matice. 
Aplikácia umožní používateľovi vybrať si z troch stupňov matíc a štyroch číselných polí. Stupne matíc sú 3, 4, 5 a číselné polia sú R, C, Z2 a Z3.*

Implementácia aplikácie je realizovaná formou **desktopovej aplikácie**. Pri implementácii sme využívali programovací jazyk **Java**, 
platformu **JavaFX** a vizuálny nástroj **Scene Builder** v prostredí IntelliJ IDEA.

## Pre správny chod aplikácie treba:
	1. doinštalovať Java JDK (verzia 11 a vyššie)
	2. nastaviť systémovú premennú JAVA_HOME aby odkazovala na JDK adresár 
	3. k premennej PATH pridať cestu k JDK/bin súboru : '  %JAVA_HOME%\bin '

Aplikáciu spustíme cez príkazový riadok s príkazom `java -jar minimalny_polynom.jar`.

Iný spôsob spustenia aplikácie je kliknutím na súbor `start.bat`, 
v tomto prípade je dôležitá aby súbory *minimal_polynomial.jar* a *start.bat* boli v jednom adresári.

Priečinok *src* obsahuje zdrojový kód aplikáce.
Aplikácia bola vyvíjaná pomocou **Maven**. Súbor `pom.xml` obsahuje informácie o projekte a informácie o konfigurácii pre mavena na zostavenie projektu.

vyvýjala: Enikő Villantová

## Preview / Ukážka
**Front page**

![front_page](https://user-images.githubusercontent.com/57987866/189872330-2608877c-43d2-47db-9a00-6cf30afd56dc.png)

**Calculation setup**

![setup](https://user-images.githubusercontent.com/57987866/189872355-aacef3db-bff1-44ad-b119-142708889442.png)

**Calculation**

![calculation](https://user-images.githubusercontent.com/57987866/189872384-50533c6c-cab2-4b1e-a4f2-a74a255dcfac.png)

**Interactive elements**

![calculation2](https://user-images.githubusercontent.com/57987866/189872406-f8f3734d-bc55-4c01-8697-82fefc3fbb98.png)
![calculation3](https://user-images.githubusercontent.com/57987866/189872429-ad915ac8-b01c-47ae-b577-7f58e4b85f1c.png)

**Result**

![result](https://user-images.githubusercontent.com/57987866/189872452-eccf4ad2-50f9-45c1-9195-53d1fdc59073.png)

