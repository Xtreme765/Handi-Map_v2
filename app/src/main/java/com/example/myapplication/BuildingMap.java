package com.example.myapplication;


import java.util.ArrayList;
import java.util.HashMap;

public class BuildingMap {

    HashMap<String, ArrayList<Entrance>> map;



    public BuildingMap() {
        map = new HashMap<>();
    }

    public BuildingMap(Entrance entrance) {
        map = new HashMap<>();
        ArrayList<Entrance> newList = new ArrayList<>();
        newList.add(entrance);
        map.put(entrance.getBuildingName(), newList);
    }


    public void addEntrance(Entrance entrance) {


        if (map.containsKey(entrance.getBuildingName())) {
            ArrayList<Entrance> entranceList = map.get(entrance.getBuildingName());
            entranceList.add(entrance);
        }
        else {
            ArrayList<Entrance> newList = new ArrayList<>();
            newList.add(entrance);
            map.put(entrance.getBuildingName(), newList);
        }
    }

    public ArrayList<Entrance> getEntrances(String buildingName) {
        if (map.containsKey(buildingName)) {
            return map.get(buildingName);
        }
        System.out.println("ERROR: No Entrances for: " + buildingName);
        return null;
    }

}
