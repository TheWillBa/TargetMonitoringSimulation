public class BreakageClassifierTester {
    private BreakageClassifier _classifier;
    private TargetGenerator _generator;

    public BreakageClassifierTester(BreakageClassifier classifier, TargetGenerator generator){
        _classifier = classifier;
        _generator = generator;
    }

    public TestStatistics test(int numSamples){
        return test(numSamples, numSamples);
    }

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
