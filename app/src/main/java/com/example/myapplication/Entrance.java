package com.example.myapplication;

public class Entrance {


    private String buildingName;
    private Node node;


    public Entrance(String buildingName, Node node) {
        this.buildingName = buildingName;
        this.node = node;
    }

    public String getBuildingName() {
        return buildingName;
    }
    public Node getNode(){
        return new Node(node);
    }


}
