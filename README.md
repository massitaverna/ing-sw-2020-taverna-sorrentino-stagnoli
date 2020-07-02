# ing-sw-2020-taverna-sorrentino-stagnoli

Software Engineering Project | A.A. 2019 - 2020 | GC6

## Functionalities

In our project we implemented:

- Complete Rules
- CLI
- GUI
- Socket
- 2 Advanced functionalities: Multiple games and Advanced gods

The advanced deities are Hera, Hestia, Limus, Triton and Zeus.

## Test Coverage

| Package | Coverage |
| ------- | :------: |
| Model   |   100%   |

## Prerequisites

In order to use this piece of software it is required that you have **Maven** and **Java** installed. You can download both using the following links:

- Maven (3.6.3): https://maven.apache.org/download.cgi
- Java (JDK 14.0.1): https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html

To install Maven follow this [guide](https://maven.apache.org/install.html) on their site.

### If you are using Windows

Our CLI version of the game uses escape sequences and in order to work properly in the windows cmd you'll have to run this command inside your terminal:

```shell
reg add HKEY_CURRENT_USER\Console /v VirtualTerminalLevel /t REG_DWORD /d 0x00000001 /f
```

## How to use this software

### Generate the Jar

To run this piece of software you'll need to generate a .jar file. After you've cloned this repository make sure you are in the main directory called **_"ing-sw-2020-taverna-sorrentino-stagnoli"_**. Open your terminal in this position and type the following commands (it's the same for all operating system).

This is just a good practice to make sure it's a clean generation

```maven
mvn clean
```

This is the actual command that will generate the jar file

```maven
mvn package shade:shade
```

After the jar is created you'll need to change directory into the **_"target"_** folder. Simply type

```shell
cd target
```

Now you should see something like this:
![target_folder]

### Run the server

Now that you are in the **_"target"_** folder all you need to do to run the server is type this simple command in your terminal:

```shell
java -cp GC6-1.0-SNAPSHOT.jar it.polimi.ingsw.ServerMain
```

If you've renamed the jar file use the new name instead of **GC6-1.0-SNAPSHOT.jar**.

### Run the client

Now that you are in the **_"target"_** folder all you need to do to run the client is type this simple command in your terminal:

```shell
java -cp GC6-1.0-SNAPSHOT.jar it.polimi.ingsw.ClientMain
```

If you've renamed the jar file use the new name instead of **GC6-1.0-SNAPSHOT.jar**.

You'll be prompted to choose if you want to use a CLI or a GUI, make your choice and enjoy the game.


[target_folder]: https://i.imgur.com/OA061wr.png "Target folder"

