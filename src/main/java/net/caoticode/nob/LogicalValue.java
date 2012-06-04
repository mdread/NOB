package net.caoticode.nob;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Daniel Camarda [0xcaos@gmail.com]
 */

public class LogicalValue {
	// STATIC CONSTANTS
	private static final Float ZERO_F = new Float(0.0F);
	private static final Double ZERO_D = new Double(0.0D);

	private static enum Operator {
		FIRST_VAL, NOT, AND, OR
	}

	// PUBLIC ATTRIBUTES
	public final NotProxyAND and;
	public final NotProxyOR or;

	// PRIVATE ATTRIBUTES
	private List<Element> vl = new LinkedList<Element>();

	private boolean boolResult = false;
	private Object valResult = null;

	// INNER CLASSES
	private class Element implements Cloneable {
		public Operator op;
		public Object val;
		public Constraint cons;
		public boolean bool;
		
		public Element(Object val, Operator op, Constraint cons) {
			this.op = op;
			this.val = val;
			this.cons = cons;
			this.bool = eval();
		}
		
		public Element(Object val, Operator op, boolean bool) {
			this.op = op;
			this.val = val;
			this.cons = Constraint.NOOP;
			this.bool = bool;
		}

		public Element(Operator op) {
			this.op = op;
			this.val = null;
		}
		
		public Element clone() {
			Element e = new Element(val, op, bool);
			e.cons = cons;
			return e;
		}
		
		public boolean eval(){
			if(cons.getRestriction() != Constraint.Restriction.NOOP){
				return cons.eval(val);
			}else{
				return LogicalValue.bool(val);
			}
		}
	}

	public class Result {
		public final boolean bool;
		public final Object val;

		public Result(boolean bool, Object val) {
			this.bool = bool;
			this.val = val;
		}
	}

	public class NotProxyAND {
		private LogicalValue _this = null;

		public NotProxyAND(LogicalValue _this) {
			this._this = _this;
		}

		public LogicalValue not(Object value, Constraint cons) {
			Element e = new Element(Operator.NOT);
			_this.vl.add(e);

			return _this.and(value, cons);
		}
		public LogicalValue not(Object value) {
			return not(value, Constraint.NOOP);
		}
	}

	public class NotProxyOR {
		private LogicalValue _this = null;

		public NotProxyOR(LogicalValue _this) {
			this._this = _this;
		}

		public LogicalValue not(Object value, Constraint cons) {
			Element e = new Element(Operator.NOT);
			_this.vl.add(e);

			return _this.or(value, cons);
		}
		public LogicalValue not(Object value) {
			return not(value, Constraint.NOOP);
		}
	}


	// *******CONSTRUCTORS*******

	private LogicalValue(Object value, Constraint cons) {
		this();
		first(value, cons);
	}

	private LogicalValue() {
		and = new NotProxyAND(this);
		or = new NotProxyOR(this);
	}


	// ********STATIC METHODS********
	
	public static LogicalValue $(Object val, Constraint cons) {
		return new LogicalValue(val, cons);
	}
	
	public static LogicalValue $(Object val) {
		return $(val, Constraint.NOOP);
	}

	public static LogicalValue not(Object val, Constraint cons) {
		return new LogicalValue().not().first(val, cons);
	}
	
	public static LogicalValue not(Object val) {
		return not(val, Constraint.NOOP);
	}

	public static boolean bool(Object value) {
		if (value == null) {
			return false;
		} else if (value instanceof IsEmpty && ((IsEmpty) value).isEmpty()) {
			return false;
		} else if (value instanceof LogicalValue
				&& !((LogicalValue) value).bool()) {
			return false;
		} else if (value instanceof Boolean
				&& ((Boolean) value).equals(Boolean.FALSE)) {
			return false;
		} else if (value instanceof CharSequence
				&& ((CharSequence) value).length() == 0) {
			return false;
		} else if (value instanceof Collection
				&& ((Collection<?>) value).isEmpty()) {
			return false;
		} else if (value instanceof Long && ((Long) value).longValue() == 0L) {
			return false;
		} else if (value instanceof Integer
				&& ((Integer) value).intValue() == 0) {
			return false;
		} else if (value instanceof Short
				&& ((Short) value).shortValue() == (short) 0) {
			return false;
		} else if (value instanceof Byte
				&& ((Byte) value).byteValue() == (byte) 0) {
			return false;
		} else if (value instanceof Float && ((Float) value).equals(ZERO_F)) {
			return false;
		} else if (value instanceof Double && ((Double) value).equals(ZERO_D)) {
			return false;
		} else if (value instanceof BigInteger
				&& ((BigInteger) value).equals(BigInteger.ZERO)) {
			return false;
		} else if (value instanceof BigDecimal
				&& ((BigDecimal) value).equals(BigDecimal.ZERO)) {
			return false;
		} else if (value instanceof Number
				&& new Double(((Number) value).doubleValue()).equals(ZERO_D)) {
			return false;
		} else if (value.getClass().isArray() && Array.getLength(value) == 0) {
			return false;
		} else if (value instanceof Character
				&& Character.isIdentifierIgnorable(((Character) value)
						.charValue())) {
			return false;
		}

		return true;
	}

