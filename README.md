# ing-sw-2020-taverna-sorrentino-stagnoli

Software Engineering Project | A.A. 2019 - 2020 | GC6

## Functionalities

In our project we implemented:

- Complete Rules
- CLI
- GUI
- Socket
- 2 Advanced functionalities

The advanced functionalities we decided to implement allow the software to handle multiple games at the same times and you can play with 5 advanced gods. Theese new deities are Zeus, Era

## Test Coverage

| Package | Coverage |
| ------- | :------: |
| Model   |   100%   |

## Prerequisites

In order to use this piece of software it is required that you have **Maven** and **Java** installed. You can download both using the following links:

- Maven (3.6.3): https://maven.apache.org/download.cgi
- Java (JDK 14.0.1): https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html

To install Maven follow this [guide](https://maven.apache.org/install.html) on their site.

## How to use this software

### Generate the Jar

To run this piece of software you'll need to generate a .jar file. After you've cloned this repository make sure you are in the main directory called _"ing-sw-2020-taverna-sorrentino-stagnoli"_. Open your terminal in this position and type the following commands (it's the same for all operating system).

This is just a good practice to make sure it's a clean generation

```shell
mvn clean
```

This is the actual command that will generate the jar file

```shell
mvn package shade:shade
```

After the jar is created you'll need to change directory into the _"target"_ folder. Simply type

```shell
cd target
```

Now you should see something like this:
![target_folder]

### Run the server

### Run the client



[target_folder]: https://imgur.com/OA061wr "Target folder"

Le funzionalità sviluppate (cioè la riga corrispondenete al 30L)
La coverage dei test: tipo una tabella che dica:
model --> 100%
controller --> 100%
Prerequisiti per l'installazione (java 14 + maven)
Come generare i jar da maven
Come avviare client e server
