package com.picture.publishing.publisher.service.impl;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.picture.publishing.publisher.controller.exception.NotAllowedRequestException;
import com.picture.publishing.publisher.dto.PictureDto;
import com.picture.publishing.publisher.dto.PictureListingDto;
import com.picture.publishing.publisher.model.Picture;
import com.picture.publishing.publisher.model.PictureCategory;
import com.picture.publishing.publisher.model.PictureStatus;
import com.picture.publishing.publisher.repository.PictureRepository;
import com.picture.publishing.publisher.service.PictureService;
import com.picture.publishing.publisher.service.UserService;
import com.picture.publishing.publisher.utils.MapperUtils;
import com.picture.publishing.publisher.utils.PictureUtils;

@Service
public class PictureServiceImpl implements PictureService {

	private final Logger logger = LoggerFactory.getLogger(PictureServiceImpl.class);

	private static final String ALLOWED_IMAGE_TYPES_MSG = "The file may be corrupted or has invalid type, The allowed Image types are [jpg, png, gif] only";

	private static final String PATH_SEPARATOR = "/";

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private MapperUtils mapperUtils;

	@Autowired
	private PictureUtils pictureUtils;
	@Autowired
	private UserService userService;

	@Autowired
	private PictureRepository pictureRepository;

	private static String urlPrefix;

	private Picture preparePictureData(String description, PictureCategory category, MultipartFile pictureFile)
			throws IOException {
		Picture picture = new Picture();
		picture.setName(pictureFile.getOriginalFilename());
		picture.setDescription(description);
		picture.setCategory(category);
		Dimension dimension = pictureUtils.getImageDimensions(pictureFile);
		picture.setPictureFile(pictureFile.getBytes());
		picture.setHeight(dimension.getHeight());
		picture.setWidth(dimension.getWidth());
		picture.setSize(pictureFile.getSize());
		picture.setStatus(PictureStatus.PENDING);
		return picture;
	}

	@Override
	public PictureDto submitPicture(String description, PictureCategory category, MultipartFile pictureFile)
			throws IOException {
		if (!pictureUtils.validateFile(pictureFile)) {
			throw new NotAllowedRequestException(ALLOWED_IMAGE_TYPES_MSG);
		}
		Picture picture = preparePictureData(description, category, pictureFile);
		String username = userService.getCurrentUserName();
		picture.setOwner(userService.findUserByUsername(username));
		Picture savedPicture = pictureRepository.save(picture);
		logger.info("User submitted picture: {userEmail = {}, pictureName = {}}", username, picture.getName());
		return mapper.map(savedPicture, PictureDto.class);
	}

	@Override
	public List<PictureListingDto> getAcceptedPictures() {
		List<Picture> pictures = pictureRepository.findByStatus(PictureStatus.ACCEPTED);

		for (Picture picture : pictures) {
			picture.setUrl(getUrlPrefix() + PATH_SEPARATOR + UPLOAD_ROOT + PATH_SEPARATOR + picture.getUrl());
		}
		return mapperUtils.mapList(pictures, PictureListingDto.class);
	}

	private static String getUrlPrefix() {
		if (urlPrefix == null) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			urlPrefix = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		}
		return urlPrefix;
	}

}