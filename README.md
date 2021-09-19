# Tiktok-downloader

## Installation en Ubuntu 20.04

### Install Java 11

``` sudo apt install openjdk-11-jre ```

### Install JavaFX

``` sudo apt install openjfx ```

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
