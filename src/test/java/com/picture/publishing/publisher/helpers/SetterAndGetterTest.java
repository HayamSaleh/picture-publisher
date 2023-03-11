package com.picture.publishing.publisher.helpers;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

public final class SetterAndGetterTest {

	private SetterAndGetterTest() {
	}

	public static void validateSetterAndGetterTest(Class<?> testedClass) {
		PojoClass pojoClass = PojoClassFactory.getPojoClass(testedClass);
		Validator validator = ValidatorBuilder.create()//
				.with(new GetterMustExistRule())//
				.with(new SetterMustExistRule()).with(new GetterTester())//
				.with(new SetterTester()).build();
		validator.validate(pojoClass);
	}

}