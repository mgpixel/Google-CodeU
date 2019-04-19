/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.data.Trail;
import com.google.codeu.data.UserMarker;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Handles fetching and saving {@link Trail} instances. */
@WebServlet("/new-trails")
public class NewTrailServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with a JSON representation of {@link Trail} data for a specific trail. Responds with
   * an empty array if the trail is not provided. If there are multiple trails with the same
   * name, returns all of them.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");
    String trail = request.getParameter("trail");
    
    if (trail == null || trail.equals("")) {
    	// request is invalid, return empty array
    	response.getWriter().println("[]");
    	return;
    }
    
    List<Trail> trails = datastore.getTrailsByName(trail);
    Gson gson = new Gson();
    String json = gson.toJson(trails);
    response.getWriter().println(json);
  }

  /** Stores a new {@link Trail}. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }
    
    String trailName = request.getParameter("trailName");
    String state = request.getParameter("state");
    String city = request.getParameter("city");
    double startLat = Double.parseDouble(request.getParameter("startLat"));
    double startLon = Double.parseDouble(request.getParameter("startLon"));

    Trail trail = new Trail(trailName, state, city, startLat, startLon);
    datastore.storeTrail(trail);
    
    UserMarker marker = new UserMarker(startLat, startLon, trailName);
    datastore.storeMarker(marker);
    response.sendRedirect("/hikesLocation.html");
  }
}
