# Tiktok-downloader

## Instalación Ubuntu 20.04

### Install Java 11

``` sudo apt install openjdk-11-jre ```

### Install JavaFX ([web](https://gluonhq.com/products/javafx/))

``` Download JavaFX 11: ``` [Download](https://gluonhq.com/download/javafx-11-0-2-sdk-linux/)

``` sudo apt install nodejs ```

``` sudo apt install npm ```

``` npm i -g tiktok-scraper ```

## Problemas

Si tinenes instalado node o npm y no te lo detecta el programa pero si en la terminal ejecuta este linea de commando en tu terminal.

``` n=$(which node);n=${n%/bin/node}; chmod -R 755 $n/bin/*; sudo cp -r $n/{bin,lib,share} /usr/local ```
