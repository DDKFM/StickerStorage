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

run with docker-compose:
version: "3"
services:
  db:
    image: "postgres"
    container_name: "postgres"
    environment:
      - POSTGRES_USER=stickerstorage
      - POSTGRES_PASSWORD=stickerstorage
      - POSTGRES_DB=stickerstorage
    volumes:
      - ./postgres-data:/var/lib/postgresql/data
  stickerstorage:
    depends_on:
      - db
    image: "ddkfm/stickerstorage"
    container_name: "stickerstorage"
    environment:
      - DATABASE_HOST=db
      - DATABASE_USERNAME=stickerstorage
      - DATABASE_PASSWORD=stickerstorage
      - DATABASE_NAME=stickerstorage
      - AUTHENTICATION_USERNAME=admin
      - AUTHENTICATION_PASSWORD=admin
      - JWT_SECRET=MyPerfectSecret!
    ports:
      - 8084:8080
