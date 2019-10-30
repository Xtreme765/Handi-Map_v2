package com.example.myapplication;

import java.util.ArrayList;

public class Node {

    private Point point;
    private String description;
    private boolean isStairs;
    private boolean isRamp;
    private ArrayList<Edge> edges;



    public Node(Point point, String description, boolean isStairs, boolean isRamp) {

        this.point = point;
        this.description = description;
        this.isStairs = isStairs;
        this.isRamp = isRamp;
        // The edge list starts empty since we create nodes one at a time
        // we won't be able to connect a node to a non-existent node
        // Instead we will populate the edges once all nodes have been created
        this.edges = new ArrayList<>();
    }

    public  Node(Node node) {
        this.point = node.getPoint();
        this.description = node.getDescription();
        this.isStairs = node.getIsStairs();
        this.isRamp = node.getIsRamp();
        this.edges = node.getEdges();
    }

    // We add edges after all the nodes are created
    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public String getId() {
        return this.point.getId();
    }

    public Point getPoint() {
        return new Point(point);
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsStairs(){
       return Boolean.valueOf(isStairs);
    }

    public Boolean getIsRamp(){
       return Boolean.valueOf(isRamp);
    }

    public ArrayList<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

}
