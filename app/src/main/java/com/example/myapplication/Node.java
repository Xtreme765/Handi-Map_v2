package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Node {

    private Point point;
    private String description;
    private boolean isStairs;
    private ArrayList<Edge> edges;
    private double score;
    private String buildingName;
    private Path path;



    public Node(Point point, String buildingName, String description, boolean isStairs) {

        this.point = point;
        this.description = description;
        this.isStairs = isStairs;
        this.buildingName = buildingName;
        // The edge list starts empty since we create nodes one at a time
        // we won't be able to connect a node to a non-existent node
        // Instead we will populate the edges once all nodes have been created
        this.edges = new ArrayList<>();
    }

    public  Node(Node node) {
        this.point = node.getPoint();
        this.description = node.getDescription();
        this.buildingName = node.getBuilding();
        this.isStairs = node.getIsStairs();
        this.edges = node.getEdges();
        this.score = node.getScore();
    }

    // We add edges after all the nodes are created
    public void addEdge(Edge edge) {

        // Check if the edge already exists in the Node's edge list
        String newStartId = edge.getStartingNode().getId();
        String newEndId = edge.getEndingNode().getId();
        Iterator iter = edges.iterator();
        while(iter.hasNext()) {
            Edge tempEdge = (Edge)iter.next();
            String tempStartId = tempEdge.getStartingNode().getId();
            String tempEndId = tempEdge.getEndingNode().getId();
            if ((newStartId.equals(tempStartId) && newEndId.equals(tempEndId) )
                || (newStartId.equals(tempEndId) && newEndId.equals(tempStartId))) {
                    return;
            }
        }
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

    // Tell us if this node is marked as a staircase
    public Boolean getIsStairs(){
        return Boolean.valueOf(isStairs);
    }

    // Returns all edges of the node
    public ArrayList<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    // The score is used in Dijsktras and represents the distance
    // of this particular node from the start node
    public void setScore(double score) {
        this.score = score;
    }
    public double getScore() {
        return score;
    }
    public void setPath(Path path) {
        this.path = path;
    }
    public Path getPath() {
        return new Path(path);
    }
    public String getBuilding() {
        return buildingName;
    }

}
