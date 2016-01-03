package mygame.math;

import static mygame.math.HexaDirection.ZMin;
import static mygame.math.HexaDirection.ZPlus;
import static mygame.math.HexaDirection.ZPlusLeft;
import static mygame.math.HexaDirection.ZPlusRight;


public class HexaHelper{
  
  public static boolean isBlockUp(Vector3 position){
    return position.isEvenXZ();
  }
  public static Vector3 moveIntoDirection(Vector3 position, int stepSize, HexaDirection direction){
    boolean even = (stepSize % 2 == 0);
    int nBDoubleSteps = stepSize / 2;
    Vector3 aDPL = Vector3.Null;
    switch(direction){
        case ZMin:
          return position.add(0,0,-stepSize);
        case ZPlus:
          return position.add(0,0,stepSize);
        case ZPlusLeft:
          aDPL = position.add(nBDoubleSteps*3,0,nBDoubleSteps);
          break;
        case ZPlusRight:
          aDPL = position.add(-nBDoubleSteps*3,0,nBDoubleSteps);
          break;
        case ZMinLeft:
          aDPL = position.add(nBDoubleSteps*3,0,-nBDoubleSteps);
          break;
        case ZMinRight:
          aDPL = position.add(-nBDoubleSteps*3,0,-nBDoubleSteps);
          break;
    }
    return even? aDPL : moveIntoDirectionSingleStep(aDPL, direction);   
  }
  private static Vector3 moveIntoDirectionSingleStep(Vector3 position, HexaDirection direction){
   boolean even = position.isEvenXZ();
    switch(direction){
        case ZMin:
          return position.add(0,0,-1);
        case ZPlus:
          return position.add(0,0,1);
        case ZPlusLeft:
            return even? position.add(1, 0, 0) : position.add(2, 0, 1);
        case ZPlusRight:
            return even? position.add(-1, 0, 0) : position.add(-2, 0, 1);
        case ZMinLeft:
            return even? position.add(2, 0, -1): position.add(1, 0, 0);
        case ZMinRight:
            return even?  position.add(-2, 0, -1) : position.add(-1, 0, 0);
  }
          throw new Error();  
  }
}
