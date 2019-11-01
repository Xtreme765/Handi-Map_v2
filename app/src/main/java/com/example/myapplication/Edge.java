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


    // Coordinate Distance Code Source: https://www.geodatasource.com/developers/java
    // The sample code is licensed under LGPLv3.
    //   unit 'M' is statute miles (default)
    //   unit 'K' is kilometers
    //   unit 'N' is nautical miles
    public double getLength(String unit) {
            float lon1 = startingNode.getPoint().getLongitude();
            float lat1 = startingNode.getPoint().getLatitude();
            float lat2 = endingNode.getPoint().getLatitude();
            float lon2 = endingNode.getPoint().getLongitude();

            if ((lat1 == lat2) && (lon1 == lon2)) {
                return 0;
            }
            else {
                double theta = lon1 - lon2;
                double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
                dist = Math.acos(dist);
                dist = Math.toDegrees(dist);
                dist = dist * 60 * 1.1515;
                if (unit == "K") {
                    dist = dist * 1.609344;
                } else if (unit == "N") {
                    dist = dist * 0.8684;
                }
                return dist;
            }
        }



}