	// ********PUBLIC METHODS********

	// CHAINABLE METHODS
	public LogicalValue or(Object value, Constraint cons) {
		Element e = new Element(value, Operator.OR, cons);
		vl.add(e);

		return this;
	}
	public LogicalValue or(Object value) {
		return or(value, Constraint.NOOP);
	}

	public LogicalValue and(Object value, Constraint cons) {
		Element e = new Element(value, Operator.AND, cons);
		vl.add(e);

		return this;
	}

	public LogicalValue and(Object value) {
		return and(value, Constraint.NOOP);
	}
	
	// PROCESOR METHODS
	public boolean bool() {
		doProcess();
		return boolResult;
	}

	@SuppressWarnings("unchecked")
	public <T> T val() {
		doProcess();
		return (T)valResult;
	}

	public String valAsString() {
		return (String) val();
	}

	public Character valAsCharacter() {
		return (Character) val();
	}

	public Byte valAsByte() {
		return (Byte) val();
	}

	public Short valAsShort() {
		return (Short) val();
	}

	public Integer valAsInteger() {
		return (Integer) val();
	}

	public Float valAsFloat() {
		return (Float) val();
	}

	public Double valAsDouble() {
		return (Double) val();
	}

	public Boolean valAsBoolean() {
		return (Boolean) val();
	}

	@SuppressWarnings("unchecked")
	public <T> T valAs(Class<T> type) {
		Object v = val();
		
		return v == null ? null : (T) v;
	}

	public Result result() {
		doProcess();
		// process(vl);
		return new Result(boolResult, valResult);
	}

	public String toString() {
		Object res = val();
		if (res == null)
			return null;
		return res.toString();
	}

	// *******PRIVATE METHODS********

	private LogicalValue first(Object value, Constraint cons) {
		Element e = new Element(value, Operator.FIRST_VAL, cons);
		vl.add(e);

		return this;
	}
	
	private LogicalValue not() {
		Element e = new Element(Operator.NOT);
		vl.add(e);

		return this;
	}

	private void doProcess() {
		List<Element> vl_clone = new LinkedList<Element>();
		for (Iterator<Element> iterator = vl.iterator(); iterator.hasNext();) {
			Element e = iterator.next();
			vl_clone.add(e.clone());
		}
		process(vl_clone);
	}

	private void process(List<Element> vl) {
		// prima cerco ed eseguo le negazioni, creando una nuova lista filtrata
		// TODO filtrare la lista ricevuta... ?
		List<Element> newvl = new LinkedList<Element>();
		for (int i = 0; i < vl.size(); i++) {
			Element e = (Element) vl.get(i);
			if (e.op == Operator.NOT) {
				// dopo un NOT esiste sempre un altro elemento
				Element nextE = (Element) vl.get(++i); // incremento i per prendere il prossimo elemento e saltarlo nella prossima iterazione
				nextE.bool = !nextE.bool;
				nextE.val = Boolean.valueOf(nextE.bool);
				newvl.add(nextE);
			} else {
				newvl.add(e);
			}
		}
		vl = newvl;
		newvl = new LinkedList<Element>();

		// cerco ed eseguo gli and, filtrando la lista
		for (int i = 0; i < vl.size(); i++) {
			Element e = (Element) vl.get(i);
			if (e.op == Operator.AND) {
				int j = i;
				Element prevE = (Element) newvl.get(newvl.size() - 1);

				do {
					newvl.remove(newvl.size() - 1);
					e = (Element) vl.get(j);

					if (!prevE.bool) {
						newvl.add(new Element(prevE.val, prevE.op, false));
					} else {
						newvl.add(new Element(e.val, prevE.op, e.bool));
					}

					prevE = (Element) newvl.get(newvl.size() - 1);
					j++;
				} while (j < vl.size() && ((Element) vl.get(j)).op == Operator.AND);

				i += j - i - 1;
			} else {
				newvl.add(e);
			}
		}
		vl = newvl;
		newvl = new LinkedList<Element>();

		// a questo punto la lista consiste in una serie di OR e quindi
		// restituisco il primo elemento true
		for (int i = 0; i < vl.size(); i++) {
			Element e = (Element) vl.get(i);
			
			if(e.cons.getRestriction() == Constraint.Restriction.NOOP){
				valResult = ((Element) vl.get(i)).val;
			}else{
				valResult = null;
			}
			
			if (e.bool) {
				valResult = ((Element) vl.get(i)).val;
				boolResult = true;
				break;
			}
		}
	}
	
}
