package com.example.myapplication;

import java.util.Hashtable;
import java.util.Iterator;

public class NodeMap {


    // A hashtable of nodes, the key is the Node's Point's ID (geo-hash ID)
    private Hashtable<String, Node> map;

    public NodeMap() {
        map = new Hashtable();
    }

    public void addNode(Node p) {
        map.put(p.getId(), p);
    }

    public Node getNode(String id) {
        // Search through the map and return the requested Node
        if (map.containsKey(id)) {
            return map.get(id);
        }
        System.out.println("Invalid Node requested with id " + id);
        return null;

    }


    public void resetDijkstra() {


        Iterator nodeIterator = map.entrySet().iterator();
        while(nodeIterator.hasNext()) {
          //  map.
        }

    }

}
