import javax.swing.*;
import java.awt.*;

public class TargetVisualizer extends JPanel {

    public TargetVisualizer() {

    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int diameter = 108; //mm
        int speed = 2; //mph
        int numLasers = 9;
        int spacing = 35;
        int numEdges = 32;

        TargetGenerator generator = new ManySmallPiecesGenerator(numEdges, diameter, speed);
        LaserDataCollector collector = new InterruptLaserDataCollector(numLasers, spacing);
        LaserDataAnalyzer analyzer = new BasicLaserDataAnalyzer(diameter, spacing);
        BreakageClassifier classifier = new LaserBreakageClassifier(collector, analyzer);
        BreakageClassifierTester tester = new BreakageClassifierTester(classifier, generator);
        Target target = generator.getBrokenTarget(150, 150);
        paintTarget(target, g);
    }

    private void paintTarget(Target t, Graphics g){
        g.setColor(Color.orange);
        for(Piece p : t.getPieces()){
            g.fillPolygon(p.getXs(), p.getYs(), p.getXs().length);
        }
    }
}
