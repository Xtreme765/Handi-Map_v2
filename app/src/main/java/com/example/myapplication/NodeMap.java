package com.example.myapplication;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class NodeMap {

    // A hashtable of nodes, the key is the Node's Point's ID (geo-hash ID)
    private Hashtable<String, Node> map;

    public NodeMap() {

        map = new Hashtable();

    }

    // Used to create a duplicate node map
    // This might not be a good idea in terms of memory... might not be an issue
    public NodeMap(NodeMap nodeMap) {

        map = nodeMap.getMap();

    }

    // Tells us the size of the database
    public int getSize() {
        return map.size();
    }


    // Adding a new node
    public void addNode(Node node) {
        // There is no reason we should accidentally add duplicates
        if(map.containsKey(node.getId())) {
            System.out.println("Error: Duplicate Node in NodeMap: " + node.getId());
        }
        map.put(node.getId(), node);
    }

    public Node getNode(String id) {
        // Search through the map and return the requested Node
        if (map.containsKey(id)) {
            return map.get(id);
        }

        // We should not be able to request a node that isn't in the database
        // This implies an error in the database
        System.out.println("Error: Invalid Node requested with id " + id);
        return null;

    }

    // Return a copy of the node map
    public Hashtable<String, Node> getMap() {
        return new Hashtable<>(map);
    }


    // Sets all node scores to an essentially infinite value
    // This is the first step in Dijksta's algorithm
    public void resetDijkstra() {

        Iterator nodeIterator = map.entrySet().iterator();
        while(nodeIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)nodeIterator.next();
            Node currentNode = (Node)mapElement.getValue();
            currentNode.setScore(1000000);
        }
    }
}
