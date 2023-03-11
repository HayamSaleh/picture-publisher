package com.picture.publishing.publisher.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picture.publishing.publisher.dto.PictureDto;
import com.picture.publishing.publisher.service.PictureProcessingService;

@RestController
@RequestMapping("/admin/pictures")
public class PictureProcessingController {

	@Autowired
	PictureProcessingService pictureProcessingService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<PictureDto> getPictureById(@PathVariable Long id) {
		return new ResponseEntity<>(pictureProcessingService.getPictureById(id), HttpStatus.OK);
	}

	@GetMapping(value = "/pending")
	public ResponseEntity<List<PictureDto>> getPendingPictures() {
		return new ResponseEntity<>(pictureProcessingService.getPendingPictures(), HttpStatus.OK);
	}

	@GetMapping(value = "/rejected")
	public ResponseEntity<List<PictureDto>> getRejectedPictures() {
		return new ResponseEntity<>(pictureProcessingService.getRejectedPictures(), HttpStatus.OK);
	}

	@PutMapping(value = "/accept/{id}")
	public ResponseEntity<PictureDto> acceptPicture(@PathVariable Long id) throws IOException {
		return new ResponseEntity<>(pictureProcessingService.acceptPicture(id), HttpStatus.OK);
	}

	@PutMapping(value = "/reject/{id}")
	public ResponseEntity<PictureDto> rejectPicture(@PathVariable Long id) {
		return new ResponseEntity<>(pictureProcessingService.rejectPicture(id), HttpStatus.OK);
	}

}