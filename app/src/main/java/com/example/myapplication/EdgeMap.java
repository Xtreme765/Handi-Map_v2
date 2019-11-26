package com.example.myapplication;

import android.util.Log;

import java.util.Hashtable;

public class EdgeMap {

    private Hashtable<String, Edge> edgeMap;

    // Constructor for a blank edge map
    public EdgeMap() {

        edgeMap = new Hashtable<>();
    }

    // Adds an edge to the map
    public void addEdge(Edge edge) {
        String startId = edge.getStartingNode().getId();
        String endId = edge.getEndingNode().getId();

        // Make sure the edge map doesn't already have this edge (or the reverse of that edge)
        if (!edgeMap.containsKey(startId + endId)  && !edgeMap.containsKey(endId + startId) ) {
            edgeMap.put(startId + endId, edge);
        }
    }


    // Return a copy of the edgemap
    public Hashtable<String, Edge> getEdgeMap() {
        return new Hashtable<>(edgeMap);
    }

}
