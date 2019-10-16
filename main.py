import xml.etree.ElementTree as ET
import csv
import geohash

f_name = input("Enter the file name to open: ")
f_kml = f_name + ".kml"
file = ET.parse(f_kml)

isRamp = input("Is this route ramped? (y/n) ")
if isRamp == "y":
    isRamp = "true"
elif isRamp == 'n':
    isRamp = "false"
isStairs = input("Is this route stairs? (y/n) ")
if isStairs == "y":
    isStairs = "true"
elif isStairs == 'n':
    isStairs = "false"
des = input("Enter description: ")

nmsp = '{http://www.opengis.net/kml/2.2}'
c_list = []

for pm in file.iterfind('.//{0}Placemark'.format(nmsp)):
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

csvData = [["ID", "LATITUDE", "LONGITUDE", "DESCRIPTION", "ISSTAIRS", "ISRAMP", "EDGES"]]
for i in range(0, len(c_list), 2):
    edges = ""
    if i != 0:
        edges += "(" + geohash.encode(float(c_list[i-2]), float(c_list[i-1])) + ") "
    if i != len(c_list)-2:
        edges += "(" + geohash.encode(float(c_list[i+2]), float(c_list[i+3])) + ") "
    hash_id = geohash.encode(float(c_list[i]), float(c_list[i + 1]))
    csvData.append([hash_id, c_list[i], c_list[i + 1], des, isStairs, isRamp, edges])

f_csv = f_name + ".csv"
with open(f_csv, "w", newline="") as csvFile:
    writer = csv.writer(csvFile)
    writer.writerows(csvData)
csvFile.close()

exit()
