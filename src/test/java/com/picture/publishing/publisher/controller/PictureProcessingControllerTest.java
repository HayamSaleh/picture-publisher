package com.picture.publishing.publisher.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.picture.publishing.publisher.dto.PictureDto;
import com.picture.publishing.publisher.service.PictureProcessingService;

@ExtendWith(MockitoExtension.class)
class PictureProcessingControllerTest {

	@Mock
	private PictureProcessingService PictureProcessingService;

	@Test
	void getPendingPictures_ValidData_ReturnsPictureDtoList() {
		List<PictureDto> photos = new ArrayList<>();
		when(PictureProcessingService.getPendingPictures()).thenReturn(photos);

		PictureProcessingController controller = new PictureProcessingController();
		controller.pictureProcessingService = PictureProcessingService;

		ResponseEntity<List<PictureDto>> response = controller.getPendingPictures();
		assertSame(HttpStatus.OK, response.getStatusCode());
		assertSame(photos, response.getBody());
	}

	@Test
	void getRejectedPictures_ValidData_ReturnsPictureDtoList() {
		List<PictureDto> photos = new ArrayList<>();
		when(PictureProcessingService.getRejectedPictures()).thenReturn(photos);

		PictureProcessingController controller = new PictureProcessingController();
		controller.pictureProcessingService = PictureProcessingService;

		ResponseEntity<List<PictureDto>> response = controller.getRejectedPictures();
		assertSame(HttpStatus.OK, response.getStatusCode());
		assertSame(photos, response.getBody());
	}

	@Test
	void acceptPicture_ValidPictureId_ReturnsAcceptedPictureDto() throws IOException {
		Long id = 1L;
		PictureDto expectedPictureDto = new PictureDto();
		when(PictureProcessingService.acceptPicture(id)).thenReturn(expectedPictureDto);

		PictureProcessingController controller = new PictureProcessingController();
		controller.pictureProcessingService = PictureProcessingService;
		ResponseEntity<PictureDto> response = controller.acceptPicture(id);
		assertSame(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedPictureDto, response.getBody());
	}

	@Test
	void rejectPicture_ValidPictureId_ReturnsRejectedPictureDto() throws IOException {
		Long id = 1L;
		PictureDto expectedPictureDto = new PictureDto();
		when(PictureProcessingService.rejectPicture(id)).thenReturn(expectedPictureDto);

		PictureProcessingController controller = new PictureProcessingController();
		controller.pictureProcessingService = PictureProcessingService;
		ResponseEntity<PictureDto> response = controller.rejectPicture(id);
		assertSame(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedPictureDto, response.getBody());
	}

}
