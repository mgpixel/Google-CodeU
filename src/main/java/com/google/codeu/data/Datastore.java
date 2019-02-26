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

package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  /** 
   *  Checks if a user was already stored, done when a user logs in.
   * 
   *  @return true if a user in Datastore, false otherwise.
   */
  private boolean userFound(String user) {
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("User", FilterOperator.EQUAL, user));
    PreparedQuery results = datastore.prepare(query);
    return results.asSingleEntity() == null ? false : true;
  }

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    // sets the fields and their values in our datastore
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    messageEntity.setProperty("recipient", message.getRecipient());

    datastore.put(messageEntity);
  }

  /** Stores the user in Datastore (if not already in it) */
  public void storeUser(String user) {
    Entity userEntity = new Entity("User", user);
    if (!userFound(user)) {
      datastore.put(userEntity);
    }
  }

  /** 
   * Gets total number of messages in system.
   * 
   * @return number of messages, with a cap of 5000 if length is bigger.
   */
  public int getTotalMessageCount(){
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(5000));
  }

  /**
   * Gets total number of users in system.
   * 
   * @return number of users in system, with a cap of 1000 if length is bigger.
   */
  public int getTotalUserCount(){
    Query query = new Query("User");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  /**
   * Gets the message sent by the user.
   * 
   * @return a lit of message send by the sender, or empty list if the sender hasn't 
   * 	sent a message. List is sorted by time descending.
   */
  public List<Message> getMessagesBySender(String sender) {
	  List<Message> messages = new ArrayList<>();
	  
	  Query query =
		  new Query("Message")
	          .setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, sender))
	          .addSort("timestamp", SortDirection.DESCENDING);
	  PreparedQuery results = datastore.prepare(query);

	  for (Entity entity : results.asIterable()) {
		  try {
			  // get the fields for each message
			  // (id, user who received, time stamp, text of message)
			  String idString = entity.getKey().getName();
	          UUID id = UUID.fromString(idString);
	          String recipient = (String) entity.getProperty("recipient"); 
	          String text = (String) entity.getProperty("text");
	          long timestamp = (long) entity.getProperty("timestamp");

	          Message message = new Message(id, sender, text, timestamp, recipient);
	          messages.add(message);
	       } catch (Exception e) {
	         System.err.println("Error reading message.");
	         System.err.println(entity.toString());
	         e.printStackTrace();
	       }
	   }
	   return messages;
  }
  
  /**
   * Gets messages received by the user. 
   *
   * @return a list of messages received by the recipient, or empty list if the recipient
   * 	hasn't received message. List is sorted by time descending. 
   */
  public List<Message> getMessagesForRecipient(String recipient) {
    List<Message> messages = new ArrayList<>();

    Query query =
        new Query("Message")
            .setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
    	// get the fields for each message
    	// (id, user who sent message, time stamp, text of message)
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String sender = (String) entity.getProperty("user"); 
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");

        Message message = new Message(id, sender, text, timestamp, recipient);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return messages;
  }
}
