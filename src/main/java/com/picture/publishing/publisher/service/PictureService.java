package com.picture.publishing.publisher.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.picture.publishing.publisher.dto.PictureDto;
import com.picture.publishing.publisher.dto.PictureListingDto;
import com.picture.publishing.publisher.model.PictureCategory;

public interface PictureService {
	static final String UPLOAD_ROOT = "images";

	PictureDto submitPicture(String description, PictureCategory category, MultipartFile attachment) throws IOException;

	List<PictureListingDto> getAcceptedPictures();

}
