import xml.etree.ElementTree as ET
import csv
import geohash
from distance import get_distance

f_name = input("Enter the file name to open: ")
f_kml = f_name + ".kml"
file = ET.parse(f_kml)

isStairs = False
des = ""
building_name = "N/A"

nmsp = '{http://www.opengis.net/kml/2.2}'
c_list = []

# Read from the kml file and parse
for pm in file.iterfind('.//{0}Placemark'.format(nmsp)):
    if len(c_list) != 0 and c_list[len(c_list)-1] != "SEPARATE":
        c_list.append("SEPARATE")
    for ls in pm.iterfind('{0}LineString/{0}coordinates'.format(nmsp)):
        string = ls.text.strip()
        coordinate = ""
        for i in string:
            if i != " " and i != ",":
                coordinate += i
            if i == ",":
                c_list.append(coordinate)
                coordinate = ""
            if i == "\n":
                coordinate = ""

# Actual parsing operations
header = [["ID", "LATITUDE", "LONGITUDE", "BUILDING NAME", "DESCRIPTION", "ISSTAIR", "EDGES"]]
csvData = []
start = False
end = False
i = 0
while i < len(c_list):
    edges = ""
    if c_list[i] == "SEPARATE":
        start = True
        i += 1
    else:
        start = False
    if i < len(c_list)-2 and c_list[i+2] == "SEPARATE":
        end = True
    else:
        end = False
    if i != 0 and not start:
        if c_list[i-1] == "SEPARATE":
            edges += geohash.encode(float(c_list[i-2]), float(c_list[i-3])) + ";"
        else:
            edges += geohash.encode(float(c_list[i-1]), float(c_list[i-2])) + ";"
    if i != len(c_list)-2 and not end:
        if c_list[i+2] == "SEPARATE":
            edges += geohash.encode(float(c_list[i + 4]), float(c_list[i + 3])) + ";"
        else:
            edges += geohash.encode(float(c_list[i+3]), float(c_list[i+2])) + ";"
    hash_id = geohash.encode(float(c_list[i+1]), float(c_list[i]))
    found = False
    for row in csvData:
        if row[0] == hash_id or get_distance(c_list[i+1], c_list[i], row[1], row[2]) < 0.0005:
            row[6] += edges
            found = True
            break
    if not found:
        csvData.append([hash_id, c_list[i+1], c_list[i], building_name, des, isStairs, edges])
    i += 2

# Writing to csv file
f_csv = f_name + ".csv"
with open(f_csv, "w", newline="") as csvFile:
    writer = csv.writer(csvFile)
    writer.writerows(header)
    writer.writerows(csvData)
csvFile.close()

exit()
