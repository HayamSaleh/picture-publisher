package com.picture.publishing.publisher.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "pictures")
public class Picture {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner;

	@Column
	private String name;
	@Column
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PictureCategory category = PictureCategory.LIVING_THING;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PictureStatus status;

	@Column
	private double width;

	@Column
	private double height;

	@Column
	private double size;

	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "picture_file")
	private byte[] pictureFile;

	@Column
	private String url;

	public Picture() {
		super();

	}

	public Picture(Long id, String name, PictureStatus status) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public void setHeight(double height) {
		this.height = height;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public byte[] getPictureFile() {
		return pictureFile;
	}

	public void setPictureFile(byte[] pictureFile) {
		this.pictureFile = pictureFile;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
