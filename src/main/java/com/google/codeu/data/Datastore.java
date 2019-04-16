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
  private final int fetchLimit = 10000;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Checks if a user was already stored, done when a user logs in.
   * 
   *  @return a user entity if found, null otherwise.
   */
  private Entity findUser(String username) {
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("User", FilterOperator.EQUAL, username));
    PreparedQuery results = datastore.prepare(query);
    return results.asSingleEntity();
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    // sets the fields and their values in our datastore
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    messageEntity.setProperty("recipient", message.getRecipient());
    messageEntity.setProperty("imageUrl", message.getImageUrl());
    datastore.put(messageEntity);
  }

  /**
   * Either stores the user in datastore, or updates the messagesSent property
   * when a message is being stored.
   */
  public void storeUser(String username, int updateMessagesSent) {
    Entity userEntity = new Entity("User", username);
    Entity storedUserEntity = findUser(username);
    // Doesn't override previous count by getting previous count first.
    if (storedUserEntity != null) {
      updateMessagesSent += (long) storedUserEntity.getProperty("messagesSent");
    }
    userEntity.setProperty("messagesSent", updateMessagesSent);
    datastore.put(userEntity);
  }

  /**
   * Gets average message length.
   * 
   * @return average length of messages sent.
   */
  public int getAverageMessageLength() {
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    int numMessages = results.countEntities(FetchOptions.Builder.withLimit(fetchLimit));
    // Use double for calculations and cast to int when returning.
    double averageLength = 0;
    if (numMessages == 0) {
      return 0;
    }
    for (Entity messageEntity : results.asIterable()) {
      String text = (String) messageEntity.getProperty("text");
      averageLength += (double) text.length() / numMessages;
    }
    return (int) averageLength;
  }

  /**
   * Gets most active user based on number of messages sent.
   * 
   * @return most active user, first user if no messages sent in system/ties or a
   *         message saying there are no users.
   */
  public String getMostActiveUser() {
    Query query = new Query("User").addSort("messagesSent", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asIterator().next();
    if (userEntity == null) {
      return "No users in the system";
    }
    return userEntity.getKey().getName();
  }

  /**
   * Gets number of messages a user has sent.
   * 
   * @return number of messages sent by user, with a cap of 10000. Can be 0.
   */
  public int getNumMessagesUserSent(String sender) {
    Query query = new Query("Message").setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, sender));
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(fetchLimit));
  }

  /**
   * Gets total number of messages in system. Note: since countEntities() is
   * deprecated, setting an arbitrary limit to every getter using it. May cause an
   * issue with getAverageMessageLength if there are more messages than the limit
   * specified, keep in mind.
   * 
   * @return number of messages, with a cap of 10000 if length is bigger.
   */
  public int getTotalMessageCount() {
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(fetchLimit));
  }

  /**
   * Gets total number of users in system.
   * 
   * @return number of users in system, with a cap of 1000 if length is bigger.
   *         Can be 0 if there no one has logged in.
   */
  public int getTotalUserCount() {
    Query query = new Query("User");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(fetchLimit));
  }
  
  /**
   * Gets all the messages that exist in the datastore,
   * regardless of sender or recipient.
   *  
   * @return a list of messages that exists in the datastore/app, or empty list if
   * there are no messages in the datastore/app. List is sorted by time descending.
   */
  public List<Message> getAllMessages() {
	  // call the getMessagesForRecipient method,
	  // but setting the recipient to null
	  return getMessagesForRecipient(null);
  }

  /**
   * Gets the message sent by the user.
   * 
   * @return a list of message send by the sender, or empty list if the sender hasn't 
   * 	sent a message. List is sorted by time descending.
   */
  public List<Message> getMessagesBySender(String sender) {
    List<Message> messages = new ArrayList<>();

    Query query = new Query("Message").setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, sender))
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
        // if there's no url, it'll still be set to null
        message.setImageUrl((String) entity.getProperty("imageUrl"));
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
   * @return a list of messages received by the recipient, or empty list if the
   *         recipient hasn't received message. List is sorted by time descending.
   */
  public List<Message> getMessagesForRecipient(String recipient) {
    List<Message> messages = new ArrayList<>();
    
    Query query;
    if (recipient != null) {
    	// recipient was specified
        query = new Query("Message")
                    .setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
                    .addSort("timestamp", SortDirection.DESCENDING);
    } else {
    	// recipient was not specified, return all messages in datastore
    	query = new Query("Message")
    				.addSort("timestamp", SortDirection.DESCENDING);
    }

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
        message.parseImageURLs();
        message.setImageUrl((String) entity.getProperty("imageUrl"));
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
