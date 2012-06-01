package net.caoticode.nob;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")

/**
 * Questo testo descrittivo e un estratto dell'articolo "Instant Python"(http://hetland.org/writing/instant-python.html)
 * questa classe simula lo stesso comportamento.
 * alcune parti del testo sono state modificate per riflettere i tipi di dati e la sintassis di java al posto
 * di quella python
 * 
 * 
 * All values with this class can be used as logic values. 
 * Some of the more "empty" ones, like new int[0], 0, "" and null represent logical falsity,
 * while most other values (like new int[]{0}, 1 or "Hello, world") represent logical truth.
 * Now, logical expressions like a and b are evaluated like this: 
 * 	First, check if a is true. If it is not, then simply return it. If it is, 
 * 	then simply return b (which will represent the truth value of the expression.) 
 * 	The corresponding logic for a or b is: If a is true, then return it. If it isn't, then return b.
 * This mechanism makes "and" and "or" behave like the boolean operators they are supposed to implement, but 
 * they also let you write short and sweet little conditional expressions. For instance, the statement
 * 
 * if(!a.equals("")){
 * 		System.out.println(a);
 * }else{
 * 		System.out.println(b);
 * }
 * 
 * Could instead be written:
 * 
 * System.out.println($(a).or(b));
 * 
 * ****************************************************************************************************************************************
 * 
 * SimpleLogic e stata pensata per avere una sintassis il piu semplice, compatta e intuitiva possibile in modo da 
 * aumentare la produttività e la leggibilità del codice
 * la classe mette a disposizione tre metodi fondamentali: and, or e not che corrispondono agli operatori condizionali java &&, ||, e !
 * ma con alcune funzionalita extra. i condizionali di java possono essere utilizzati solo con valori di tipo booleano, cioè true e false
 * e questo significa che per evaluare la validita di una condizione siamo obbligati a trasformate i nostri dati in valori di tipo booleano 
 * come ad esempio per verificare se il valore di una stringa puo essere considerato true o false e comune usare il 
 * metodo equals per assicurarsi che non contenga una stringa vuota e usare il risultato booleano di quel metodo in un espressione 
 * piu complessa; con questa classe tutti i tipi di dati, oltre ai boolean, hanno inherente al suo valore un significato di verita o falsita, 
 * o piu specificamente di vuoto o pieno il che permette di usare i valori in modo diretto in espressioni booleane.
 * il concetto di vuoto o pieno varia a seconda del tipo di dato, in seguito un riassunto.
 * i seguenti valori sono considerati false:
 * 		qualsiasi oggetto null, una stringha di dimensioni 0, un integer, short o byte uguale a 0, un float o un double uguali a 0, 
 * 		un char che fa parte dei caratteri ignorabili(come i caratteri di controllo... Character.isIdentifierIgnorable), 
 * 		un array di 0 elementi, una collezione con 0 elementi, il booleano false.
 * qualsiasi altro oggetto non null e considerato true 
 * 
 * oltre all'utilizzo in modo diretto dei valori e possibile ottenere il risultato di un'intera espressione booleana in due formati:
 * 	il classico true o false o il valore associato alla "verità" o "falsità" dell'espressione, questo concetto e piu chiaro con un esempio.
 * 
 * System.out.println( $('a').or('b').and('c').bool() ) //true
 * System.out.println( $('a').or('b').and('c') ) // a
 * 
 * cominciamo per vedere come viene evaluata l'espressione 'a' or 'b' and 'c'; per prima cosa viene evaluato il char 'b' per verificate
 * la sua verità o falsità, il quale viene evaluato a true, poi viene evaluato 'c', anche true, poi viene fatta l'operazione and tra
 * quei due valori appenda validati, in questo caso il risultato e come ci si puo aspettare true in termini di valori booleani, 
 * e 'c' in termini dei valori che si stanno evaluando a questo punto viene evaluato 'a' anche a true e viene effettuato l'or tra
 * 'a' e 'c' dando come risultato 'a'.
 * analizzando l'esempio con piu dettaglio si puo vedere che gli operatori hanno una precedenza uno sull'altro, l'operatore and ha precedenza 
 * sul operatore or e il not, non presente in questo esempio, ha precedenza sul and; questo spiega il perche viene evaluato prima
 * 'b' and 'c' e non 'a' or 'b'
 * un altro dato importante e che in un operazione and se tutti i valori sino true viene restituito l'ultimo di questi valori evaluato mentre
 * se uno dei valori e false e questo il valore restituito, ad esempio:
 * 		$('a').and('b').and('c') restituisce 'c', ma invece
 * 		$('a').and(0).and('c') restituisce 0 visto che a quel punto il risultato dell'espressione e stato trovato(anche se dopo quel valore false
 * 		ci stanno altri che sono true, come in questo caso 'c', visto che si tratta di un AND il risultato sarebbe comunque false e non ha senso
 * 		andare avanti.
 * invece quando si tratta di un or viene restituito il primo valore true e altrimenti l'ultimo valore false, esempio:
 *  	$('a').or('b').or('c') restituisce 'a'
 *  	$("").or(0).or(0.00) restituisce 0.00, questo e cosi perche in on'operazione or ce sempre la possibilita che dopo un valore false ci sia
 *  	un valore true che faccia diventare true il risultao dell'espressione.
 *  
 * in sintesi viene restituito l'ultimo valore che viene evaluato per determinare la verita o falsita dell'espressione
 * 
 * 
 * @author Daniel Camarda
 */
