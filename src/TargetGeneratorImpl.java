public class TargetGeneratorImpl extends AbstractTargetGenerator{

    public TargetGeneratorImpl(int numEdges, int targetRadius){
        super(numEdges, targetRadius);
    }

    @Override
    public Target getBrokenTarget() {
        return null;
    }
}
