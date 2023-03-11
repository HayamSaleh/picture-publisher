package com.picture.publishing.publisher.dto;

import com.picture.publishing.publisher.model.PictureStatus;

public class PictureDto extends PictureListingDto {
	private Long id;
	private PictureStatus status = PictureStatus.PENDING;
	private double width;
	private double height;
	private double size;

	public PictureDto() {
		super();
	}

	public PictureDto(Long id, String description, PictureStatus status) {
		super();
		this.id = id;
		this.setDescription(description);
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PictureStatus getStatus() {
		return status;
	}

	public void setStatus(PictureStatus status) {
		this.status = status;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double d) {
		this.height = d;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

}