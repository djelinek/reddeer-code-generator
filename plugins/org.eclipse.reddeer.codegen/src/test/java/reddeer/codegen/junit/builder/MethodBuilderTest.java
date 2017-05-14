package reddeer.codegen.junit.builder;

import static org.junit.Assert.assertEquals;

import reddeer.codegen.builder.MethodBuilder;
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
	
	@Test
	public void testMethodVisibility() {
		String visibility = "public";
		builder.visibility(visibility);
		assertEquals(visibility, builder.getVisibility());
	}
	
	@Test
	public void testMethodRule() {
		String rule = "BTN";
		builder.rule(rule);
		assertEquals(rule, builder.getRule());
	}
	
	@Test
	public void testMethodSetterName() {
		String name = "Name";
		builder.set(name);
		assertEquals("set" + name, builder.getName());
	}
	
	@Test
	public void testMethodReturnType() {
		String returnType = "String";
		builder.returnType(returnType);
		assertEquals(returnType, builder.getReturnType());
	}

}
