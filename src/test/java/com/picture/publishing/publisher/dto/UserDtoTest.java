package com.picture.publishing.publisher.dto;

import org.junit.jupiter.api.Test;

import com.picture.publishing.publisher.helpers.SetterAndGetterTest;

class UserDtoTest {

	@Test
	void testFlatFileReaderMetadata_Parameters() throws Exception {
		SetterAndGetterTest.validateSetterAndGetterTest(UserDto.class);
	}

}
