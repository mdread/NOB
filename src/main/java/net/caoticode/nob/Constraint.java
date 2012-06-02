package net.caoticode.nob;

public class Constraint {
	public static enum Restriction{
		EQ, NE, GT, LT, GE, LE, TRUE, FALSE
	}
	private Restriction restriction;
	
	public Constraint(Restriction restriction) {
		this.restriction = restriction;
	}
	
	public static Constraint eq(Object obj){
		return new Constraint(Restriction.EQ);
	}
	
	public static Constraint ne(Object obj){
		return new Constraint(Restriction.NE);
	}
	
	public static Constraint gt(Object obj){
		return new Constraint(Restriction.GT);
	}
	
	public static Constraint lt(Object obj){
		return new Constraint(Restriction.LT);
	}
	
	public static Constraint ge(Object obj){
		return new Constraint(Restriction.GE);
	}
	
	public static Constraint le(Object obj){
		return new Constraint(Restriction.LE);
	}
	
	public static Constraint cons(boolean bool){
		return new Constraint(bool ? Restriction.TRUE : Restriction.FALSE);
	}
	
	public Restriction getRestriction() {
		return restriction;
	}
}
