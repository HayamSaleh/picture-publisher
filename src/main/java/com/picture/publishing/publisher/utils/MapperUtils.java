package com.picture.publishing.publisher.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperUtils {

	@Autowired
	private DozerBeanMapper dozerMapper;

	public <T, S> List<T> mapList(List<S> source, Class<T> destinationClass) {
		List<T> destination = new ArrayList<>(source.size());

		if (CollectionUtils.isNotEmpty(source)) {
			for (S item : source) {
				destination.add(dozerMapper.map(item, destinationClass));
			}
		}

		return destination;
	}

}
