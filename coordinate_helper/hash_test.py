import geohash

inputFile = open("input.txt", "r+")
outputFile = open("output.txt", "w")

for line in inputFile:
    coords = line.split()
    lat = float(coords[0])
    long = float(coords[1])
    encoded = geohash.encode(lat, long)
    outputFile.write(encoded + "\n")
    print("Encoded:", lat, long, "=", encoded)
    decoded = geohash.decode(encoded)
    print("Decoded:", decoded)


print("Finished encoding, output saved to output.txt")