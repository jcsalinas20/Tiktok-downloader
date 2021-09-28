# Tiktok-downloader

<br />

# Index
 * [Windows 10 Installation](#windows-10-installation)
 * [Ubuntu 20.04 Installation](#ubuntu-2004-installation)
 * [Run Program](#run-program)
    * [Terminal](#user-content-in-terminal-cmd-powershell-etc)
    * [File System](#user-content-in-file-system)
 * [Problems](#problems)
    * [Linux](#user-content-linux)
    * [Windows](#user-content-windows)
 * [Tested Versions](#tested-versions)
    * [Java](#user-content-java-versions)
    * [Node](#user-content-node-versions)
    * [Tiktok-scraper](#user-content-tiktok-scraper-versions)

<br />

# Windows 10 Installation

## 1. Install Java JDK 11

First check if you have Java with the version 11

```
java -version
```

If you don't have Java download and install 

* [Download](https://drive.google.com/file/d/18F7-0Sepzd9houMPDsEyXNU61jcGxY9Y/view?usp=sharing) Java JDK 11

If you have Java 8 or other version you need to change the version to Java 11

* First [Download](https://drive.google.com/file/d/18F7-0Sepzd9houMPDsEyXNU61jcGxY9Y/view?usp=sharing) Java JDK 11 and install
* After installation if the version does not change you must edit the [environment variables](https://superuser.com/questions/1057532/how-to-change-java-version-on-windows-10)

## 2. Install Node.js

* Go to the [website](https://nodejs.org/es/), select LTS Version and install

## 3. Install tiktok-scraper

``` 
npm i -g tiktok-scraper
```

## 4. Download Windows .jar and [run program](#run-program)

* [Download](https://github.com/jcsalinas20/Tiktok_downloader/releases) the last .jar for Windows

<br />

# Ubuntu 20.04 Installation

## 1. Install Java JDK 11

```
sudo apt install openjdk-11-jre 
```

## 2. Install Node and Npm

``` 
sudo apt install nodejs npm
```

## 3. Install tiktok-scraper

``` 
sudo npm i -g tiktok-scraper
```

## 4. Download Linux .jar and [run program](#run-program)

* [Download](https://github.com/jcsalinas20/Tiktok_downloader/releases) the last .jar for Linux


<br />

# Run Program

## In terminal, cmd, PowerShell, etc.

```
java -jar Tiktok-downloader.jar
```

## In File System

### Windows: With double click

### Linux/Ubuntu

First right click in the .jar and select Properties

![prop1](http://imgfz.com/i/ZOBTGjJ.png)

Then in Permissions select the checkbox

![prop2](http://imgfz.com/i/2xZpg5s.png)

And finally in Open With select your Java version

![prop3](http://imgfz.com/i/UuOcvib.png)

<br />

# Problems

## Linux

### Node is not installed

``` 
sudo apt install nodejs 
```

If you have installed node and npm and the program does not detect it, try this

``` 
n=$(which node);n=${n%/bin/node}; chmod -R 755 $n/bin/*; sudo cp -r $n/{bin,lib,share} /usr/local 
```

### Npm is not installed

``` 
sudo apt install npm 
```

### TikTok Scraper is not installed

``` 
sudo npm i -g tiktok-scraper 
```

If you have this error when you try to install tiktok-scraper,

![error-img](http://imgfz.com/i/XxfG0ho.png)

try this ([Link](https://stackoverflow.com/questions/49679808/error-eacces-permission-denied-mkdir-usr-local-lib-node-modules-node-sass-b))

``` 
sudo npm install -g tiktok-scraper --unsafe-perm=true --allow-root 
```

## Windows

### Node is not installed

* [Solution](#user-content-2-install-nodejs)

### TikTok Scraper is not installed

Option 1:

``` 
npm i -g tiktok-scraper 
```

Option 2:

 * Add npm path to the [environment variables](https://stackoverflow.com/questions/27864040/fixing-npm-path-in-windows-8-and-10#answer-57193639)

### Java version conflict ([Solution](#user-content-1-install-java-jdk-11))

<img src="http://imgfz.com/i/1KaG9E2.png" />

<br />

# Tested Versions

## Java Versions

Java Version | Test
| :---: | :---:
Java 8 | <img src="http://imgfz.com/i/6qfZ1pw.png" width="25px"/>
Java 11 | <img src="http://imgfz.com/i/TlgAGXL.png" width="25px"/>

## Node Versions

Node Version | Test
| :---: | :---:
Node 10.19.0 | <img src="http://imgfz.com/i/TlgAGXL.png" width="25px"/>
Node 14.17.6 | <img src="http://imgfz.com/i/TlgAGXL.png" width="25px"/>

## Tiktok-scraper Versions

Tiktok-scraper Version | Test
| :---: | :---:
Tiktok-scraper 1.4.36 | <img src="http://imgfz.com/i/TlgAGXL.png" width="25px"/>


