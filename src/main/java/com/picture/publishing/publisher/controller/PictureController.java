package com.picture.publishing.publisher.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.picture.publishing.publisher.dto.PictureDto;
import com.picture.publishing.publisher.dto.PictureListingDto;
import com.picture.publishing.publisher.model.PictureCategory;
import com.picture.publishing.publisher.service.PictureService;

@RestController
@RequestMapping("/pictures")
public class PictureController {

	@Autowired
	PictureService pictureService;

	@PostMapping(value = "/submit")
	public ResponseEntity<PictureDto> sumbitPicture(@RequestParam String description,
			@RequestParam PictureCategory category, @RequestParam MultipartFile attachment) throws IOException {
		return new ResponseEntity<>(pictureService.submitPicture(description, category, attachment),
				HttpStatus.CREATED);
	}

	@GetMapping(value = "/accepted")
	public ResponseEntity<List<PictureListingDto>> getAcceptedPictures() {
		return new ResponseEntity<>(pictureService.getAcceptedPictures(), HttpStatus.OK);
	}

}