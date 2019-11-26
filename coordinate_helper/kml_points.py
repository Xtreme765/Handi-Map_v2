import xml.etree.ElementTree as ET
import csv
import geohash
from distance import get_distance

f_name = input("Enter the file name to open: ")
f_kml = f_name + ".kml"
file = ET.parse(f_kml)

nmsp = '{http://www.opengis.net/kml/2.2}'
b_dict = {}
des = ""

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

# Split the coordinates
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

# Compare to another file
f_name2 = input("Enter the file name to compare to: ")
f_compare = f_name2 + ".csv"
compare_data = []

with open(f_compare, "r", newline="") as compareFile:
    reader = csv.reader(compareFile)
    for row in reader:
        compare_data.append(row)
compareFile.close()

# Actual comparison operations
for row1 in csvData:
    words = row1[4].split()
    if "87" in words:
        row1[3] = "87 Gymnasium"
    elif "Academy" in words:
        row1[3] = "Academy Hall"
    elif "Admission" in words:
        row1[3] = "Admissions"
    elif "Amos" in words:
        row1[3] = "Amos Eaton"
    elif "Barton" in words:
        row1[3] = "Barton Hall"
    elif "Blitman" in words:
        row1[3] = "Blitman Commons"
    elif "Bray" in words:
        row1[3] = "Bray Hall"
    elif "Carnegie" in words:
        row1[3] = "Carnegie Building"
    elif "Cary" in words:
        row1[3] = "Cary Hall"
    elif "CBIS" in words:
        row1[3] = "Center for Biotech. (CBIS)"
    elif "Cogswell" in words:
        row1[3] = "Cogswell Laboratory"
    elif "Commons Dining Hall" in words:
        row1[3] = "Commons Dining Hall"
    elif "Crockett" in words:
        row1[3] = "Crockett Hall"
    elif "DCC" in words:
        row1[3] = "Darrin Comm. Center (DCC)"
    elif "Davison" in words:
        row1[3] = "Davison Hall"
    elif "Library" in words:
        row1[3] = "Folsom Library"
    elif "Greene" in words:
        row1[3] = "Greene Building"
    elif "JEC" in words:
        row1[3] = "Jonsson Engineering Center (JEC)"
    elif "JEC" in words:
        row1[3] = "Jonsson Engineering Center (JEC)"
    elif "JROWL" in words:
        row1[3] = "Jonsson-Rowland Science Center (JROWL)"
    elif "Low" in words or "Lowe" in words:
        row1[3] = "Low Center for Ind. Innov.(CII)"
    elif "Lally" in words:
        row1[3] = "Lally Hall"
    elif "Mueller" in words:
        row1[3] = "Mueller Center"
    elif "Nason" in words:
        row1[3] = "Nason Hall"
    elif "Nugent" in words:
        row1[3] = "Nugent Hall"
    elif "Pittsburgh" in words:
        row1[3] = "Pittsburgh Building"
    elif "Pubsafe" in words:
        row1[3] = "Public Safety"
    elif "Union" in words:
        row1[3] = "Rensselaer Union"
    elif "Ricketts" in words:
        row1[3] = "Ricketts Building"
    elif "Sage" in words and "Dining" in words:
        row1[3] = "Sage Dining Hall"
    elif "Sage" in words:
        row1[3] = "Sage Laboratory"
    elif "Troy" in words:
        row1[3] = "Troy Building"
    elif "VCC" in words:
        row1[3] = "Voorhees Computing Center (VCC)"
    elif "Walker" in words:
        row1[3] = "Walker Laboratory"
    elif "West" in words:
        row1[3] = "West Hall"

    for row2 in compare_data:
        if row1[0] == "ID" or row2[0] == "ID":
            continue
        if row2[0] == row1[0] or get_distance(float(row1[1]), float(row1[2]), float(row2[1]), float(row2[2])) <= 0.0008:
            row1[6] = row2[6]
            row2[3] = row1[3]
            row2[4] = row1[4]

# Write to csv file
f_csv = f_name + ".csv"
with open(f_csv, "w", newline="") as csvFile:
    writer = csv.writer(csvFile)
    writer.writerows(header)
    writer.writerows(csvData)
csvFile.close()

with open("result_no_stairs.csv", "w", newline="") as csvFile2:
    writer = csv.writer(csvFile2)
    writer.writerows(compare_data)
csvFile2.close()

exit()
