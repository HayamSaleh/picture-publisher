package com.picture.publishing.publisher.service;

import java.io.IOException;
import java.util.List;

import com.picture.publishing.publisher.dto.PictureDto;

public interface PictureProcessingService {

	PictureDto getPictureById(Long id);

	List<PictureDto> getPendingPictures();

	PictureDto acceptPicture(Long pictureId) throws IOException;

	PictureDto rejectPicture(Long pictureId);

	List<PictureDto> getRejectedPictures();

}
