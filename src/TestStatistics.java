/**
 * A class to get binary classification statistics on target data.
 * Consider: whole = positive
 * Consider: broken = negative
 */
public class TestStatistics {
    final private int trueWhole;
    final private int numWhole;
    final private int trueBroken;
    final private int numBroken;


    public TestStatistics(int trueWhole, int numWhole, int trueBroken, int numBroken){
        this.trueWhole = trueWhole;
        this.trueBroken = trueBroken;
        this.numWhole = numWhole;
        this.numBroken = numBroken;
    }

    /**
     * The number of whole targets that were correctly classified as whole targets
     * @return the number of true wholes
     */
    public int trueWhole() {
        return trueWhole;
    }

    /**
     * The number of broken targets that were incorrectly classified as whole
     * @return the number of false wholes
     */
    public int falseWhole(){
        return numBrokenSamples() - trueBroken();
    }

    /**
     * The number of broken targets that were correctly classified as broken
     * @return the number of true broken
     */
    public int trueBroken() {
        return trueBroken;
    }

    /**
     * The number of whole targets that were incorrectly classified as broken
     * @return the number of false brokens
     */
    public int falseBroken(){
        return numWholeSamples() - trueWhole();
    }

    /**
     * The total number of samples collected on whole targets
     * @return the number of whole samples
     */
    public int numWholeSamples() {
        return numWhole;
    }

    /**
     * The total number of samples collected on broken targets
     * @return the number of broken samples
     */
    public int numBrokenSamples() {
        return numBroken;
    }

    /**
     * The total number of samples collected during the test
     * @return the total number of samples
     */
    public int numTotalSamples(){
        return numWholeSamples() + numBrokenSamples();
    }

    /**
     * Out of all samples, how many of them were classified correctly
     * @return the accuracy of the test
     */
    public double accuracy(){
        return (1.0 * trueBroken() + trueWhole()) / numTotalSamples();
    }

    /**
     * A measure of confidence in the true wholes (positives);
     * The percentage of the targets classified as whole that were actually whole
     * @return the precision of the test
     */
    public double precision(){
        return (1.0 * trueWhole()) / (trueWhole() + falseWhole());
    }

    /**
     * A measurement of how many true whole targets were missed.
     * Out of all targets that were really whole, how many of them were correctly classified?
     * @return the recall of the test
     */
    public double recall(){
        return 1.0 * trueWhole() / numWholeSamples();
    }

    /**
     * A measurement of how many true broken targets were missed.
     * Out of all targets that were really broken, how many of them were correctly classified?
     * @return the specificity of the test
     */
    public double specificity(){
        return 1.0 * trueBroken() / numBrokenSamples();
    }

    /**
     * A harmonic mean of precision and recall weighted one way or the other.
     * Can be weighted to prefer false wholes or false brokens
     * @param b the beta weight
     * @return the f-beta score
     */
    public double fBetaScore(double b){
        return (1 + b*b) * precision() * recall() / (b*b*precision() + recall());
    }

    @Override
    public String toString() {
        return "TestStatistics{" +
                "trueWhole=" + trueWhole() +
                ", numWhole=" + numWholeSamples() +
                ", trueBroken=" + trueBroken() +
                ", numBroken=" + numBrokenSamples() +
                ", falseWhole=" + falseWhole() +
                ", falseBroken=" + falseBroken() +
                ", accuracy=" + accuracy() +
                ", precision=" + precision() +
                ", recall=" + recall() +
                ", specificity=" + specificity() +
                '}';
    }
}
