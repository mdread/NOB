package net.caoticode.nob;

import static org.junit.Assert.*;
import static net.caoticode.nob.LogicalValue.*;
import static net.caoticode.nob.Constraint.*;

import org.junit.Test;

public class LogicalValueTest {

	@Test
	public void testAndBoolean() {
		assertTrue($(true).and(true).bool());
		assertFalse($(true).and(false).bool());
		assertFalse($(false).and(true).bool());
		assertFalse($(false).and(false).bool());
	}

	@Test
	public void testOrBoolean() {
		assertTrue($(true).or(true).bool());
		assertTrue($(true).or(false).bool());
		assertTrue($(false).or(true).bool());
		assertFalse($(false).or(false).bool());
	}
	
	@Test
	public void testNotBoolean() {
		assertFalse(not(true).bool());
		assertTrue(not(false).bool());
	}
	
	@Test
	public void testOperatorBooleanPriority() {
		assertTrue($(true).and(false).or(true).bool());
		assertTrue($(true).and(true).or(false).bool());
		assertFalse($(false).or(true).and(false).bool());
		assertTrue($(false).or(true).and(true).bool());
		assertTrue($(false).and(false).or(true).and(true).bool());
		assertFalse($(false).and($(false).or(true)).and(true).bool());
	}
	
	@Test
	public void testReturnValue() {
		assertEquals($("a").or("b").and("c").val(), "a");
		assertEquals(not("a").or("b").and("c").val(), "c");
		assertEquals($("").or("b").and("c").val(), "c");
		assertEquals($("a").or("b").and("").val(), "a");
		assertEquals($("").or("").and("c").val(), "");
		assertEquals($("a").and(3.8f).or(0).val(), 3.8f);
		assertEquals(not($("a").or("b")).and("c").val(), false);
	}
	
	@Test
	public void testConstraintsBoolean() {
		assertTrue($("a", eq("a")).bool());
		assertFalse($("a", ne("a")).bool());
		assertTrue($(24, gt(2)).bool());
		assertFalse($(24, lt(2)).bool());
		assertTrue($(24, le(24)).bool());
		assertTrue($(24, ge(23)).bool());
		assertFalse($(24, le(22)).bool());
		assertFalse($("a", cons(false)).bool());
		assertTrue($("a", cons(true)).bool());
	}
	
	@Test
	public void testConstraintsValue() {
		assertEquals($("a", eq("a")).val(), "a");
		assertNull($("a", ne("a")).val());
		assertEquals($("a", ne("a")).or("b").val(), "b");
		assertEquals($("a", ne("a")).or(38, gt(35)).val(), 38);
		assertNull($("a", ne("a")).or(38, lt(35)).val());
		assertEquals($("a", eq("a")).and(38, gt(35)).or("b").val(), 38);
		assertEquals($("a", eq("a")).and.not(38, gt(35)).or("b").val(), "b");
	}
}
