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
    public int getSize() {
        return map.size();
    }

    public void addNode(Node node) {
        if(map.containsKey(node.getId())) {
            System.out.println("Error: Duplicate Node in NodeMap");
        }
        map.put(node.getId(), node);
    }

    public void removeNode(Node node) {

        map.remove(node.getId());
    }
    public void removeNode(String id) {

        map.remove(id);
    }
    public Node getNode(String id) {
        // Search through the map and return the requested Node
        if (map.containsKey(id)) {
            return map.get(id);
        }
        System.out.println("Invalid Node requested with id " + id);
        return null;

    }



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
