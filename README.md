# Tiktok-downloader

## Instalación en Ubuntu 20.04

### Install Java 11

``` sudo apt install openjdk-11-jre ```

### Install JavaFX ([web](https://gluonhq.com/products/javafx/))

Download JavaFX 11: [Download](https://gluonhq.com/download/javafx-11-0-2-sdk-linux/)

Install: ``` sudo apt install openjfx ```

Extract zip file: ``` unzip <zip_file> ```

Move JavaFX folder: ``` sudo mv <javafx_folder> /usr/lib/jvm/ ```

Create ENV variable: ``` sudo nano /etc/profile ```

In the bottom of the file put: ``` export PATH_TO_FX="/usr/lib/jvm/<javafx_folder>/lib" ```

Save the file and execute: ``` source /etc/profile ```

## Program Problems

### Node is not installed

``` sudo apt install nodejs ```

### Npm is not installed

``` sudo apt install npm ```

### TikTok Scraper is not installed

``` npm i -g tiktok-scraper ```

Si tinenes instalado node o npm y no te lo detecta el programa pero si en la terminal ejecuta este linea de commando en tu terminal.

``` n=$(which node);n=${n%/bin/node}; chmod -R 755 $n/bin/*; sudo cp -r $n/{bin,lib,share} /usr/local ```
