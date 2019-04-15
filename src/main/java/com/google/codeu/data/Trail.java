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
import org.apache.commons.validator.routines.UrlValidator;

import com.google.cloud.datastore.LatLng;

/** A trail in the application */
public class Trail {
	
	private UUID id;
	private String trailName;
	private String cityName;
	private LatLng start;
	private LatLng end;
	
	/**
	 * Constructs a new {@link Trail} with starting lat/lon coordinates {@code start}
	 * and ending lat/lon coordinates {@code end}. Generates a random ID. 
	 */
	public Trail(String trailName, String cityName, LatLng start, LatLng end) {
		this(UUID.randomUUID(), trailName, cityName, start, end);
	}
	
	public Trail(UUID id, String trailName, String cityName, LatLng start, LatLng end) {
		this.id = id;
		this.trailName = trailName;
		this.start = start;
		this.end = end;
	}
	

	public UUID getId() {
		return id;
	}
	
	public String getTrailName() {
		return this.trailName;
	}
	
	public String getCityName() {
		return this.cityName;
	}
	
	public LatLng getStart() {
		return this.start;
	}
	
	public LatLng getEnd() {
		return this.end;
	}
}
