package com.example.myapplication;


import java.util.ArrayList;
import java.util.HashMap;

public class BuildingMap {

    HashMap<String, ArrayList<Entrance>> map;


    // Constructor. Creates an empty building map.
    public BuildingMap() {

        map = new HashMap<>();
    }

    // Add a new entrance to the map
    public void addEntrance(Entrance entrance) {

        // If the map already has this building,
        // add the entrance to the existing list for that building
        if (map.containsKey(entrance.getBuildingName())) {
            ArrayList<Entrance> entranceList = map.get(entrance.getBuildingName());
            entranceList.add(entrance);
        }
        // If this is the first entrance for that building,
        // make a new list for that building
        else {
            ArrayList<Entrance> newList = new ArrayList<>();
            newList.add(entrance);
            map.put(entrance.getBuildingName(), newList);
        }
    }

    // Returns all entrances for a building name
    public ArrayList<Entrance> getEntrances(String buildingName) {
        if (map.containsKey(buildingName)) {
            return map.get(buildingName);
        }

        // This only happens if you request a building name that's not in the database
        // Typically this means there was a typo in a building name so we will print it out
        System.out.println("ERROR: No Entrances for: " + buildingName);
        return null;
    }

}
