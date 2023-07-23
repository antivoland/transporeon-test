# Tech specification

This project consists of a backend written in Java and a simple JavaScript frontend.

## Backend

* [Spring Boot](https://github.com/spring-projects/spring-boot)
* [Geodesy](https://github.com/mgavaghan/geodesy)
* [JHeaps](https://github.com/d-michail/jheaps)
* [Guava](https://github.com/google/guava)
* [Jackson](https://github.com/FasterXML/jackson)
* [Lombok](https://github.com/projectlombok/lombok)

## Frontend

* [Globe.GL](https://github.com/vasturiano/globe.gl)
* [Selectize.js](https://github.com/selectize/selectize.js)
* [sweetalert2](https://github.com/sweetalert2/sweetalert2)

## Video

I recorded the video demonstrating UI with a screen recorder and compressed using the following magic:

```
ffmpeg -i ui.720p.mov -f mp4 -vcodec libx264 -preset fast -profile:v main -acodec aac ui.mp4
```