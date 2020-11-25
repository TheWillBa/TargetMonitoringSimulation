/**
 * A class that will test the performance of any <code>BreakageClassifier</code>
 * with any <code>TargetGenerator</code>
 */
public class BreakageClassifierTester {
    private final BreakageClassifier _classifier;
    private final TargetGenerator _generator;

    public BreakageClassifierTester(BreakageClassifier classifier, TargetGenerator generator){
        _classifier = classifier;
        _generator = generator;
    }

    /**
     * Tests the classifier with <code>numSamples</code> broken targets and whole targets
     * @param numSamples the number of broken and whole targets each to be used for testing
     * @return a <code>TestStatistics</code> object containing the results from the test
     */
    public TestStatistics test(int numSamples){
        return test(numSamples, numSamples);
    }

    /**
     * Tests the classifier with <code>numBrokenTargets</code> broken targets and <code>numWholeTargets</code> whole targets
     * @param numWholeTargets the number of whole targets to test with
     * @param numBrokenTargets the number of broken targets to test with
     * @return a <code>TestStatistics</code> object containing the results from the test
     */
    public TestStatistics test(int numWholeTargets, int numBrokenTargets){
        int trueWhole = 0;
        for(int i = 0; i < numWholeTargets; i++){
            Target t = _generator.getWholeTarget(); // Add random variation here?
            if(_classifier.isWhole(t)) trueWhole++;
        }

        int trueBroken = 0;
        for(int i = 0; i < numBrokenTargets; i++){
            Target t = _generator.getBrokenTarget(); // Add random variation here?
            if(_classifier.isBroken(t)) trueBroken++;
        }

        return new TestStatistics(trueWhole, numWholeTargets, trueBroken, numBrokenTargets);
    }


}
