package com.google.codeu.servlets;

import java.io.IOException;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.LatLng;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Trail;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * Returns UFO data as a JSON array, e.g. [{"lat": 38.4404675, "lng": -122.7144313}]
 */
@WebServlet("/MapsInfo")
public class MapsInfoServlet extends HttpServlet {

    JsonArray ufoSightingArray;
    
	private Datastore datastore;

    @Override
    public void init() {
        ufoSightingArray = new JsonArray();
        datastore = new Datastore();
        Gson gson = new Gson();
        Scanner scanner = 
        		new Scanner(getServletContext().getResourceAsStream("/WEB-INF/Hikes_Locations_Names.csv"));
        // populate array for google maps markers
        // add to datastore
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] cells = line.split(",");

            double lat = Double.parseDouble(cells[0]);
            double lng = Double.parseDouble(cells[1]);
            String hikeName = cells[2].trim();
            String cityName = cells[3].trim();
            String stateName = cells[4].trim().substring(0, 2);
            
            Trail trail = new Trail(hikeName, stateName, cityName, lat, lng);
            datastore.storeTrail(trail);
            ufoSightingArray.add(gson.toJsonTree(new UfoSighting("", lat, lng)));
        }
        scanner.close();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getOutputStream().println(ufoSightingArray.toString());
    }

    private static class UfoSighting{
        String state;
        double lat;
        double lng;

        private UfoSighting(String state, double lat, double lng) {
            this.state = state;
            this.lat = lat;
            this.lng = lng;
        }
    }
}