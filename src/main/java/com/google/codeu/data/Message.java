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

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.validator.routines.UrlValidator;

/** A single message posted by a user. */
public class Message {

  private final String regex = "(https?://\\S+\\.(png|jpg))";
  private final String replacement = "<img src=\"$1\" />";
  private UUID id;
  private String user;
  private String text;
  private long timestamp;
  private String recipient;
  private String imageUrl;

  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text} content 
   * and recipient {@code recipient}. Generates a random ID and uses the current 
   * system time for the creation time.
   */
  public Message(String user, String text, String recipient) {
    this(UUID.randomUUID(), user, text, System.currentTimeMillis(), recipient);
  }

  public Message(UUID id, String user, String text, long timestamp, String recipient) {
    this.id = id;
    this.user = user;
    this.text = text;
    this.timestamp = timestamp;
    this.recipient = recipient;
  }

  public UUID getId() {
    return id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getUser() {
    return user;
  }

  public String getText() {
    return text;
  }

  public long getTimestamp() {
    return timestamp;
  }
  
  public String getRecipient() {
    return recipient;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  /**
   * Changes message text when the user is looking at messages.
   * Does not modified stored message in case future functionality
   * allows user to edit messages.
   */
  public void parseImageURLs() {
    // Initial setup with url validation and regex matching.
    String[] schemes = {"http, https"};
    StringBuffer sb = new StringBuffer();
    UrlValidator urlValidator = new UrlValidator(schemes);
    String match;
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(this.text);
    // Continously changes the text if valid url(s) was/were passed in.
    while (m.find()) {
      match = m.group();
      if (urlValidator.isValid(match)) {
        match = match.replaceAll(regex, replacement);
      }
      m.appendReplacement(sb, replacement);
    }
    m.appendTail(sb);
    this.text = sb.toString();
  }
}
