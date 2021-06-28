# Tiktok-downloader

## Instalación en Ubuntu 20.04

### Install Java 11

``` sudo apt install openjdk-11-jre ```

### Install JavaFX

``` sudo apt install openjfx ```

## Program Problems

### Node is not installed

``` sudo apt install nodejs ```

### Npm is not installed

``` sudo apt install npm ```

### TikTok Scraper is not installed

``` npm i -g tiktok-scraper ```

Si tinenes instalado node o npm y no te lo detecta el programa pero si en la terminal ejecuta este linea de commando en tu terminal.

``` n=$(which node);n=${n%/bin/node}; chmod -R 755 $n/bin/*; sudo cp -r $n/{bin,lib,share} /usr/local ```
