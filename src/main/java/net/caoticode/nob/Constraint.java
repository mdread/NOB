package net.caoticode.nob;

/**
 * @author Daniel Camarda [0xcaos@gmail.com]
 */

public class Constraint {
	public static enum Restriction{
		EQ, NE, GT, LT, GE, LE, TRUE, FALSE, NOOP
	}
	public static final Constraint NOOP = new Constraint(null, Restriction.NOOP);

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
	
	public static Constraint cons(boolean bool){
		return new Constraint(null, bool ? Restriction.TRUE : Restriction.FALSE);
	}
	
	public Restriction getRestriction() {
		return restriction;
	}
	
	public Object getValue() {
		return value;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean eval(Object val){
		if(restriction == Restriction.NOOP)
				throw new RuntimeException("can not eval on NOOP Constraint");
		if(val == null && getValue() == null)
			return true;
		if((val == null ^ getValue() == null) && restriction != Restriction.TRUE && restriction != Restriction.FALSE)
			return false;
		
		switch(restriction){
			case EQ:
				return val.equals(getValue());
			case NE:
				return !(val.equals(getValue()));
			case GT:
				return areComparable(val, getValue()) ? ((Comparable)val).compareTo(getValue()) > 0 : false;
			case GE:
				return areComparable(val, getValue()) ? ((Comparable)val).compareTo(getValue()) >= 0 : false;
			case LT:
				return areComparable(val, getValue()) ? ((Comparable)val).compareTo(getValue()) < 0 : false;
			case LE:
				return areComparable(val, getValue()) ? ((Comparable)val).compareTo(getValue()) <= 0 : false;
			case TRUE:
				return true;
			case FALSE:
				return false;
			default:
				return false;
		}
	}
	
	private boolean areComparable(Object obj1, Object obj2){
		return obj1 instanceof Comparable && obj1.getClass().isAssignableFrom(obj2.getClass());
	}
}
