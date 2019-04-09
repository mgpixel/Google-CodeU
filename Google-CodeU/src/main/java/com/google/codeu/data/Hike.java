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

import java.util.Date;
import java.util.UUID;
import org.apache.commons.validator.routines.UrlValidator;

import com.google.cloud.datastore.LatLng;

/** A hike in the application */
public class Hike {
	
	private UUID id;
	private String organizer;
	private long startTime;
	private Date startDate;
	
	/**
	 * Constructs a new {@link Hike} created by {@code organizer} with a scheduled
	 * starting time {@code startTime} and date {@startDate}. Generates a random ID.
	 */
	public Hike(String organizer, long startTime, Date startDate) {
		this(UUID.randomUUID(), organizer, startTime, startDate);
		
	}
	
	public Hike(UUID id, String organizer, long startTime, Date startDate) {
		this.id = id;
		this.organizer = organizer;
		this.startTime = startTime;
		this.startDate = startDate;
	}
	
	public UUID getId() {
		return this.id;
	}
	
	public String getOrganizer() {
		return this.organizer;
	}
	
	public long getStartTime() {
		return this.startTime;
	}
	
	public Date getStartDate() {
		return this.startDate;
	}
	
	public void setStartTime(long newTime) {
		this.startTime = newTime;
	}
	
	public void setStartDate(Date newDate) {
		this.startDate = newDate;
	}
}
