package net.caoticode.nob;

public class Constraint {
	public static enum Restriction{
		EQ, NE, GT, LT, GE, LE, TRUE, FALSE, NOOP
	}
	private Restriction restriction;
	private Object value;
	
	public Constraint(Object obj, Restriction restriction) {
		this.restriction = restriction;
		this.value = obj;
	}
	
	public static Constraint eq(Object obj){
		return new Constraint(obj, Restriction.EQ);
	}
	
	public static Constraint ne(Object obj){
		return new Constraint(obj, Restriction.NE);
	}
	
	public static Constraint gt(Object obj){
		return new Constraint(obj, Restriction.GT);
	}
	
	public static Constraint lt(Object obj){
		return new Constraint(obj, Restriction.LT);
	}
	
	public static Constraint ge(Object obj){
		return new Constraint(obj, Restriction.GE);
	}
	
	public static Constraint le(Object obj){
		return new Constraint(obj, Restriction.LE);
	}
	
	public static Constraint cons(Object obj, boolean bool){
		return new Constraint(obj, bool ? Restriction.TRUE : Restriction.FALSE);
	}
	
	public Restriction getRestriction() {
		return restriction;
	}
	public Object getValue() {
		return value;
	}
}
