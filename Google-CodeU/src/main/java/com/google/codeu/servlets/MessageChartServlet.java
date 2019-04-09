package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;

/**
 * Sends the requested data to render a chart 
 * displaying the message data.
 */
@WebServlet("/messageschart")
public class MessageChartServlet extends HttpServlet {

	private Datastore datastore;

	  @Override
	  public void init() {
	    datastore = new Datastore();
	  }

	  @Override
	  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		  response.setContentType("application/json");
		  
		  // get the messages stored in datastore
		  List<Message> msgList = datastore.getAllMessages();
		  
		  // store data as json
		  Gson gson = new Gson();
		  String json = gson.toJson(msgList);
		  
		  // print json data
		  response.getWriter().println(json);
	  }
}
