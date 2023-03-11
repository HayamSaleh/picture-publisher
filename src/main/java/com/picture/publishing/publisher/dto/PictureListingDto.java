package com.picture.publishing.publisher.dto;

import com.picture.publishing.publisher.model.PictureCategory;

public class PictureListingDto {
	private String name;
	private String description;
	private PictureCategory category;
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PictureCategory getCategory() {
		return category;
	}

	public void setCategory(PictureCategory category) {
		this.category = category;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}