public class LogicalValue{
	//STATIC CONSTANTS
	private static final Float ZERO_F = new Float(0.0F);
	private static final Double ZERO_D = new Double(0.0D);
	
	private static enum Operator {
		FIRST_VAL, NOT, AND, OR
	}
	
//	private static final byte FIRST_VAL = 3;
//	private static final byte NOT = 2;
//	private static final byte AND = 1;
//	private static final byte OR = 0;
	
	//PUBLIC ATTRIBUTES
	public final NotProxyAND and;
	public final NotProxyOR or;
	
	//PRIVATE ATTRIBUTES
	private List<Element> vl = new LinkedList<Element>();
	
	private boolean boolResult = false;
	private Object valResult = null;
	
	//INNER CLASSES
	private class Element implements Cloneable{
		public Operator op;
		public Object val;
		public boolean bool;
		
		public Element(Object val, Operator op, boolean bool) {
			this.op = op;
			this.val = val;
			this.bool = bool;
		}
		
		public Element(Operator op) {
			this.op = op;
			this.val = null;
		}
		
		protected Element clone() {
			return new Element(val, op, bool);
		}
	}
	
	public class Result{
		public final boolean bool;
		public final Object val;
		
		public Result(boolean bool, Object val) {
			this.bool = bool;
			this.val = val;
		}
	}
	
	public class NotProxyAND{
		private LogicalValue _this = null;
		
		public NotProxyAND(LogicalValue _this) {
			this._this = _this;
		}
		
		public LogicalValue not(Object value){
			Element e = new Element(Operator.NOT);
			_this.vl.add(e);
			
			return _this.and(value);
		}
	}
	
	public class NotProxyOR{
		private LogicalValue _this = null;
		
		public NotProxyOR(LogicalValue _this) {
			this._this = _this;
		}
		
		public LogicalValue not(Object value){
			Element e = new Element(Operator.NOT);
			_this.vl.add(e);
			
			return _this.or(value);
		}
	}

    //******************************************************
	//*********************CONSTRUCTORS*********************
	//******************************************************
    
	private LogicalValue(Object value) {
		this();
		first(value);
	}
	
	private LogicalValue() {
		and = new NotProxyAND(this);
		or = new NotProxyOR(this);
	}

    //******************************************************
	//********************STATIC METHODS********************
	//******************************************************
	
	public static LogicalValue $(Object val){
		return new LogicalValue(val);
	}

	public static LogicalValue not(Object val){
		return new LogicalValue().not().first(val);
	}

    public static boolean bool(Object value){
		if (value == null){
			return false;
		}else if(value instanceof IsEmpty && ((IsEmpty)value).isEmpty()){
			return false;
		}else if(value instanceof LogicalValue && !((LogicalValue)value).bool()){
			return false;
		}else if(value instanceof Boolean && ((Boolean)value).equals(Boolean.FALSE)){
			return false;
		}else if(value instanceof CharSequence && ((CharSequence)value).length() == 0){
			return false;
		}else if(value instanceof Collection && ((Collection<?>)value).isEmpty()){
			return false;
		}else if(value instanceof Long && ((Long)value).longValue() == 0L){
			return false;
		}else if(value instanceof Integer && ((Integer)value).intValue() == 0){
			return false;
		}else if(value instanceof Short && ((Short)value).shortValue() == (short)0){
			return false;
		}else if(value instanceof Byte && ((Byte)value).byteValue() == (byte)0){
			return false;
		}else if(value instanceof Float && ((Float)value).equals(ZERO_F)){
			return false;
		}else if(value instanceof Double && ((Double)value).equals(ZERO_D)){
			return false;
		}else if(value instanceof BigInteger && ((BigInteger)value).equals(BigInteger.ZERO)){
			return false;
		}else if(value instanceof BigDecimal && ((BigDecimal)value).equals(BigDecimal.ZERO)){
			return false;
		}else if(value instanceof Number && new Double(((Number)value).doubleValue()).equals(ZERO_D)){
			return false;
		}else if(value.getClass().isArray() && Array.getLength(value) == 0){
			return false;
		}else if(value instanceof Character && Character.isIdentifierIgnorable(((Character)value).charValue())){
			return false;
		}

		return true;
	}
	
