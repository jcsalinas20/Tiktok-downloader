# Tiktok-downloader

## Index
 * [Windows 10 Installation](#windows-10-installation)
 * [Ubuntu 20.04 Installation](#ubuntu-2004-installation)
 * [Run Program]()
 * [Program Problems](#program-problems)

## Windows 10 Installation

### 1. A

## Ubuntu 20.04 Installation

### 1. Install Java 11

```
sudo apt install openjdk-11-jre 
```

### 2. Install JavaFX

``` 
sudo apt install openjfx 
```

### 3. Install Node and npm

``` 
sudo apt install nodejs npm
```

### 4. Install tiktok-scraper

``` 
sudo npm i -g tiktok-scraper
```


<br />

## Program Problems

### Node is not installed

``` sudo apt install nodejs ```

If you have installed node and npm and the program does not detect it, try this

``` n=$(which node);n=${n%/bin/node}; chmod -R 755 $n/bin/*; sudo cp -r $n/{bin,lib,share} /usr/local ```

### Npm is not installed

``` sudo apt install npm ```

### TikTok Scraper is not installed

``` npm i -g tiktok-scraper ```

If you have installed tiktok-scraper and the program does not detect it, try this

``` sudo npm i -g tiktok-scraper ```
