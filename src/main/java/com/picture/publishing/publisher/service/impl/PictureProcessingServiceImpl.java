package com.picture.publishing.publisher.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picture.publishing.publisher.controller.exception.NotAllowedRequestException;
import com.picture.publishing.publisher.controller.exception.PictureNotFoundException;
import com.picture.publishing.publisher.dto.PictureDto;
import com.picture.publishing.publisher.model.Picture;
import com.picture.publishing.publisher.model.PictureStatus;
import com.picture.publishing.publisher.repository.PictureRepository;
import com.picture.publishing.publisher.service.PictureProcessingService;
import com.picture.publishing.publisher.service.PictureService;
import com.picture.publishing.publisher.service.UserService;
import com.picture.publishing.publisher.utils.MapperUtils;

@Service
public class PictureProcessingServiceImpl implements PictureProcessingService {

	private final Logger logger = LoggerFactory.getLogger(PictureProcessingServiceImpl.class);

	private static final String PICTURE_ALREADY_PROCESSED_STATUS_MESSAGE = "Picture has already been %s before";
	private static final Random RANDOM = new Random();

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private MapperUtils mapperUtils;

	@Autowired
	private UserService userService;

	@Autowired
	private PictureRepository pictureRepository;

	@Override
	public List<PictureDto> getPendingPictures() {
		return getPicturesByStatus(PictureStatus.PENDING);
	}

	@Override
	public List<PictureDto> getRejectedPictures() {
		return getPicturesByStatus(PictureStatus.REJECTED);
	}

	@Override
	public PictureDto getPictureById(Long id) {
		return mapper.map(findPictureById(id), PictureDto.class);
	}

	@Override
	public PictureDto acceptPicture(Long pictureId) throws IOException {
		Picture picture = findPictureById(pictureId);
		PictureStatus status = picture.getStatus();
		if (status.equals(PictureStatus.PENDING)) {
			picture.setStatus(PictureStatus.ACCEPTED);
			String generateUrl = generateUrl(picture);
			picture.setUrl(generateUrl);
			Picture savedPicture = pictureRepository.save(picture);

			logger.info("Admin approved picture: {adminName = {}, PicturePath: {}}", userService.getCurrentUserName(),
					generateUrl);
			return mapper.map(savedPicture, PictureDto.class);
		}

		throw new NotAllowedRequestException(String.format(PICTURE_ALREADY_PROCESSED_STATUS_MESSAGE, status));
	}

	@Override
	public PictureDto rejectPicture(Long pictureId) {
		Picture picture = findPictureById(pictureId);
		PictureStatus status = picture.getStatus();
		if (status.equals(PictureStatus.PENDING)) {
			picture.setStatus(PictureStatus.REJECTED);
			picture.setPictureFile(null);
			Picture savedPicture = pictureRepository.save(picture);

			logger.info("Admin rejected picture: {adminName = {}, PictureName: {}}", userService.getCurrentUserName(),
					savedPicture.getName());
			return mapper.map(savedPicture, PictureDto.class);
		}

		throw new NotAllowedRequestException(String.format(PICTURE_ALREADY_PROCESSED_STATUS_MESSAGE, status));
	}

	private List<PictureDto> getPicturesByStatus(PictureStatus status) {
		List<Picture> pictures = pictureRepository.findByStatus(status);
		return mapperUtils.mapList(pictures, PictureDto.class);
	}

	private Picture findPictureById(Long pictureId) {
		return pictureRepository.findById(pictureId).orElseThrow(() -> new PictureNotFoundException(pictureId));
	}

	private int generateRandomFiveDigitsNumber() {
		return RANDOM.nextInt(90000) + 10000;
	}

	private String generateUrl(Picture picture) throws IOException {
		String imagePath = generateRandomFiveDigitsNumber() + "-" + picture.getName();
		Path filePath = Paths.get(PictureService.UPLOAD_ROOT, imagePath);
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(picture.getPictureFile())) {
			Files.copy(inputStream, filePath);
		} catch (IOException e) {
			logger.error("Error while generating URL for picture: {}", picture.getName());
			throw e;
		}

		return imagePath;
	}

}