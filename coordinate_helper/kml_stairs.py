import xml.etree.ElementTree as ET
import csv
import geohash
from distance import get_distance


f_name = input("Enter the file name to open: ")
f_kml = f_name + ".kml"
file = ET.parse(f_kml)

nmsp = '{http://www.opengis.net/kml/2.2}'
b_dict = {}
des = "stairs_raw_data"

# Read from the kml file and parse
for pm in file.iterfind('.//{0}Placemark'.format(nmsp)):
    for nm in pm.iterfind('{0}name'.format(nmsp)):
        des = nm.text.strip()
    for cd in pm.iterfind('{0}Point/{0}coordinates'.format(nmsp)):
        coordinate = cd.text.strip()
        b_dict[coordinate] = des

header = [["ID", "LATITUDE", "LONGITUDE", "BUILDING NAME", "DESCRIPTION", "ISSTAIR", "EDGES"]]
csvData = []
separate = False

# Split latitude and longitude
for c, d in b_dict.items():
    lat = ""
    long = ""
    for i in c:
        if i == ",":
            separate = True
            continue
        if not separate:
            long += i
        else:
            lat += i
    separate = False
    hash_id = geohash.encode(float(lat), float(long))
    csvData.append([hash_id, lat, long, "", d, False, ""])

f_name2 = input("Enter the file name to compare to: ")
f_compare = f_name2 + ".csv"
compare_data = []

# Compare to another file
with open(f_compare, "r", newline="") as compareFile:
    reader = csv.reader(compareFile)
    for row in reader:
        compare_data.append(row)
compareFile.close()

# Comparision operations
for row1 in csvData:
    for row2 in compare_data:
        if row1[0] == "ID" or row2[0] == "ID":
            continue
        if row2[0] == row1[0] or get_distance(float(row1[1]), float(row1[2]), float(row2[1]), float(row2[2])) <= 0.0008:
            row2[5] = True

# Save to csv files
f_csv = f_name + ".csv"
with open(f_csv, "w", newline="") as csvFile:
    writer = csv.writer(csvFile)
    writer.writerows(header)
    writer.writerows(csvData)
csvFile.close()

with open("result.csv", "w", newline="") as csvFile2:
    writer = csv.writer(csvFile2)
    writer.writerows(compare_data)
csvFile2.close()

exit()
