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



    // Sully Was here
    // Virginia is for lovers
    // Darrian was not here
    // What's up it's Mao
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

        // Add a marker in Sydney and move the camera
        LatLng rpi_union = new LatLng(42.730192, -73.676731);
        mMap.addMarker(new MarkerOptions().position(rpi_union).title("The Union"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rpi_union));
        mMap.setMinZoomPreference(16);

        // Reads through the database and creates all nodes
        createNodes();

        // Reads through the database and creates all edges
        createEdges();


        //findOverlaps(false);
        //findOverlaps(false);

        drawAllEdges();

      //  Path testRoute = findPathFromBuildings("ECOMPLEX", "86FIELD");
        Path testRoute = null;

        // Path Color Values
        //int rVal = 0;
        //int gVal = 0;
        //int bVal = 0;
        // Defaults
        String startLocation = "";
        String endDestination = "";
        //String pathColor = "Blue";
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
            testRoute = findPathFromBuildings(startLocation, endDestination);

            if (testRoute == null) {
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

            drawPath(testRoute, rVal, gVal, bVal, 10);
        }
        //  testRoute = findPath("dree56gxx48z", "dree56dsbvf5");
        //  drawPath(testRoute, 255, 0, 0, 10);

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

                // Output the data (not needed in final project)
          //      Log.d("MainActivity" ,"Point: " + tokens[0] + ", longitude: " + tokens[2] + ", latitude: "  + tokens[1]);


                //ID,LATITUDE,LONGITUDE,BUILDING NAME,DESCRIPTION,ISSTAIR,EDGES
                // Create the Point from the id and coordinates
                Point newPoint = new Point(tokens[0], Float.parseFloat(tokens[2]), Float.parseFloat(tokens[1]));

                //public Node(Point point, String description, boolean isStairs, boolean isRamp)
                // Create a node from this point along with description, isStairs, and isRamp information
                Node newNode = new Node(newPoint, tokens[3], tokens[4], Boolean.parseBoolean(tokens[5]));

                // Add this new node to the node map for later use
                nodeMap.addNode(newNode);

                String buildingName = newNode.getBuilding();
                if (!buildingName.equals("N/A")) {
                    Entrance newEntrance = new Entrance(buildingName, newNode);
                    buildingMap.addEntrance(newEntrance);
                }

               // mMap.addMarker()


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
        try {
            // Skip the first line which contains header information
            line = reader.readLine();
            // Loop through the rest of the csv file line by line
            while ((line = reader.readLine()) != null) {
                // Split the line into cells
                String[] tokens = line.split(",");

                //ID,LATITUDE,LONGITUDE,BUILDING NAME,DESCRIPTION,ISSTAIR,EDGES
                String[] connectedNodes = tokens[6].split(";");

                Node currentNode = nodeMap.getNode(tokens[0]);

                if (currentNode == null) {
                    System.out.println("Error: No node found for current node id: " + tokens[0]);
                    continue;
                }

              //  System.out.println("Connecting edges on current node id: " + currentNode.getId() );
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


                   // if (newEdge.getLength() > 0.05) {
                    //    System.out.println("Found very large edge: " + newEdge.getStartingNode().getId() + "+" + newEdge.getEndingNode().getId());
                  //      continue;
                  //  }


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

            if (startingNode.getIsStairs()) {

                mMap.addPolyline(new PolylineOptions()
                        .add(startCoord, endCoord)
                        .width(5)
                        .color(Color.RED));
            }
            else {

                mMap.addPolyline(new PolylineOptions()
                        .add(startCoord, endCoord)
                        .width(5)
                        .color(Color.BLACK));
            }
            String buildingName = startingNode.getBuilding();
            if (!buildingName.equals("N/A")) {

                LatLng buildingLocation = new LatLng(startingNode.getPoint().getLatitude(), startingNode.getPoint().getLongitude());
                mMap.addMarker(new MarkerOptions().position(buildingLocation).title(buildingName));

            }

       }
    }

    private Path findPathFromBuildings(String startBuilding, String endBuilding) {

        ArrayList<Entrance> startEntrances = buildingMap.getEntrances(startBuilding);
        ArrayList<Entrance> endEntrances = buildingMap.getEntrances(endBuilding);
        if(startEntrances.size() == 0) {
            System.out.println("ERROR: No entrances for building named: " + startBuilding);
            return null;
        }

        if(endEntrances.size() == 0) {
            System.out.println("ERROR: No entrances for building named: " + startBuilding);
            return null;
        }


        ArrayList<Path> possiblePaths = new ArrayList<>();
        Path finalPath = null;


        for (int i = 0; i < startEntrances.size(); i++) {

            Entrance nextStartEntrance = startEntrances.get(i);
            Node startNode = nextStartEntrance.getNode();

            for (int j = 0; j < endEntrances.size(); j++) {

                Entrance nextEndEntrance = endEntrances.get(j);
                Node endNode = nextEndEntrance.getNode();

                Path newPath = findPath(startNode.getId(), endNode.getId());
                possiblePaths.add(newPath);

            }
        }


        double maximumDistance = 1000000;


        for (int i = 0; i < possiblePaths.size(); i++) {

            Path nextPossiblePath = possiblePaths.get(i);

            if (nextPossiblePath.getDistance() < maximumDistance) {
                finalPath = nextPossiblePath;
                maximumDistance = nextPossiblePath.getDistance();
            }
        }




        if (finalPath == null) {
            System.out.println("No possible paths between " + startBuilding + " and " + endBuilding);
        }

        return finalPath;


    }


    private Path findPath(String startId, String endId) {


        NodeMap graph = new NodeMap(nodeMap);

        graph.resetDijkstra();


        Node currentNode = graph.getNode(startId);
        Path startPath = new Path(currentNode);


        currentNode.setPath(startPath);


        ArrayList<Edge> neighbors = currentNode.getEdges();
        ArrayList<String> visited = new ArrayList<>();
        while(neighbors.size() != 0) {
            Iterator edgeIterator = neighbors.iterator();
            Edge shortestEdge = null;

            double minDistance = 1000000;
            while (edgeIterator.hasNext()) {
                Edge nextEdge = (Edge) edgeIterator.next();
                double length = nextEdge.getLength();
                Node currentNeighbor = nextEdge.getStartingNode();
                if (currentNeighbor.getScore() != 1000000) {
                    System.out.println("TEST " + length);
                    length = length + currentNeighbor.getScore();
                    System.out.println("TEST2 " + length);
                }
                if (length < minDistance) {
                    minDistance = length;
                    shortestEdge = nextEdge;
                }
            }
            if (shortestEdge == null) {
                System.out.println("ERROR NO SHORTEST EDGE");
                return null;
            }
            neighbors.remove(shortestEdge);
            currentNode = graph.getNode(shortestEdge.getStartingNode().getId());
            Node closestNode = graph.getNode(shortestEdge.getEndingNode().getId());

            ArrayList<Edge> newEdges = closestNode.getEdges();
            Iterator newNeighborIterator = newEdges.iterator();
            while (newNeighborIterator.hasNext()) {
                Edge newNextEdge = (Edge)newNeighborIterator.next();
                Node newNextNode = graph.getNode(newNextEdge.getEndingNode().getId());
                if (!visited.contains(newNextNode.getId())) {
                    neighbors.add(newNextEdge);
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
            //    drawPath(newPath);
                System.out.println("Seting score of node: " + closestNode.getId() + " to: " + newPath.getDistance());
                closestNode.setScore(newPath.getDistance());
                closestNode.setPath(newPath);
            }
            if (closestNode.getId().equals(endId)) {

                return closestNode.getPath();
            }

            visited.add(currentNode.getId());

        }

        // Only called if no path is found, can occur if the end node is unreachable
        // or if the start node is the end node
        // or if the code fails to find a path
      //  System.out.println("PATH FINDER FAILED");
        Path returnPath = new Path(currentNode);
        return returnPath;
    }



    private void drawPath(Path path, int red, int green, int blue, int width) {

        Iterator nodeIterator = path.getPath().iterator();
        if (!nodeIterator.hasNext()) {
            // Path is empty...
            // This should never happen
            return;
        }

        // The very first node will be the start point
        Node startNode = (Node)nodeIterator.next();

        while(nodeIterator.hasNext()) {



            Node endNode = (Node)nodeIterator.next();

            float startLongitude = startNode.getPoint().getLongitude();
            float startLatitude = startNode.getPoint().getLatitude();
            LatLng startCoord = new LatLng(startLatitude, startLongitude);


            float endLongitude = endNode.getPoint().getLongitude();
            float endLatitude = endNode.getPoint().getLatitude();
            LatLng endCoord = new LatLng(endLatitude, endLongitude);

            mMap.addPolyline(new PolylineOptions()
                    .add(startCoord, endCoord)
                    .width(width)
                    .color(Color.rgb(red,green,blue)));

            // Move the start Node to the end Node.
            // This allows us to draw a continuous line
            startNode = endNode;
        }
    }


    private void findOverlaps(Boolean fix) {

        Iterator nodeIterator = nodeMap.getMap().entrySet().iterator();
        while(nodeIterator.hasNext()) {

            Map.Entry mapElement = (Map.Entry) nodeIterator.next();
            Node currentNode = (Node) mapElement.getValue();



            Iterator nodeIteratorInner = nodeMap.getMap().entrySet().iterator();
            while(nodeIteratorInner.hasNext()) {

                Map.Entry mapElementInner = (Map.Entry) nodeIteratorInner.next();
                Node loopNode = (Node) mapElementInner.getValue();
                if (currentNode.getId().equals(loopNode.getId())) {
                    continue;
                }

                Edge tempEdge = new Edge(currentNode, loopNode);
                // 0.00621371 = 10 meters in miles
                if (tempEdge.getLength() < 0.003 && !edgeMap.contains(tempEdge)) {
                    Path tempPath = new Path(currentNode);
                    tempPath.addNode(loopNode);
                  //  System.out.println("Very Close (" + tempEdge.getLength() + "m) " + currentNode.getId() + " and " + loopNode.getId());
                    if (!fix) {
                        drawPath(tempPath, 255, 0, 0, 20);
                    }
                    else {

                        nodeMap.removeNode(loopNode);

                        Iterator neighborIterator = loopNode.getEdges().iterator();
                        while(neighborIterator.hasNext()) {
                            Edge tempNeighborEdge = (Edge) neighborIterator.next();
                            edgeMap.removeEdge(tempNeighborEdge);

                            Node tempEndNode = tempNeighborEdge.getEndingNode();
                            Path oldEdgePath = new Path(tempNeighborEdge.getEndingNode());
                            oldEdgePath.addNode(loopNode);
                            drawPath(oldEdgePath, 255, 0, 0, 10);
                            tempEndNode.removeEdge(tempNeighborEdge);
                            Edge newEdge = new Edge(currentNode, tempEndNode);
                            Edge newEdgeRev = new Edge(tempEndNode, currentNode);
                            tempEndNode.addEdge(newEdgeRev);
                            currentNode.addEdge(newEdge);
                            nodeMap.removeNode(tempEndNode.getId());
                            nodeMap.addNode(tempEndNode);
                            edgeMap.addEdge(newEdge);
                        }

                    }
                }
            }

        }

    }


}
