package com.picture.publishing.publisher.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.picture.publishing.publisher.model.Picture;
import com.picture.publishing.publisher.model.PictureStatus;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

	List<Picture> findByStatus(PictureStatus pending);
}