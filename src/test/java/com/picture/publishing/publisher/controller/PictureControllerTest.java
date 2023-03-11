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
import org.springframework.mock.web.MockMultipartFile;

import com.picture.publishing.publisher.dto.PictureDto;
import com.picture.publishing.publisher.dto.PictureListingDto;
import com.picture.publishing.publisher.model.PictureCategory;
import com.picture.publishing.publisher.model.PictureStatus;
import com.picture.publishing.publisher.service.PictureService;

@ExtendWith(MockitoExtension.class)
class PictureControllerTest {

	@Mock
	private PictureService pictureService;

	@Test
	void getAcceptedPictures_ValidData_ReturnsPictureListingDtoList() {
		List<PictureListingDto> photos = new ArrayList<>();
		when(pictureService.getAcceptedPictures()).thenReturn(photos);

		PictureController controller = new PictureController();
		controller.pictureService = pictureService;

		ResponseEntity<List<PictureListingDto>> response = controller.getAcceptedPictures();
		assertSame(HttpStatus.OK, response.getStatusCode());
		assertSame(photos, response.getBody());
	}

	@Test
	void submitPicture_ValidData_ReturnsPictureDto() throws IOException {
		String description = "Test picture";
		PictureCategory category = PictureCategory.MACHINE;
		MockMultipartFile file = new MockMultipartFile("data", "filename.png", "image/png",
				new byte[] { 0x01, 0x02, 0x03 });
		PictureDto expectedPictureDto = new PictureDto(1L, description, PictureStatus.PENDING);

		when(pictureService.submitPicture(description, category, file)).thenReturn(expectedPictureDto);
		PictureController controller = new PictureController();
		controller.pictureService = pictureService;

		ResponseEntity<PictureDto> response = controller.sumbitPicture(description, category, file);
		assertSame(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expectedPictureDto, response.getBody());
	}

}
