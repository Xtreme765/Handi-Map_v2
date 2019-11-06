package com.example.myapplication;

import java.util.ArrayList;

public class Path {

    ArrayList<Node> path;

    public Path(Node node) {
        path = new ArrayList<>();
        path.add(node);
    }

    public Path(Path path) {
        this.path = path.getPath();
    }

    public Path(Path path, Node node) {
        this.path = path.getPath();
        this.path.add(node);
    }





    public ArrayList<Node> getPath() {
        return new ArrayList<>(path);
    }

    public void addNode(Node node) {
        path.add(node);
    }

    public double getDistance() {

        double dist = 0;
        for (int i = 0; i < path.size()-1; i++) {
            Node startingNode = path.get(i);
            Node endingNode = path.get(i+1);
            float lon1 = startingNode.getPoint().getLongitude();
            float lat1 = startingNode.getPoint().getLatitude();
            float lat2 = endingNode.getPoint().getLatitude();
            float lon2 = endingNode.getPoint().getLongitude();

            if ((lat1 == lat2) && (lon1 == lon2)) {
                continue;
            }
            else {
                double theta = lon1 - lon2;
                double thisEdgeDist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
                thisEdgeDist = Math.acos(thisEdgeDist);
                thisEdgeDist = Math.toDegrees(thisEdgeDist);
                thisEdgeDist = thisEdgeDist * 60 * 1.1515;
                dist = dist + thisEdgeDist;
            }

        }
        return dist;

    }




}
