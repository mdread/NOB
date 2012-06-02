package net.caoticode.nob;

import static org.junit.Assert.*;
import static net.caoticode.nob.LogicalValue.*;
import static net.caoticode.nob.Constraint.*;

import org.junit.Test;

public class LogicalValueTest {

	@Test
	public void testAndBoolean() {
		assertEquals($(true).and(true).bool(), true);
		assertEquals($(true).and(false).bool(), false);
		assertEquals($(false).and(true).bool(), false);
		assertEquals($(false).and(false).bool(), false);
	}

	@Test
	public void testOrBoolean() {
		assertEquals($(true).or(true).bool(), true);
		assertEquals($(true).or(false).bool(), true);
		assertEquals($(false).or(true).bool(), true);
		assertEquals($(false).or(false).bool(), false);
	}
	
	@Test
	public void testNotBoolean() {
		assertEquals(not(true).bool(), false);
		assertEquals(not(false).bool(), true);
	}
	
	@Test
	public void testOperatorBooleanPriority() {
		assertEquals($(true).and(false).or(true).bool(), true);
		assertEquals($(true).and(true).or(false).bool(), true);
		assertEquals($(false).or(true).and(false).bool(), false);
		assertEquals($(false).or(true).and(true).bool(), true);
		assertEquals($(false).and(false).or(true).and(true).bool(), true);
		assertEquals($(false).and($(false).or(true)).and(true).bool(), false);
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
		assertEquals($("a", eq("a")).bool(), true);
		assertEquals($("a", ne("a")).bool(), false);
		assertEquals($(24, gt(2)).bool(), true);
		assertEquals($(24, lt(2)).bool(), false);
		assertEquals($(24, le(24)).bool(), true);
		assertEquals($(24, ge(23)).bool(), true);
		assertEquals($(24, le(22)).bool(), false);
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
