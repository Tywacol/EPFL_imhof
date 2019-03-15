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
and compile it with

```bash
cd EPFL_imhof
javac ch/epfl/imhof/Main.java
```

## Usage
Arguments
```bash
java Main osmFile.osm.gz, hgtFile.hgt, botLeftLon, botLeftLat, topRightLong, topRightLat, rDpi, output_name
```
*osmFile.osm.gz* : the compressed data corresponding to the desired area. To create one go to <www.openstreetmap.org>, chose the location and the dimensions than click the export button.

Note : since a single .hgt file can contains data for a square of 1 of longitude and 1 of lattitude, the latittude and longitude of the area you chosen must be contained within 1 of each other. Example: the coordinate for interlaken are 
7.7248 -> 7.994 in longitude and 46.6348 -> 46.7377 in latitude, it's in N46E007.hgt. Here 6.9999 -> 7.994 wouldn't work. 

To compress the file on linux, run :

```bash
gzip map.osm 
```
*hgtFile.hgt* : The hgt file can be downloaded from <http://viewfinderpanoramas.org/dem3.html>. Choose your regions than click on the interactive map to download the file. You may need to extract it. Note that there is 3 resolutions available : 1" 3" and 15" (arc second). the smaller, the better the details of the digital elevation model. 1" is not available everywhere but 3", however the use of 15" is discouraged due to the lack of precision.

*botLeftLon, botLeftLat, topRightLong, topRightLat* : Coordinates of the bottom-left and top-right corner of the area inside of OSM file.

*rDpi* : it's the dpi of the final map. More means better details but increasing file size and runtime. 100 produces mostly balanced results.

*output_name* : the name of the file generated.

As an example the command to generate the map of interlaken in examples_files/interlaken/command.txt is :
```bash
java -cp . ch.epfl.imhof.Main interlaken_high.osm.gz N46E007.hgt 7.7248 46.6348 7.9943 46.7377 300 interlakenhighHD300.png
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)

