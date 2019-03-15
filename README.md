# EPFL imhof

Java project made during my second semester at the EPFL, Switzerland. This program renders images from OpenStreetMap and height maps data.

Example : HD Map of interlaken made using this program :

![](https://github.com/Tywacol/EPFL_imhof/blob/master/examples_files/interlaken/interlakenhighHD300.png?raw=true)

## How it works

The programs parses a compressed .osm file containing the maps elements (roads, building, lakes etc) so that its easily usable after. 2D map example from example_files/interlaken/interlaken.osm.gz :

![](https://github.com/Tywacol/EPFL_imhof/blob/master/examples_files/interlaken/paintedMap.png?raw=true)

It then create an height map using the data from the corresponding hgt file passed on argument. height map example from example_files/interlaken/N46E007.hgt :

![](https://github.com/Tywacol/EPFL_imhof/blob/master/examples_files/interlaken/relief.png?raw=true)

It then mixes the two files to produce a map with relief, as seen in the presentation.

## Prequisite

This project require Oracle JDK8 or newer to be compiled.
On Arch-based systems it can be installed by running
```bash
pacman -Syu jdk8-openjdk
```

## Installation

You can clone the project with
```bash
git clone git@github.com:Tywacol/EPFL_imhof.git
```


## Usage

```bash
python3 snake.py
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)

