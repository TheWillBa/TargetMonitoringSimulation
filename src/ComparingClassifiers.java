import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ComparingClassifiers {



    public static void main(String[] args) {
        final Int _diameter = new Int(108); //mm
        final Int _speed = new Int(2); //mph
        final Int _numLasers = new Int(9);
        final Int _spacing = new Int(35);
        final Int _numEdges = new Int(32);

        TargetGenerator generator = new RandomQuadrantGenerator(_numEdges._value, _diameter._value, _speed._value);
        LaserDataCollector collector = new InterruptLaserDataCollector(_numLasers._value, _spacing._value);

        LaserDataAnalyzer hitOrder = new HitOrderLaserDataAnalyzer(_spacing._value, _diameter._value);
        LaserDataAnalyzer timeHit = new LengthHitLaserDataAnalyzer(_spacing._value, _diameter._value);

        LaserDataAnalyzer A = hitOrder;
        LaserDataAnalyzer B = timeHit;

        int numSamplesEach = 1000;
        List<Trial> trials = new ArrayList<Trial>();

        //region Collection data on same targets

        // Whole targets
        for(int i = 0; i < numSamplesEach; ++i){
            // Pull a target
            Target t = generator.getWholeTarget();
            boolean[][] data = collector.collectDataOnTarget(t);

            // Classify it with both of the analyzers
            boolean a = A.isBroken(data);
            boolean b = B.isBroken(data);

            // Put the trial data to the list
            Trial trial = new Trial();
            trial.a = !a;
            trial.b = !b;

            trials.add(trial);
        }

        // Broken targets
        for(int i = 0; i < numSamplesEach; ++i){
            // Pull a target
            Target t = generator.getBrokenTarget();
            boolean[][] data = collector.collectDataOnTarget(t);

            // Classify it with both of the analyzers
            boolean a = A.isBroken(data);
            boolean b = B.isBroken(data);

            // Put the trial data to the list
            Trial trial = new Trial();
            trial.a = a;
            trial.b = b;

            trials.add(trial);
        }

        //endregion

        int aNumCorrect = 0;
        int bNumCorrect = 0;

        for(Trial t : trials){
            if(t.a) aNumCorrect++;
            if(t.b) bNumCorrect++;
        }

        double aAccuracy = (double) aNumCorrect / trials.size();
        double bAccuracy = (double) bNumCorrect / trials.size();


        System.out.println("A accuracy: " + aAccuracy);
        System.out.println("B accuracy: " + bAccuracy);



        int n = 0; // Number of examples where the classifiers produced different output
        int s = 0; // The number of time A got something right that B got wrong

        // Compute the above values from the data
        for(Trial t : trials){
            boolean a = t.a;
            boolean b = t.b;

            if(a == !b) n++;
            if(a && !b) s++;
        }

        System.out.println("n: "+ n);
        System.out.println("s: "+ s);

        BigDecimal p = new BigDecimal("0");

        BigDecimal nFact = factorial(n);
        BigDecimal halfPowN = new BigDecimal(Double.toString(Math.pow(0.5, n)));

        for(int i = s; i <= n; ++i){
            BigDecimal value = nFact.divide( factorial(i).multiply(factorial(n - i)) ).multiply(halfPowN); //* halfPowN;
            p = p.add(value);


            System.out.println("f(n-i): " + factorial(n - i));
            System.out.println("f(i): " + factorial(i));
            System.out.println("(i): " + (i));
            System.out.println(value);


        }

        System.out.println("One sided p-value: " + p);
        System.out.println("Two sided p-value: " + p.multiply(new BigDecimal("2")));



    }

    private static class Trial{
        boolean a;
        boolean b;
    }

    private static BigDecimal factorial(int n){
        BigDecimal f = new BigDecimal("1");

        for(int i = n; i > 1; --i){
            f = f.multiply(new BigDecimal(Integer.toString(i)));
        }

        return f;
    }
}
