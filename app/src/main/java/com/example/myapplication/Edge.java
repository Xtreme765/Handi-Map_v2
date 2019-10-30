package com.example.myapplication;

public class Edge {


    private Node startingNode;
    private Node endingNode;


    Edge(Node startingNode, Node endingNode) {
        System.out.println("Creating new edge from id: " + startingNode.getId() + " to node with id: " + endingNode.getId());
        this.startingNode = startingNode;
        this.endingNode = endingNode;
    }

    public Node getStartingNode() {
        System.out.println("Getting start node");
        return new Node(startingNode);
    }
    public Node getEndingNode() {
        System.out.println("Getting end node");
        return new Node(endingNode);
    }


}


