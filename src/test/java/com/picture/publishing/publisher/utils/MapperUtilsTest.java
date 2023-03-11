package com.picture.publishing.publisher.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MapperUtilsTest {

  @Mock
  private DozerBeanMapper dozerMapper;

  @InjectMocks
  private MapperUtils mapperUtils;

  @Test
  void shouldMapList() {
	List<SourceObject> sourceList = Arrays.asList(new SourceObject("first", "last"), new SourceObject("1st", "2nd"));

	List<DestinationObject> expectedDestinationList = Arrays.asList(new DestinationObject("first", "last"),
		new DestinationObject("1st", "2nd"));

	when(dozerMapper.map(sourceList.get(0), DestinationObject.class)).thenReturn(expectedDestinationList.get(0));
	when(dozerMapper.map(sourceList.get(1), DestinationObject.class)).thenReturn(expectedDestinationList.get(1));
	List<DestinationObject> actualDestinationList = mapperUtils.mapList(sourceList, DestinationObject.class);

	assertThat(actualDestinationList).containsExactlyElementsOf(expectedDestinationList);
  }

  // add test case for empty source

  private static class SourceObject {
	private String firstName;
	private String lastName;

	public SourceObject(String firstName, String lastName) {
	  this.firstName = firstName;
	  this.lastName = lastName;
	}
  }

  private static class DestinationObject {
	private String firstName;
	private String lastName;

	public DestinationObject(String firstName, String lastName) {
	  this.firstName = firstName;
	  this.lastName = lastName;
	}
  }

}
