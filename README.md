# Flight Connections

[![Build Status](https://github.com/antivoland/transporeon-test/workflows/build/badge.svg)](https://github.com/antivoland/transporeon-test/actions/workflows/build.yml)

Check the task description [here](TASK.md). We need to find a shortest route between two airports. This route shouldn't contain more than 4 flights. It is also possible to move between airports by land if the distance between them is less than 100 km, however these changes cannot be made more than once in a row.

I [started](https://github.com/antivoland/transporeon-test/tree/typescript) with the implementation using TypeScript, but soon I realized that I would not be able to meet any deadlines, since I was not used to building backends with this stack, and the algorithmic task itself is quite complicated. So I've decided to focus on the solution using Java. Check the project specification [here](TECH.md).

## Run

You'll need to build the project (prerequisites are Maven and 17th Java):

```shell
make build
```

And then run the application server as follows:

```shell
make run
```

I created a graphical interface in a fast and a simple manner, and the result looks awesome. Check the video below:

https://github.com/antivoland/transporeon-test/assets/3669979/29915220-8819-4e6f-87e7-20d97aea014f

Open `localhost:40075` in your browser to access the UI. It allows to select source and destination airports by IATA or ICAO codes or simply by name. You can search for routes using the original task constraint (4 flights maximum) or search for routes of any length. There are also two display themes to choose from, dark and light.

## Main algorithm

The existence of single ground runs makes it impossible to use a pure shortest route searching algorithm. So I introduced virtual nodes of two types: entered by air and entered by ground. If some route ends with a virtual node entered by ground, then the next move can only be made by air.

The algorithm is an extension of Dijkstra's one with a Fibonacci heap. We still pick the shortest route on every step, traverse its neighbors and so on. And the proof of correctness is about the same.

I only want to outline why we can stop searching once we have reached any of the virtual destination nodes. Same logic, if there is a route between source and the second virtual node, that is shorter, then the algorithm will reach it first.

## Segmentation

Calculating the distance between two points is a rather expensive operation. For a dataset of more than 7000 airports, a parallelized brute force method to determine all ground routes between airports within a radius of 100 km can take a minute. This is a bit annoying as I would also like to use datasets not only at runtime but also in tests.

So let's split the Earth into segments as follows:

![Sectors](sectors.png)

There are regular and pole segments. For each given segment, I can find an environment consisting of other segments that have points closer than 100 km to any possible point within that segment. There is a special behaviour for getting an environment for pole sectors, however the implementation is clean and self-descriptive, so I won't go into details here.

## Datasets

I've found suitable [datasets](https://openflights.org/data.html) at openflights.org:
* [airports.dat](src/main/resources/data/airports.dat) having the following structure:
  * **Airport ID**, unique OpenFlights identifier for this airport
  * **Name**, name of airport
  * **City**, main city served by airport
  * **Country**, country or territory where airport is located
  * **IATA**: 3-letter IATA code (null if not assigned/unknown)
  * **ICAO**, 4-letter ICAO code (null if not assigned)
  * **Latitude**, decimal degrees, usually to six significant digits (negative is South, positive is North)
  * **Longitude**, decimal degrees, usually to six significant digits (negative is West, positive is East)
  * **Altitude**, altitude in feet
  * **Timezone**, hours offset from UTC
  * **DST**, daylight savings time
  * **Tz database time zone**, timezone in "tz" (Olson) format (eg. "America/Los_Angeles")
  * **Type**, type of the airport (only type=airport is used)
  * **Source**,	source of this data
* [routes.dat](src/main/resources/data/routes.dat), which are directional (if an airline operates services from A to B and from B to A, both A-B and B-A are listed separately) and have the structure described below:
  * **Airline**, 2-letter (IATA) or 3-letter (ICAO) code of the airline
  * **Airline ID**, unique OpenFlights identifier for airline (see Airline)
  * **Source airport**, 3-letter (IATA) or 4-letter (ICAO) code of the source airport
  * **Source airport ID**, Unique OpenFlights identifier for source airport
  * **Destination airport**, 3-letter (IATA) or 4-letter (ICAO) code of the destination airport
  * **Destination airport ID**, unique OpenFlights identifier for destination airport
  * **Codeshare**, "Y" if this flight is a codeshare (that is, not operated by Airline, but another carrier), empty otherwise
  * **Stops**, number of stops on this flight ("0" for direct)
  * **Equipment**, 3-letter codes for plane type(s) generally used on this flight, separated by spaces

These datasets are widely used, [here](https://www.kaggle.com/datasets/elmoallistair/airlines-airport-and-routes?select=airports.csv) and [here](https://www.kaggle.com/datasets/elmoallistair/airlines-airport-and-routes?select=routes.csv) you'll find some stats for each field.

## Test coverage

I only focused on testing crucial parts of the solution and briefly introduced some other tests.