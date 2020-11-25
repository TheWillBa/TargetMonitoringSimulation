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

    public int getTrueWhole() {
        return trueWhole;
    }

    public int getFalseWhole(){
        return getTrueBroken() - getNumBroken();
    }

    public int getTrueBroken() {
        return trueBroken;
    }

    public int getFalseBroken(){
        return getTrueWhole() - getNumWhole();
    }

    public int getNumWhole() {
        return numWhole;
    }

    public int getNumBroken() {
        return numBroken;
    }
}
