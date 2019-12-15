<p align="center">
  <a href="https://github.com/DDKFM/StickerStorage/">
    <img src="logo.svg" alt="Logo" width="320px">
  </a>
</p>
<p align="center">
  <a href="https://kotlinlang.org/">
    <img src="kotlin.svg" alt="Logo" width=72 height=72>
  </a>
    <a href="">
    <img src="https://wiki.postgresql.org/images/3/30/PostgreSQL_logo.3colors.120x120.png" alt="Postgresql", width="72px"/>
  </a>
</p>

# Sticker storage

Sticker storage provides a spring boot powered backend with a REST-API to manage your whole sticker collection.

## The problem

The main problem for all of us developers is to keep track of all stickers, which we bring along from it conferences and meetups.

## The solution

the sticker storage backend provides a CRUD Rest-API to store the stickers and assigm them to locations or events. 

## Requirements

- docker
- or for building from source: a OpenJDK > 8
## How to use it



## Run

You can run a development web server with the Angular CLI using `ng serve`. If you want to use the integrated proxy of the angular cli development server, as mentioned in "Preparation", you need to run `ng serve --proxy-config proxy/proxy.conf.json`.

## Build

To build the web app run `ng build --prod`.

## Mobile App

We use [Apache Cordova](https://cordova.apache.org/) to generate mobile apps. In order to generate a mobile app, follow these steps:

1. Install the cordova CLI by executing `npm install -g cordova`
2. Create an application folder by executing `cordova create plan4BA_mobile de.ba-leipzig.plan4ba "Plan4BA"`
3. Navigate into the folder using `cd plan4BA_mobile`
4. Add the desired platform, for example android, by executing `cordova platform add android`
5. In the root directory of this Angular application, run `ng build --prod --base-href . --output-path ../Plan4plan4BA_mobileBA/www/` (adjust the output path)
6. Add `<script type=”text/javascript” src=”cordova.js”></script>` to the generated index.html file
7. Build for your desired platform, for example android, by executing `cordova build android`

For further information, read [this guide](https://medium.com/@EliaPalme/how-to-wrap-an-angular-app-with-apache-cordova-909024a25d79).
