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

# Test datasets

I used 4 airports for the sake of testing:
* LYR, the northernmost airport in the world
* OSL, Oslo Airport
* TOS, Tromsø Airport
* TLL, Tallinn Airport

TLL -> CCE4 - not found

TLL -> BUZ, AGV, BIAR, BISF - roads

AAP -4 len
ABE - 4 len, ends with road

AHU -2 fl, 2 roads
ASP -road, far , 4 legs

tll - ags - 5 unlimited
- anq 5 + road at the end

anm 8 fl, ends with road / limited exists

bca 6fl, 1r / limited doesn't exist

brt, bsd, byq, adl, agn, aju lim/unl diff visible nicely

bzf  - side move

Video:
srs: TLL
dst: anm
limited: true
limited: false
ags
limited: true
limited: false
dst: buz
dst: null
src: null

ffmpeg -i ui.720p.mov -f mp4 -vcodec libx264 -preset fast -profile:v main -acodec aac ui.mp4


grep 'LYR' routes.dat --color=always

grep -E '^.*,[A-Za-z0-9]{3,4},.*,(LYR|ENSB),.*$' routes.dat

grep -E '^.*,(TLL|EETN),.*,(OSL|ENGM|TOS/ENTC),.*$' routes.dat


grep -E '^.*(TLL|EETN).*(OSL|TOS).*$' routes.dat
grep '^(.*?)[LYR](.*?)$' routes.dat --color=always
