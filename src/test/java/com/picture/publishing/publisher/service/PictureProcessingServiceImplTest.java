package com.picture.publishing.publisher.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.picture.publishing.publisher.controller.exception.NotAllowedRequestException;
import com.picture.publishing.publisher.controller.exception.PictureNotFoundException;
import com.picture.publishing.publisher.dto.PictureDto;
import com.picture.publishing.publisher.model.Picture;
import com.picture.publishing.publisher.model.PictureStatus;
import com.picture.publishing.publisher.repository.PictureRepository;
import com.picture.publishing.publisher.service.UserService;
import com.picture.publishing.publisher.service.impl.PictureProcessingServiceImpl;
import com.picture.publishing.publisher.utils.MapperUtils;

@ExtendWith(MockitoExtension.class)
class PictureProcessingServiceImplTest {
	public static final long PIC2_ID = 2L;
	public static final long PIC1_ID = 1L;
	public static final String PIC2_DESC = "picture2";
	public static final String PIC1_DESC = "picture1";
	private static final String ADMIN_USERNAME = "admin";

	@Mock
	private DozerBeanMapper dozerMapper;

	@Mock
	private MapperUtils mapperUtils;

	@Mock
	private UserService userService;

	@Mock
	private PictureRepository pictureRepository;

	@InjectMocks
	private PictureProcessingServiceImpl pictureProcessingService = new PictureProcessingServiceImpl();

	private static List<Picture> generatePictureListWithPictureStatus(PictureStatus pictureStatus) {
		return Arrays.asList(new Picture(PIC1_ID, PIC1_DESC, pictureStatus),
				new Picture(PIC2_ID, PIC2_DESC, pictureStatus));
	}

	private static List<PictureDto> generatePictureDtoListWithPictureStatus(PictureStatus pictureStatus) {
		return Arrays.asList(new PictureDto(PIC1_ID, PIC1_DESC, pictureStatus),
				new PictureDto(PIC2_ID, PIC2_DESC, pictureStatus));
	}

	@Test
	void getAllPendingPictures_NonEmptyData_ReturnsPendingPictureListDto() {
		List<Picture> pictures = generatePictureListWithPictureStatus(PictureStatus.PENDING);
		List<PictureDto> expectedPictureDtos = generatePictureDtoListWithPictureStatus(PictureStatus.PENDING);

		doReturn(pictures).when(pictureRepository).findByStatus(PictureStatus.PENDING);
		doReturn(expectedPictureDtos).when(mapperUtils).mapList(pictures, PictureDto.class);

		List<PictureDto> actualPictureDtos = pictureProcessingService.getPendingPictures();

		assertPictureList(pictures, expectedPictureDtos, actualPictureDtos, PictureStatus.PENDING);

	}

	@Test
	void getAllRejectedPictures_NonEmptyData_ReturnsRejectedPictureListDto() {
		List<Picture> pictures = generatePictureListWithPictureStatus(PictureStatus.REJECTED);
		List<PictureDto> expectedPictureDtos = generatePictureDtoListWithPictureStatus(PictureStatus.REJECTED);

		doReturn(pictures).when(pictureRepository).findByStatus(PictureStatus.REJECTED);
		doReturn(expectedPictureDtos).when(mapperUtils).mapList(pictures, PictureDto.class);

		List<PictureDto> actualPictureDtos = pictureProcessingService.getRejectedPictures();

		assertPictureList(pictures, expectedPictureDtos, actualPictureDtos, PictureStatus.REJECTED);
	}

	private void assertPictureList(List<Picture> pictures, List<PictureDto> expectedPictureDtos,
			List<PictureDto> actualPictureDtos, PictureStatus pictureStatus) {
		assertEquals(expectedPictureDtos, actualPictureDtos);
		Mockito.verify(pictureRepository).findByStatus(pictureStatus);
		Mockito.verify(mapperUtils).mapList(pictures, PictureDto.class);
		assertEquals(pictures.size(), expectedPictureDtos.size());
	}

	@Test
	void getPictureById_ValidId_ReturnsPictureDto() {
		long id = 1L;
		Picture picture = new Picture(PIC1_ID, PIC1_DESC, PictureStatus.ACCEPTED);
		PictureDto pictureDto = new PictureDto(PIC1_ID, PIC1_DESC, PictureStatus.ACCEPTED);
		doReturn(Optional.of(picture)).when(pictureRepository).findById(id);
		doReturn(pictureDto).when(dozerMapper).map(picture, PictureDto.class);

		assertEquals(pictureDto, pictureProcessingService.getPictureById(id),
				"Expected and actual picture objects should match");

		verify(pictureRepository).findById(id);
		verify(dozerMapper).map(picture, PictureDto.class);
	}