    //******************************************************
	//********************PUBLIC METHODS********************
	//******************************************************

	//CHAINABLE METHODS
	public LogicalValue or(Object value){
		Element e = new Element(value, Operator.OR, bool(value));
		vl.add(e);
		
		return this;
	}

	public LogicalValue and(Object value){
		Element e = new Element(value, Operator.AND, bool(value));
		vl.add(e);
		
		return this;
	}
    
	//PROCESOR METHODS
	public boolean bool(){
		doProcess();
		//process(vl);
		return boolResult;
	}
	
	public Object val(){
		doProcess();
		//process(vl);
		return valResult;
	}

    public String valAsString(){
        return (String)val();
    }

    public Character valAsCharacter(){
        return (Character)val();
    }

    public Byte valAsByte(){
        return (Byte)val();
    }

    public Short valAsShort(){
        return (Short)val();
    }

    public Integer valAsInteger(){
        return (Integer)val();
    }

    public Float valAsFloat(){
        return (Float)val();
    }

    public Double valAsDouble(){
        return (Double)val();
    }

    public Boolean valAsBoolean(){
        return (Boolean)val();
    }

    public<T> T valAs(Class<T> type){
        return (T)val();
    }
	
	public Result result(){
		doProcess();
		//process(vl);
		return new Result(boolResult, valResult);
	}
	
	public String toString() {
		Object res = val();
		if(res == null)
			return null;
		return res.toString();
	}

    /*public boolean equals(Object obj) {
        return val().equals(obj);
    }*/

    //******************************************************
	//*******************PRIVATE METHODS*******************
	//******************************************************
	
	private LogicalValue first(Object value){
		Element e = new Element(value, Operator.FIRST_VAL, bool(value));
		vl.add(e);
		
		return this;
	}

    private LogicalValue not(){
		Element e = new Element(Operator.NOT);
		vl.add(e);

		return this;
	}
	
	private void doProcess(){
		List<Element> vl_clone = new LinkedList<Element>();
		for (Iterator<Element> iterator = vl.iterator(); iterator.hasNext();) {
			Element e = iterator.next();
			vl_clone.add(e.clone());
		}
		process(vl_clone);
	}
	
	private void process(List<Element> vl){
		//prima cerco ed eseguo le negazioni, creando una nuova lista filtrata
        //TODO filtrare la lista ricevuta... ?
		List<Element> newvl = new LinkedList<Element>();
		for (int i = 0; i < vl.size(); i++) {
			Element e = (Element) vl.get(i);
			if(e.op == Operator.NOT){
				//si presuppone che dopo un NOT esista sempre un altro elemento, garantirlo da qualche parte...
				Element nextE = (Element) vl.get(++i); //incremento i per prendere il prossimo elemento e saltarlo nella prossima iterazione
				nextE.bool = !nextE.bool;
				nextE.val = Boolean.valueOf(nextE.bool);
				newvl.add(nextE);
			}else{
				newvl.add(e);
			}
		}
		vl = newvl;
		newvl = new LinkedList<Element>();
		
		//cerco ed eseguo gli and, filtrando la lista
		for (int i = 0; i < vl.size(); i++) {
			Element e = (Element) vl.get(i);
			if(e.op == Operator.AND){
				int j = i;
				Element prevE = (Element) newvl.get(newvl.size() - 1);
				
				do{
					newvl.remove(newvl.size() - 1);
					e = (Element) vl.get(j);
					
                    if(!prevE.bool){
                        newvl.add(new Element(prevE.val, prevE.op, false));
                    }else{
                        newvl.add(new Element(e.val, prevE.op, e.bool));
                    }
                    
					prevE = (Element) newvl.get(newvl.size() - 1);
					j++;
				}while(j < vl.size() && ((Element)vl.get(j)).op == Operator.AND);
				
				i += j - i - 1;
			}else{
				newvl.add(e);
			}
		}
		vl = newvl;
		newvl = new LinkedList<Element>();
		
		//a questo punto la lista consiste in una serie di OR e quindi restituisco il primo elemento true
		for (int i = 0; i < vl.size(); i++) {
			Element e = (Element) vl.get(i);
			
			valResult = ((Element)vl.get(i)).val;
			
			if(e.bool){
				boolResult = true;
				break;
			}
		}
	}
}
