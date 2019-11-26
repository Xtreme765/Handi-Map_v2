package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;

    private NodeMap nodeMap;
    private EdgeMap edgeMap;
    private BuildingMap buildingMap;


    //Path Colors
    int rVal = 0;
    int gVal = 0;
    int bVal = 255;
    String pathColor = "Blue";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        nodeMap = new NodeMap();
        edgeMap = new EdgeMap();
        buildingMap = new BuildingMap();



    }

    /** Called when the user taps the Settings button */

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
        Toast.makeText( this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView){
    }

    /** Called when the user taps the Settings button */
    public void settingsMessage(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the Navigation button */
    public void navigationMessage(View view) {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Move the camera to the Union by default
        LatLng rpi_union = new LatLng(42.730192, -73.676731);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rpi_union));
        mMap.setMinZoomPreference(16);

        // Reads through the database and creates all nodes
        createNodes();

        // Reads through the database and creates all edges
        createEdges();
        drawAllEdges();

        Path userRoute;

        String startLocation = "";
        String endDestination = "";

        // Gets locations and colors passed to this activity
        // Replaces default locations with start and end specified
        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ) {
            if ((bundle.getString("startKey") != null) && (bundle.getString("endKey") != null)) {
                startLocation = bundle.getString("startKey");
                endDestination = bundle.getString("endKey");
            }
            if (bundle.getString("colorKey") != null) {
                pathColor = bundle.getString("colorKey");
            }
            userRoute = findPathFromBuildings(startLocation, endDestination);

            if (userRoute == null) {
                System.out.println("No Route Possible");
            }
            // Changes Color based on what's specified
            if (pathColor.equals("Blue")) {
                rVal = 0;
                gVal = 0;
                bVal = 255;
            }
            if (pathColor.equals("Red")) {
                rVal = 255;
                gVal = 0;
                bVal = 0;
            }
            if (pathColor.equals("Pink")) {
                rVal = 255;
                gVal = 192;
                bVal = 203;
            }
            if (pathColor.equals("Green")) {
                rVal = 50;
                gVal = 255;
                bVal = 15;
            }

            drawPath(userRoute, rVal, gVal, bVal, 10);
        }

    }


    // Reads through the database and creates all nodes
    private void createNodes() {
        // Open the resource file stored in /raw/____.csv
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        try {
            // Skip the first line which contains header information
            line = reader.readLine();
            // Loop through the rest of the csv file line by line
            while ((line = reader.readLine()) != null) {
                // Split the line into cells
                String[] tokens = line.split(",");

                // Create the Point from the id and coordinates
                Point newPoint = new Point(tokens[0], Float.parseFloat(tokens[2]), Float.parseFloat(tokens[1]));

                // Create a node from this point along with description, isStairs, and isRamp information
                Node newNode = new Node(newPoint, tokens[3], tokens[4], Boolean.parseBoolean(tokens[5]));

                // Add this new node to the node map for later use
                nodeMap.addNode(newNode);

                String buildingName = newNode.getBuilding();
                if (!buildingName.equals("N/A")) {
                    Entrance newEntrance = new Entrance(buildingName, newNode);
                    buildingMap.addEntrance(newEntrance);
                }
            }

            // Close the input stream
            is.close();
        }
        catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();

        }
    }

    // Reads through the database and creates all edges
    private void createEdges() {
        // Open the resource file stored in /raw/____.csv
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        // Database line is in this format
        //ID,LATITUDE,LONGITUDE,BUILDING NAME,DESCRIPTION,ISSTAIR,EDGES
        try {
            // Skip the first line which contains header information
            line = reader.readLine();
            // Loop through the rest of the csv file line by line
            while ((line = reader.readLine()) != null) {
                // Split the line into cells
                String[] tokens = line.split(",");


                String[] connectedNodes = tokens[6].split(";");

                Node currentNode = nodeMap.getNode(tokens[0]);

                if (currentNode == null) {
                    System.out.println("Error: No node found for current node id: " + tokens[0]);
                    continue;
                }


                for (int i = 0; i<connectedNodes.length; i++) {

                    Node connectedNode = nodeMap.getNode(connectedNodes[i]);

                    // If the node was never made we can't connect it.
                    if (connectedNode == null) {
                        System.out.println("Error: No node found for target node id: " + connectedNodes[i]);
                        System.out.println("       Skipping target node");
                        continue;
                    }

                    // Edge from the current node to one of it's connected nodes
                    Edge newEdge = new Edge(currentNode, connectedNode);

                    // Add the edge to the current Node
                    currentNode.addEdge(newEdge);

                    // Add the edge to the edge map, the edge map will handle duplicates
                    edgeMap.addEdge(newEdge);

                }


            }

            // Close the input stream
            is.close();
        }
        catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();

        }
    }


    // Draws all edges present in the database
    // Might not be included in the final version but serves to visualize the database
    private void drawAllEdges() {


        // Here we loop through every edge in the entire database
        // There are no repeat edges in the edgeMap so we won't draw any duplicate lines
        Hashtable<String, Edge> edgeHashMap = edgeMap.getEdgeMap();
        Iterator edgeIterator = edgeHashMap.entrySet().iterator();
        while(edgeIterator.hasNext()) {

            Map.Entry mapElement = (Map.Entry)edgeIterator.next();
            Edge currentEdge = (Edge)mapElement.getValue();

            Node startingNode = currentEdge.getStartingNode();
            Node endingNode = currentEdge.getEndingNode();

            float startLongitude = startingNode.getPoint().getLongitude();
            float startLatitude = startingNode.getPoint().getLatitude();
            LatLng startCoord = new LatLng(startLatitude, startLongitude);


            float endLongitude = endingNode.getPoint().getLongitude();
            float endLatitude = endingNode.getPoint().getLatitude();
            LatLng endCoord = new LatLng(endLatitude, endLongitude);

            // We will color the stairs red
            if (startingNode.getIsStairs()) {

                mMap.addPolyline(new PolylineOptions()
                        .add(startCoord, endCoord)
                        .width(5)
                        .color(Color.RED));
            }
            // and everything else will be black by default
            else {

                mMap.addPolyline(new PolylineOptions()
                        .add(startCoord, endCoord)
                        .width(5)
                        .color(Color.BLACK));
            }

            // This places markers at all nodes that have building names
            // Remove it to remove markers on the map
            String buildingName = startingNode.getBuilding();
            if (!buildingName.equals("N/A")) {
                LatLng buildingLocation = new LatLng(startingNode.getPoint().getLatitude(), startingNode.getPoint().getLongitude());
                mMap.addMarker(new MarkerOptions().position(buildingLocation).title(buildingName));
            }
            // We check the end node too since there is no direction in the edgeMap iterator
            String buildingName2 = endingNode.getBuilding();
            if (!buildingName2.equals("N/A")) {
                LatLng buildingLocation = new LatLng(endingNode.getPoint().getLatitude(), endingNode.getPoint().getLongitude());
                mMap.addMarker(new MarkerOptions().position(buildingLocation).title(buildingName2));
            }

       }
    }


    // Takes in two building names and compares all possible paths
    // Between all possible connections of entrances
    // To find the best possible path between buildings
    private Path findPathFromBuildings(String startBuilding, String endBuilding) {

        ArrayList<Entrance> startEntrances = buildingMap.getEntrances(startBuilding);
        ArrayList<Entrance> endEntrances = buildingMap.getEntrances(endBuilding);

        // Check if the entrances were found
        if(startEntrances.size() == 0) {
            System.out.println("ERROR: No entrances for building named: " + startBuilding);
            return null;
        }
        if(endEntrances.size() == 0) {
            System.out.println("ERROR: No entrances for building named: " + startBuilding);
            return null;
        }


        // A list of possible paths
        ArrayList<Path> possiblePaths = new ArrayList<>();
        Path finalPath = null;


        // Loop through the entrances a user may start from
        for (int i = 0; i < startEntrances.size(); i++) {

            Entrance nextStartEntrance = startEntrances.get(i);
            Node startNode = nextStartEntrance.getNode();


            // Loop through the entrances a user may end at
            for (int j = 0; j < endEntrances.size(); j++) {

                Entrance nextEndEntrance = endEntrances.get(j);
                Node endNode = nextEndEntrance.getNode();

                // Create path between this particular combination of entrances
                // Save that path in the possible paths list
                Path newPath = findPath(startNode.getId(), endNode.getId());
                if (newPath.getDistance() != 0.0) {
                    possiblePaths.add(newPath);
                }
            }
        }

        // A default value we are basically considering
        // infinite so it will be replaced
        double maximumDistance = 1000000;

        // Loop through possible paths to find the shortest one
        for (int i = 0; i < possiblePaths.size(); i++) {
            Path nextPossiblePath = possiblePaths.get(i);
            if (nextPossiblePath.getDistance() < maximumDistance) {
                finalPath = nextPossiblePath;
                maximumDistance = nextPossiblePath.getDistance();
            }
        }

        // If not path was found, output an error
        if (finalPath == null) {
            System.out.println("No possible paths between " + startBuilding + " and " + endBuilding);
        }

        // Return the final path which will be the shortest possible path
        return finalPath;

    }


    // Finds the shortest route between two nodes
    // Takes in node ids as inputs
    private Path findPath(String startId, String endId) {


        // A copy of the nodemap graph
        // We need a copy so we can edit the scores of each node
        NodeMap graph = new NodeMap(nodeMap);

        // First step of Dijkstras is to set all node
        graph.resetDijkstra();


        // Grab the start node from the graph
        Node currentNode = graph.getNode(startId);

        // Create a beginning path which is just the start node
        Path startPath = new Path(currentNode);
        currentNode.setPath(startPath);


        // The first neighbors are the start node's edges
        ArrayList<Edge> neighbors = currentNode.getEdges();
        ArrayList<String> visited = new ArrayList<>();

        // This is where Dijkstra's algorithm does its magic
        // It continues until there are no more possible neighbors
        while(neighbors.size() != 0) {

            // If there are more neighbors than nodes, there is a bug!
            // It is better to break out of the loop than to crash
            // This probably means there is no path
            if (neighbors.size() > graph.getSize()) {
                break;
            }


            // This loop finds the next edge Dijkstra's algorithm wants
            // this will be the next closest node
            // to the start node along the shortest route
            Iterator edgeIterator = neighbors.iterator();
            Edge shortestEdge = null;
            double minDistance = 1000000;
            while (edgeIterator.hasNext()) {
                Edge nextEdge = (Edge) edgeIterator.next();
                double length = nextEdge.getLength();
                Node currentNeighbor = nextEdge.getStartingNode();
                if (currentNeighbor.getScore() != 1000000) {
                    length = length + currentNeighbor.getScore();
                }
                if (length < minDistance) {
                    minDistance = length;
                    shortestEdge = nextEdge;
                }
            }

            // There should always be a shortestEdge
            // We can't continue otherwise
            if (shortestEdge == null) {
                System.out.println("ERROR NO SHORTEST EDGE");
                return null;
            }

            // Remove the current edge from the neighbors so we don't revisit it
            // This is needed to allow the loop to end
            for (int i = 0; i < neighbors.size(); i++) {
                if (shortestEdge.getStartingNode().getId().equals(neighbors.get(i).getStartingNode().getId())) {
                    if (shortestEdge.getEndingNode().getId().equals(neighbors.get(i).getEndingNode().getId())) {
                        neighbors.remove(i);
                    }

                }
            }

            currentNode = graph.getNode(shortestEdge.getStartingNode().getId());
            Node closestNode = graph.getNode(shortestEdge.getEndingNode().getId());


            // Some testing error cases that should never trigger
            if (currentNode == null) {
                System.out.println("Null Edge in Path Finding");
                continue;
            }
            if (closestNode == null) {
                System.out.println("Null Edge in Path Finding");
                continue;
            }

            // We add the closest node's neighbors to the list of neighbors that
            // could potentially be the next closest point
            ArrayList<Edge> newEdges = closestNode.getEdges();
            for (int i = 0; i < newEdges.size(); i++) {
                Edge newNextEdge = newEdges.get(i);
                Node newNextNode = graph.getNode(newNextEdge.getEndingNode().getId());

                // We make sure the neighbor has not been checked yet
                if (!visited.contains(newNextNode.getId())) {
                    // We make sure the neighbor is not a staircase
                    // Here we will want to check if the user has requested stair routes as well
                    if (!newNextNode.getIsStairs()) {
                        if (!newNextEdge.getStartingNode().getIsStairs()) {
                            neighbors.add(newNextEdge);
                        }
                    }
                }

            }

            // Get the currentNode's Path
            Path tempPath = currentNode.getPath();

            // Create a new path that includes the closest node
            // And the previous best path for that node
            Path newPath = new Path(tempPath, closestNode);

            // If the new path to the closest node is a shorter path than
            // the existing path to that node then update the node
            if (newPath.getDistance() < closestNode.getScore() ) {

                closestNode.setScore(newPath.getDistance());
                closestNode.setPath(newPath);
            }

            // This means we found the end node
            // If we find the end node, while iterating through shortest paths
            // That means the current path is already the best path!
            // That is how Dijkstra's works
            // We return the path
            if (closestNode.getId().equals(endId)) {
                return closestNode.getPath();
            }


            // Keep track of nodes that are done
            visited.add(currentNode.getId());

        }

        // Only called if no path is found, can occur if the end node is unreachable
        // or if the start node is the end node
        // or if the code fails to find a path
        System.out.println("Error: PATH FINDER FAILED");
        Path returnPath = new Path(currentNode);
        return returnPath;
    }



    // This function takes in a path, color, and width
    // Then draws the path on the map as specified
    private void drawPath(Path path, int red, int green, int blue, int width) {


        ArrayList<Node> userPath = path.getPath();

        if (userPath.size() == 0) {
            // Path is empty
            // This should generally not happen
            System.out.println("Error: Path was empty!");
            return;
        }

        // The very first node will be the start point
        Node startNode = userPath.get(0);
        Node endNode;

        // We loop through the path nodes to draw lines
        // Between them
        for (int i = 1; i < userPath.size(); i++) {


            // Get the start coordinate of the next line
            float startLongitude = startNode.getPoint().getLongitude();
            float startLatitude = startNode.getPoint().getLatitude();
            LatLng startCoord = new LatLng(startLatitude, startLongitude);


            // Get the end coordinate of the next line
            endNode = userPath.get(i);
            float endLongitude = endNode.getPoint().getLongitude();
            float endLatitude = endNode.getPoint().getLatitude();
            LatLng endCoord = new LatLng(endLatitude, endLongitude);

            // This function draws the line on the map itself
            // It is a straight line from the start coordinate to the end coordinate
            mMap.addPolyline(new PolylineOptions()
                    .add(startCoord, endCoord)
                    .width(width)
                    .color(Color.rgb(red,green,blue)));

            // Move the start Node to the end Node.
            // This allows us to draw a continuous line
            startNode = endNode;
        }
    }

}
