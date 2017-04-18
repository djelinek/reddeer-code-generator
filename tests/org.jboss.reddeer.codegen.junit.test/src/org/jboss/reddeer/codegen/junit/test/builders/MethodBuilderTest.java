package org.jboss.reddeer.codegen.junit.test.builders;

import static org.junit.Assert.*;

import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.junit.Before;
import org.junit.Test;

public class MethodBuilderTest {

	MethodBuilder builder;

	@Before
	public void init() {
		builder = new MethodBuilder();
	}

	@Test
	public void testMethodName() {
		String name = "testClass";
		builder.name(name);
		assertEquals(name, builder.getName());
	}

	@Test
	public void testMethodType() {
		String type = "void";
		builder.type(type);
		assertEquals(type, builder.getMethodType());
	}

	@Test
	public void testMethodGetterName() {
		String get = "TestName";
		builder.get(get);
		assertEquals("get" + get, builder.getName());
	}

}
