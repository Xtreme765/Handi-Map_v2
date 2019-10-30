package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private NodeMap nodeMap;
    private EdgeMap edgeMap;

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
    }

    /** Called when the user taps the Settings button */
    public void settingsMessage(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Sully Was here
    // Virginia is for lovers
    // Darrian was not here
    // What's up it's Mao
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng rpi_union = new LatLng(42.730192, -73.676731);
        mMap.addMarker(new MarkerOptions().position(rpi_union).title("Marker in Troy"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rpi_union));
        mMap.setMinZoomPreference(17);

        // Reads through the database and creates all nodes
        createNodes();

        // Reads through the database and creates all edges
        createEdges();

        // Draws all edges present in the database
        // Might not be included in the final version but serves to visualize the database
        drawAllEdges();
    }


    // Reads through the database and creates all nodes
    private void createNodes() {
        // Open the resource file stored in /raw/____.csv
        InputStream is = getResources().openRawResource(R.raw.doc);
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
                Log.d("MainActivity" ,"Point: " + tokens[0] + ", longitude: " + tokens[1] + ", latitude: "  + tokens[2]);

                // Create the Point from the id and coordinates
                Point newPoint = new Point(tokens[0], Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));

                // Create a node from this point along with description, isStairs, and isRamp information
                Node newNode = new Node(newPoint, tokens[3], Boolean.parseBoolean(tokens[4]), Boolean.parseBoolean(tokens[5]));

                // Add this new node to the node map for later use
                nodeMap.addNode(newNode);
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
        InputStream is = getResources().openRawResource(R.raw.doc);
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

                String[] connectedNodes = tokens[6].split(":");

                Node currentNode = nodeMap.getNode(tokens[0]);

                if (currentNode == null) {
                    System.out.println("Error: No node found for current node id: " + tokens[0]);
                    continue;
                }

                System.out.println("Connecting edges on current node id: " + currentNode.getId() );
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

        Hashtable<String, Edge> edgeHashMap = edgeMap.getEdgeMap();
        Iterator edgeIterator = edgeHashMap.entrySet().iterator();
        while(edgeIterator.hasNext()) {

            Map.Entry mapElement = (Map.Entry)edgeIterator.next();
            Edge currentEdge = (Edge)mapElement.getValue();

            float startLongitude = currentEdge.getStartingNode().getPoint().getLongitude();
            float startLatitude = currentEdge.getStartingNode().getPoint().getLatitude();
            LatLng startCoord = new LatLng(startLatitude, startLongitude);

            float endLongitude = currentEdge.getEndingNode().getPoint().getLongitude();
            float endLatitude = currentEdge.getEndingNode().getPoint().getLatitude();
            LatLng endCoord = new LatLng(endLatitude, endLongitude);

            mMap.addPolyline(new PolylineOptions()
                    .add(startCoord, endCoord)
                    .width(5)
                    .color(Color.BLACK));
       }
    }



}