	@Test
	void getPictureById_InvalidId_ThrowPictureNotFoundExceptionException() {
		Long id = 1L;
		doReturn(Optional.empty()).when(pictureRepository).findById(id);

		assertThatThrownBy(() -> pictureProcessingService.getPictureById(id))
				.isInstanceOf(PictureNotFoundException.class);
	}

	@Test
	void acceptPictureById_ValidId_ReturnsPictureDto() throws IOException {
		Long pictureId = 1L;
		Picture picture = new Picture(PIC1_ID, PIC1_DESC, PictureStatus.PENDING);
		PictureDto pictureDto = new PictureDto(PIC1_ID, PIC1_DESC, PictureStatus.ACCEPTED);

		picture.setPictureFile(new byte[] {});
		Mockito.when(pictureRepository.findById(pictureId)).thenReturn(Optional.of(picture));
		Mockito.when(userService.getCurrentUserName()).thenReturn(ADMIN_USERNAME);
		Mockito.when(pictureRepository.save(Mockito.any(Picture.class))).then(AdditionalAnswers.returnsFirstArg());
		doReturn(pictureDto).when(dozerMapper).map(picture, PictureDto.class);

		PictureDto result = pictureProcessingService.acceptPicture(pictureId);

		assertPictureMetadata(pictureId, picture, result, PictureStatus.ACCEPTED);

		assertNotNull(picture.getUrl());
		assertEquals(picture.getDescription(), result.getDescription());
	}

	@Test
	void acceptPictureById_InvalidId_ThrowPictureNotFoundExceptionException() throws IOException {
		Long id = 1L;

		doReturn(Optional.empty()).when(pictureRepository).findById(id);

		assertThatThrownBy(() -> pictureProcessingService.acceptPicture(id))
				.isInstanceOf(PictureNotFoundException.class);
	}

	@Test
	void acceptPictureById_AlreadyProcessedPicture_ThrowNotAllowedRequestException() throws IOException {
		Long pictureId = 1L;
		Picture picture = new Picture();
		picture.setStatus(PictureStatus.ACCEPTED);
		Mockito.when(pictureRepository.findById(pictureId)).thenReturn(Optional.of(picture));

		assertThatThrownBy(() -> pictureProcessingService.acceptPicture(pictureId))
				.isInstanceOf(NotAllowedRequestException.class);
	}

	@Test
	void rejectPictureById_ValidId_ReturnsPictureDtoWithGeneratedUrl() throws IOException {
		Long pictureId = 1L;
		Picture picture = new Picture(PIC1_ID, PIC1_DESC, PictureStatus.PENDING);
		PictureDto pictureDto = new PictureDto(PIC1_ID, PIC1_DESC, PictureStatus.REJECTED);
		picture.setPictureFile(new byte[] {});
		Mockito.when(pictureRepository.findById(pictureId)).thenReturn(Optional.of(picture));
		Mockito.when(userService.getCurrentUserName()).thenReturn(ADMIN_USERNAME);
		Mockito.when(pictureRepository.save(Mockito.any(Picture.class))).then(AdditionalAnswers.returnsFirstArg());
		doReturn(pictureDto).when(dozerMapper).map(picture, PictureDto.class);

		PictureDto result = pictureProcessingService.rejectPicture(pictureId);

		assertPictureMetadata(pictureId, picture, result, PictureStatus.REJECTED);
		assertNull(picture.getPictureFile());
	}

	private void assertPictureMetadata(Long pictureId, Picture picture, PictureDto result,
			PictureStatus pictureStatus) {
		Mockito.verify(pictureRepository).findById(pictureId);
		Mockito.verify(pictureRepository).save(picture);
		assertEquals(pictureStatus, result.getStatus());
	}

	@Test
	void rejectPictureById_AlreadyProcessedPicture_ThrowNotAllowedRequestException() throws IOException {
		Long pictureId = 1L;
		Picture picture = new Picture();
		picture.setStatus(PictureStatus.REJECTED);
		Mockito.when(pictureRepository.findById(pictureId)).thenReturn(Optional.of(picture));

		assertThatThrownBy(() -> pictureProcessingService.rejectPicture(pictureId))
				.isInstanceOf(NotAllowedRequestException.class);
	}

}
