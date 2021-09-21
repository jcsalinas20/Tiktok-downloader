# Tiktok-downloader

## Index
 * [Windows 10 Installation](#windows-10-installation)
 * [Ubuntu 20.04 Installation](#ubuntu-2004-installation)
 * [Run Program](#run-program)
 * [Problems](#problems)

## Windows 10 Installation

### 1. A

## Ubuntu 20.04 Installation

### 1. Install Java 11

```
sudo apt install openjdk-11-jre 
```

### 2. Install Node and Npm

``` 
sudo apt install nodejs npm
```

### 3. Install tiktok-scraper

``` 
sudo npm i -g tiktok-scraper
```

## Run Program

### In terminal, cmd, PowerShell, etc.

```
java -jar Tiktok-downloader
```

### In File System

* Windows: With double click

* Linux/Ubuntu

<br />

## Problems

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

