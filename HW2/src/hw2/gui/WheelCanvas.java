package hw2.gui;

import hw2.Wheel;
import hw2.WheelOfFortuneGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Custom panel for animating the spinning of the wheel. This
 * is based on the wheel image from Wikipedia:
 * 
 * http://en.wikipedia.org/wiki/File:WheelofFortuneSeason30-Round4.png
 * 
 * which should ideally be scaled down to fit the window, presumably
 * WheelOfFortuneUI.WHEEL_SIZE x WheelOfFortuneUI.WHEEL_SIZE.
 * Note that the wheel image does not quite match what is used in 
 * the game, so we are using the blue 700 as 750 and the red 800 
 * as 850 (those amounts aren't present on the image).  
 */
public class WheelCanvas extends JPanel
{
  /**
   * Milliseconds between frames, about 30 fps.
   */
  private static final int SPEED = 40;
  
  /**
   * Degrees of rotation per frame; this number must divide
   * evenly into 360.
   */
  private static final int INCREMENT = 5;
  
  /**
   * Minimum spin time in millis.
   */
  private static final int MIN_SPIN_TIME = 2000;  
  
  /**
   * Clockwise rotation (in degrees) of image that is needed to get 
   * to dollar amounts from 300 to 900. 
   * Also need to add 7.5 degrees to center the wedge 
   * vertically.
   */ 
  private static final int[] ROTATIONS = {
    30,  //  $300
    210, // $350
    255,
    60,
    45, 
    270,
    225,
    150,
    315,
    120,
    285,
    90,
    180   // $900
  };
  
  /**
   * Clockwise rotation (in degrees) of image that is needed to get 
   * to a "bankrupt" wedge.
   */ 
  private static final int BANKRUPT_ROTATION = 15;
  
  /**
   * Clockwise rotation (in degrees) of image that is needed to get 
   * to a "lose a turn" wedge.
   */ 
  private static final int LOSE_A_TURN_ROTATION = 105;
  
  /**
   * Clockwise rotation (in degrees) of image that is needed to get 
   * to a "$5000" wedge.
   */   
  private static final int FIVE_THOUSAND_ROTATION = 0;
  
  /**
   * Additional rotation needed to line up wedge vertically.
   */
  private static final double OFFSET = 7.5;
  
  /**
   * Reference to the main UI panel, since we have to notify it when
   * the animation is complete.
   */
  private WheelOfFortuneUI ui;
  
  /**
   * Number of animation steps remaining.
   */
  private int animationCount = 0;
  
  /**
   * Current rotation of the image.
   */
  private int rotation = BANKRUPT_ROTATION;
  
  /**
   * Reference to the game.
   */
  private WheelOfFortuneGame game;
  
  /**
   * The wheel image.
   */
  private Image image;
  
  /**
   * Seing animation timer.
   */
  private Timer timer;
  
  /**
   * Width of the wheel image.
   */
  private int imageWidth;
  
  /**
   * Height of the wheel image.
   */
  private int imageHeight;
  
  /**
   * Constructs the panel and loads the image.
   * @param ui
   * @param game
   * @param filename
   */
  public WheelCanvas(WheelOfFortuneUI ui, WheelOfFortuneGame game)
  {
    this.ui = ui;
    this.game = game;
    timer = new Timer(SPEED, new WheelCallback());
    URL imageURL = WheelOfFortuneUI.class.getResource(WheelOfFortuneUI.WHEEL_FILENAME);
    System.out.println(imageURL);
    ImageIcon i = null;    
    if (imageURL != null)
    {
      i = new ImageIcon(imageURL);
    }
    
    // Make sure image is loaded
    if (i == null || i.getImageLoadStatus() != MediaTracker.COMPLETE)
    {
      image = null;
    }
    else
    {
      image = i.getImage();
      imageWidth = i.getIconWidth();
      imageHeight = i.getIconHeight();
    }
  }
  
  /**
   * Initiate animation of a wheel spin that will end up with 
   * a wedge corresponding the the game's current wheel state.
   */
  public void animateSpin()
  {
    int totalTime = MIN_SPIN_TIME;
    animationCount = totalTime / SPEED;
    
    // where we end up after animation count
    int partialRotation = (rotation + (animationCount * INCREMENT)) % 360;

    // where we need to end up
    int finalRotation = getRotation(game.getWheelState());
    
    int extra = (finalRotation + 360 - partialRotation) % 360;
    
    animationCount += extra / INCREMENT;
    
    timer.start();
  }
  
  /**
   * Returns true if an animation is in progress.
   * @return
   */
  public boolean isAnimating()
  {
    return animationCount > 0;
  }
  
  @Override
  public void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    int currentRotation = rotation;
    if (!isAnimating())
    {
      currentRotation = getRotation(game.getWheelState());
    }

    g2.setBackground(Color.WHITE);
    g2.clearRect(0, 0, getWidth(), getHeight());
    AffineTransform transform = new AffineTransform();
    transform.translate(imageWidth / 2, imageHeight / 2);
    transform.rotate((currentRotation + OFFSET) * Math.PI / 180.0);
    transform.translate(-imageWidth / 2, -imageHeight / 2);
    if (image != null)
    {
      g2.drawImage(image, transform, this);
    }
    else
    {
      // simulate animation with a rotating cursor
      if (isAnimating())
      {

        String text = "|";
        // try to center it, (x, y) is bottom left corner of text
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 24);
        g2.setFont(font);     
        FontMetrics metrics = g.getFontMetrics(font);
        int height = metrics.getHeight();
        int width = metrics.stringWidth(text);
        int x = (getWidth() - width) / 2;
        int y = (getHeight() + height) / 2; 

        // draw the text
        g2.translate(getWidth() / 2, getHeight() / 2);
        g2.rotate(currentRotation);
        g2.translate(-getWidth() / 2, -getHeight() / 2);
        g2.drawString(text, x, y);
      }

    }
    if (!isAnimating())
    {

      // get the wheel state
      String text;
      int result = game.getWheelState();
      if (result == Wheel.BANKRUPT)
      {
        text = "BANKRUPT";
      }
      else if (result == Wheel.LOSE_A_TURN)
      {
        text = "LOSE TURN";
      }
      else
      {
        text = "$" + result;
      }      

      // try to center it, (x, y) is bottom left corner of text
      Font font = new Font(Font.MONOSPACED, Font.PLAIN, 24);
      g2.setFont(font);     
      FontMetrics metrics = g.getFontMetrics(font);
      int height = metrics.getHeight();
      int width = metrics.stringWidth(text);
      int x = (getWidth() - width) / 2;
      int y = (getHeight() + height) / 2; 

      // draw the text
      g2.drawString(text, x, y);
    }
  }

  /**
   * Return the degrees of rotation required to end up 
   * at a wedge for the given wheel state.
   * @param wheelState
   * @return
   */
  private int getRotation(int wheelState)
  {
    if (wheelState == Wheel.BANKRUPT)
    {
      return BANKRUPT_ROTATION;
    }
    else if (wheelState == Wheel.LOSE_A_TURN)
    {
      return LOSE_A_TURN_ROTATION;
    }
    else if (wheelState == 5000)
    {
      return FIVE_THOUSAND_ROTATION;
    }
    else
    {
      int index = (wheelState / 50) - 6;
      if (index >= 0 && index < ROTATIONS.length)
      {
        return ROTATIONS[index];
      }
    }
    return BANKRUPT_ROTATION; // should never happen
  }

  /**
   * Timer callback.
   */
  private class WheelCallback implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
      rotation = (rotation + INCREMENT) % 360; 

      --animationCount;

      if (animationCount <= 0)
      {
        // done, notify the UI and stop the timer
        ui.wheelReady();
        timer.stop();
      }
      repaint();
    }     
  }


}
