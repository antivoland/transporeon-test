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

ffmpeg -i ui.720p.mov -f mp4 -vcodec libx264 -preset fast -profile:v main -acodec aac output.mp4
