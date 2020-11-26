/*
(se così fosse, sarebbe un semplice "zucchero sintattico per la matematica" e nulla di più utile/interessante.)

Del tipo:

functionalDev(derivanda, funzDerivatrcice, derivationValue=0):
    Set<Variables> variables = funzDerivatrcice.vars()
    variables.forEachInOrder(ExpressionSintaxTree.DeepestLeftMost,
        v -> {
        //all examples will consider "derivationValue" as equal to "0", just to make it simple
            derived = RealAnalysis.derivate(derivanda, v);
            funzDerivatrcice = funzDerivatrcice.apply(derived);
            funzDerivatrcice.setVarValue(v, derivationValue); // es; v='x' & f=[sin(x+pi) / (y+(x+2)^2)] ->  f2=[sin(0+pi) / (y+(0+2)^2)]
            funzDerivatrcice = funzDerivatrcice.simply(); // es; v='x' & f=[sin(x+pi) / (y+(x+2)^2)] -> f2.1= -1/(y+4)
            derivanda = funzDerivatrcice
        }
    );
*/

interface Argument{ public String getName(); }
abstract class ArgumentItem implements Argument{}
    abstract class NumberArgumentItem implements ArgumentItem{}
        abstract class FloatNumber implements NumberArgumentItem{ protected double val=0.0; FloatNumber(double v){this.val=v;} FloatNumber(){this(0.0);} }
interface Dominion<D> extends ArgumentItem, OrderedSet<Argment> {} // is conceptually a Set (a List it's just a Map of N -> Set<ArgumentItem>)
    enum BooleanDominion implements Dominion{ True(Boolean.TRUE), False(Boolean.FALSE); /*... TODO*/}
    abstract class NumberDominion<N extends Number> implements Dominion<N>{}
    abstract class ComplexDominion implements Dominion{}
    abstract class StringDominion implements Dominion{}
    abstract class NumberRange implements NumberDominion<BigInteger, Number>{}
interface ArgumentList implements Argument, List<Argument>{} // it's a Tupla's type 
abstract class HomogeneousArgumentList<T extends Argument> implements Argument, List<T>{ /**/
    public List<Argument> getArgsType() { this.toValueList(); }
}
abstract class Tupla<...> implements ArgumentList{
    public List<Argument> getTypes(){ this.class.getArguments();  /*reflection*/}
}

interface Function<Dom extends Dominion<T>, Codom extends Dominion<Q>> extends Argument, Map<Dom, Codom>{
    public
}
interface FunctionalFunction extends Function<HomogeneousArgumentList<Function<?,?>>,Function<?,?>>{}
//interface FunctionalFunction extends Function< Function<?,?>, Function<?,?>> {}

public abstract class OrdinarySum<N extends Number> implements Function<N,N>{
    protected Class<Dom> classDom;
    public OrdinarySum(Class<Dom> classDom){ this.classDom = classDom; }

    public static OrdinarySum<Byte> BYTE_SUM = new OrdinarySum(Byte.class){ public Byte apply(Byte x, Byte y) { return x+y; };
    public static OrdinarySum<Short> SHORT_SUM = new OrdinarySum(Short.class){ public Short apply(Short x, Short y) { return x+y; };
    public static OrdinarySum<Integer> INT_SUM = new OrdinarySum(Integer.class){ public Integer apply(Integer x, Integer y) { return x+y; };
    public static OrdinarySum<Float> FLOAT_SUM = new OrdinarySum(Float.class){ public Float apply(Float x, Float y) { return x+y; };
    public static OrdinarySum<Double> DOUBLE_SUM = new OrdinarySum(Double.class){ public Double apply(Double x, Double y) { return x+y; };
    public static OrdinarySum<Long> LONG_SUM = new OrdinarySum(Long.class){ public Long apply(Long x, Long y) { return x+y; };
    public static OrdinarySum<BigInteger> BIG_INT_SUM = new OrdinarySum(BigInteger.class){ public BigInteger apply(BigInteger x, BigInteger y) { return x.sum(y); };
    public static OrdinarySum<Decimal> DECIMAL_SUM = new OrdinarySum(Decimal.class){ public Decimal apply(Decimal x, Decimal y) { return x.sum(y); };
    private static interface ClassToNumAdder<Nn extends Number> extends Map<Class<Nn>, OrdinarySum<Nn>>{}
    private static final ClassToNumAdder<?> adders = Hashmap<>(8);
    {
        adders.put(Byte.class, BYTE_SUM);
        adders.put(Short.class, SHORT_SUM);
        adders.put(Integer.class, INT_SUM);
        adders.put(Float.class, FLOAT_SUM);
        adders.put(Double.class, DOUBLE_SUM);
        adders.put(Long.class, LONG_SUM);
        adders.put(BigInteger.class, BIG_INT_SUM);
        adders.put(Decimal.class, DECIMAL_SUM);
    }
    public static <T extends Number> OrdinarySum<T> fromNumClassOrDefault(Class<T> clazz, OrdinarySum<T> defaultAdder){
        return adders.containsKey(clazz) ? (OrdinarySum<T>) adders.get(clazz) : defaultAdder;
    }
}
public enum AnalyticFunctions implements Function<?,?>{
    Sum(/*...*/), Mult(/*...*/), /*...,*/ Sin(/*...*/) //, ... 
}

public class SumOfFunctions<Dom extends Dominion<T>, Codom extends Dominion<Q>> implements Function< HomogeneousArgumentList<Function<Dom,Codom>>, Function<Dom,Codom>>{
    public final Class<Dom> classDom;
    public final OrdinarySum<Codom> adder;
    public SumOfFunctions(Class<Dom> classDom, OrdinarySum<Codom> adder){ this.classDom=classDom; this.adder = adder; }
    public Function<Dom,Codom> apply(HomogeneousArgumentList<Function<Dom,Codom>> functions){
        List<Function<Dom,Codom>> results;
        Function<Dom,Codom> f = null;
        Iterator<Function<Dom,Codom>> iter;
        results = new ArrayList<>(functions.size());
        iter = functions.iterator();
        if(functions.size() == 0) return f;
        f = iter.next();
        while(iter.hasNext()){
            f = adder.apply(iter.next());
        }
        return f;
    }
}


abstract class Derivation<T extends Argument> extends FunctionalFunction{
    public abstract Function<T> derivate();
}

class FunctionalDerivative<F extends Function> extends Derivation<F>{
    

    public Function<?,?> apply(Pair<Function> derivandaAndFunzDerivatrcice){
        Function<?,?> derivanda,  funzDerivatrcice;
        derivanda = derivandaAndFunzDerivatrcice.first();
        funzDerivatrcice = derivandaAndFunzDerivatrcice.second();
        funzDerivatrcice

        DefaultDerivation.derivateBy()
    }

/*
    public AnalyticFunction functionalDev(AnalyticFunction derivanda, AnalyticFunction  funzDerivatrcice, Map<String, Number> derivationValues){
        
        List<Argument> obtainedValues
        funzDerivatrcice.forEachArguments(
            varOrFuncArgument -> {
                value
            }
        );

        return this.appl
    }
    */
}