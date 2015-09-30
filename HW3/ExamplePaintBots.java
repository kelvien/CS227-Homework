package hw3;
import paintbots.PaintbotControl;
import paintbots.PaintbotSimulation;

/**
 * Main class for starting up the paintbot simulation UI.
 */
public class ExamplePaintBots
{ 
  public static void main(String [] args )
  {
    PaintbotControl[] pcarr = new PaintbotControl[1];
    
    // Create the paintbot
    pcarr[0] = new TestPaintbot();
    
    // Start the UI
    PaintbotSimulation.createSimulation(pcarr);
  }
  
}
