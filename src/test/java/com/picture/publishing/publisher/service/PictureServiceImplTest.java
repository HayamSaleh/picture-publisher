package com.picture.publishing.publisher.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.awt.Dimension;
import java.io.IOException;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.picture.publishing.publisher.controller.exception.NotAllowedRequestException;
import com.picture.publishing.publisher.dto.PictureDto;
import com.picture.publishing.publisher.model.Picture;
import com.picture.publishing.publisher.model.PictureCategory;
import com.picture.publishing.publisher.model.PictureStatus;
import com.picture.publishing.publisher.model.User;
import com.picture.publishing.publisher.repository.PictureRepository;
import com.picture.publishing.publisher.service.UserService;
import com.picture.publishing.publisher.service.impl.PictureServiceImpl;
import com.picture.publishing.publisher.utils.MapperUtils;
import com.picture.publishing.publisher.utils.PictureUtils;

@ExtendWith(MockitoExtension.class)
class PictureServiceImplTest {

	@Mock
	private DozerBeanMapper dozerMapper;

	@Mock
	private MapperUtils mapperUtils;

	@Mock
	private PictureUtils pictureUtils;

	@Mock
	private UserService userService;

	@Mock
	private PictureRepository pictureRepository;

	@InjectMocks
	private PictureServiceImpl pictureService = new PictureServiceImpl();

	@Test
	void submitPicture_ValidInputs_ReturnsPictureDto() throws IOException {
		String username = "testUser";
		String description = "Test picture";
		PictureCategory category = PictureCategory.MACHINE;
		MockMultipartFile file = new MockMultipartFile("data", "filename.png", "image/png",
				new byte[] { 0x01, 0x02, 0x03 });

		PictureDto expectedPictureDto = new PictureDto(1L, description, PictureStatus.PENDING);
		expectedPictureDto.setCategory(category);
		Picture savedPicture = new Picture(1L, description, PictureStatus.PENDING);
		savedPicture.setCategory(category);

		doReturn(new User()).when(userService).findUserByUsername(username);
		doReturn(username).when(userService).getCurrentUserName();
		doReturn(new Dimension()).when(pictureUtils).getImageDimensions(any());
		doReturn(expectedPictureDto).when(dozerMapper).map(savedPicture, PictureDto.class);
		doReturn(savedPicture).when(pictureRepository).save(any(Picture.class));

		PictureDto result = pictureService.submitPicture(description, category, file);

		assertNotNull(result);
		assertEquals(description, result.getDescription());
		assertEquals(category, result.getCategory());
	}

	@Test
	void submitPicture_InvalidPictureType_ThrowException() throws IOException {
		MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "txt/txt",
				new byte[] { 0x01, 0x02, 0x03 });

		assertThatThrownBy(() -> pictureService.submitPicture(null, null, file))
				.isInstanceOf(NotAllowedRequestException.class);
	}

}