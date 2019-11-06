package com.example.myapplication;

import android.util.Log;

import java.util.Hashtable;

public class EdgeMap {

    private Hashtable<String, Edge> edgeMap;

    public EdgeMap() {
        edgeMap = new Hashtable<>();
    }

    public void addEdge(Edge edge) {
        String startId = edge.getStartingNode().getId();
        String endId = edge.getEndingNode().getId();


        // Make sure the edge map doesn't already have this edge (or the reverse of that edge)
        if (!edgeMap.containsKey(startId + endId)  && !edgeMap.containsKey(endId + startId) ) {
            edgeMap.put(startId + endId, edge);
        }
        else {
            System.out.println("Edge already in map");
        }
    }

    public void removeEdge(Edge edge) {

        String startId = edge.getStartingNode().getId();
        String endId = edge.getEndingNode().getId();
        if (edgeMap.containsKey(startId + endId) ) {
            edgeMap.remove(startId + endId);
        }
        else if (edgeMap.containsKey(endId + startId) ) {
            edgeMap.remove(endId + startId);
        }
        else {
            Log.d("EdgeMap", "Tried removing edge not in map: " + startId + "+" + endId);
        }
    }

    public boolean contains(Edge edge) {

        String startId = edge.getStartingNode().getId();
        String endId = edge.getEndingNode().getId();
        if (edgeMap.containsKey(startId + endId)  || edgeMap.containsKey(endId + startId) ) {
            return true;
        }
        return false;

    }

    public Hashtable<String, Edge> getEdgeMap() {
        return new Hashtable<>(edgeMap);
    }

}
