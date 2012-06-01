NOB (NotOnlyBoolean)
====================

All values with this class can be used as logic values.
Some of the more "empty" ones, like *new int[0]*, *0*, *""* and *null* represent logical falsity,
while most other values (like *new int[]{0}*, *1* or *"Hello, world"*) represent logical truth.

Now, logical expressions like a and b are evaluated like this: 

First, check if *a* is true. If it is not, then simply return it. If it is, 
then simply return *b* (which will represent the truth value of the expression.) 
The corresponding logic for *a* or *b* is: If *a* is true, then return it. If it isn't, then return *b*.

This mechanism makes *"and"* and *"or"* behave like the boolean operators they are supposed to implement, but 
they also let you write short and sweet little conditional expressions. For instance, the statement

	if(!a.equals("")){
		System.out.println(a);
	}else{
		System.out.println(b);
	}
  
Could instead be written
  
	System.out.println($(a).or(b));

*(this description is extracted from the article "Instant Python"- http://hetland.org/writing/instant-python.html with some modifications that reflects the Java syntax and types)*
