package hw3;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import paintbots.BoardSquare;
import paintbots.InternalLocalScan;
import paintbots.LocalScan;
import paintbots.LongRangeScan;
import paintbots.MoveRequest;
import paintbots.MoveType;
import paintbots.PaintbotControl;
import paintbots.RelativeDirection;
import paintbots.SquareType;
import paintbots.Tactic;
import static paintbots.MoveType.*;

public class TestPaintbot extends PaintbotControl
{
  private Color myColor; 
  private Tactic currentTactic; 
  private int count = 0;
  
  public MoveRequest getMove(LocalScan scan, LongRangeScan longrangescan)
  {
    ++count;
    
    // wander around for 5 moves, then spin for 5 moves
    if (count % 10 < 5)
    {
      currentTactic = new WanderingTactic();
    }
    else
    {
      currentTactic = new SpinTactic();
    }
     return currentTactic.generateMove(scan, null);
  }


  @Override
  public void reset(Color c, int seed)
  {
    myColor = c;
  }

  @Override
  public String getRobotName()
  {
    return "Test";
  }

  @Override
  public String getStudentLastName()
  {
    return "Kautz";
  }

  @Override
  public String getStudentID()
  {
    return "123456789";
  }

  @Override
  public boolean tournamentEntry()
  {
    return true;
  }

  /**
   * Tactic that always moves forward until the paintbot
   * hits an obstacle, then turns right.
   */
  public class WanderingTactic implements Tactic
  {
    @Override
    public MoveRequest generateMove(LocalScan scan,
        LongRangeScan longRange)
    {
      MoveRequest mr;
      
      // square in front of us is row -1, column 0
      BoardSquare ahead = scan.get(-1, 0);
      
      if (ahead.getSquareType() != SquareType.ROCK &&
          ahead.getSquareType() != SquareType.WALL)
      {
        mr = new MoveRequest(FORWARD);
      }
      else 
      {
        // turn right instead
        mr = new MoveRequest(ROTATE_RIGHT);
      }
      return mr;
    }   
  }
  
  /**
   * Tactic that spins in place while firing.
   */
  public class SpinTactic implements Tactic
  {
    @Override
    public MoveRequest generateMove(LocalScan scan,
        LongRangeScan longRange)
    {
      return new MoveRequest(ROTATE_LEFT, true);
    }   
  }

  @Override
  public Point findNearestEnemySquare(BoardSquare[][] longscan)
  {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Point findEnemy(BoardSquare[][] longscan)
  {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public int countMySquares(BoardSquare[][] longscan)
  {
    // TODO Auto-generated method stub
    return 0;
  }


  @Override
  public int countEnemySquares(BoardSquare[][] longscan)
  {
    // TODO Auto-generated method stub
    return 0;
  }


  @Override
  public boolean isEnemyNearby(LocalScan scan)
  {
    // TODO Auto-generated method stub
    return false;
  }


  @Override
  public boolean isEnemyPresent(RelativeDirection direction, LocalScan scan)
  {
    // TODO Auto-generated method stub
    return false;
  }


  @Override
  public boolean isObstaclePresent(RelativeDirection d, LocalScan scan)
  {
    // TODO Auto-generated method stub
    return false;
  }
  
}
