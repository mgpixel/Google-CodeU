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

import java.util.List;

/**
 * Data class for a user profile in our app to show on their home page.
 */
public class UserProfile {
  private long messagesSent;
  private List<String> favoritedHikes;
  private List<String> createdHikes;
  private List<String> hikesPlanned;
  private String avatarUrl;
  private String username;

  // Only have a constructor with these 3 parameters, others can be null.
  public UserProfile(String username, String avatarUrl, long messagesSent) {
    this.username = username;
    this.avatarUrl = avatarUrl;
    this.messagesSent = messagesSent;
  }

  public void setHikes(List<String> favorited, List<String> created, List<String> planned) {
    this.favoritedHikes = favorited;
    this.createdHikes = created;
    this.hikesPlanned = planned;
  }

  public String getAvatarUrl() {
    return this.avatarUrl;
  }

  public String getUsername() {
    return this.username;
  }

  public List<String> getCreatedHikes() {
    return this.createdHikes;
  }

  public List<String> getFavoritedHikes() {
    return this.favoritedHikes;
  }

  public List<String> getHikesPlanned() {
    return this.hikesPlanned;
  }

  public long getMessagesSent() {
    return this.messagesSent;
  }
}