package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import java.util.Random;

public class LevelBuilder{

    public void setUpLevel(Node rootNode, AssetManager assetManager, BulletAppState bulletAppState) {
        SingleBlockTriangleField tField = new SingleBlockTriangleField(assetManager,bulletAppState.getPhysicsSpace());
        rootNode.attachChild(tField.getField());
        int radius = 32;
        int firstLevel = 8;
        int secondLevel = 12;
        int thirdLevel = 16;
        int fourthLevel = 20;
        int outRadius = radius + 1;
        int ceilingHeight = 5;
        Vector3 beginPosition = new Vector3();
        Random r = new Random();
        setFilledHexagon(radius, tField, beginPosition, r);
        setFilledHexagon(radius, tField, beginPosition.add(new Vector3(0, ceilingHeight, 0)), r);
        for(int i = 1; i < ceilingHeight; i++)
        {
            setHollowHexagon(firstLevel, tField, beginPosition.add(new Vector3(0, i, 0)), r);
            setHollowHexagon(secondLevel, tField, beginPosition.add(new Vector3(0, i, 0)), r);
            setHollowHexagon(thirdLevel, tField, beginPosition.add(new Vector3(0, i, 0)), r);
            setHollowHexagon(fourthLevel, tField, beginPosition.add(new Vector3(0, i, 0)), r);
            setHollowHexagon(outRadius, tField, beginPosition.add(new Vector3(0, i, 0)), r);
            putDoorInHexagon(fourthLevel, tField, beginPosition.add(new Vector3(0, i, 0)), HexaDirection.ZMinRight);
            putDoorInHexagon(secondLevel, tField, beginPosition.add(new Vector3(0, i, 0)), HexaDirection.ZMin);
            putDoorInHexagon(thirdLevel, tField, beginPosition.add(new Vector3(0, i, 0)), HexaDirection.ZMinLeft);
            putDoorInHexagon(fourthLevel, tField, beginPosition.add(new Vector3(0, i, 0)), HexaDirection.ZPlusRight);
            putDoorInHexagon(thirdLevel, tField, beginPosition.add(new Vector3(0, i, 0)), HexaDirection.ZPlus);
            putDoorInHexagon(firstLevel, tField, beginPosition.add(new Vector3(0, i, 0)), HexaDirection.ZPlusLeft);
            
            //setHollowHexagon(8, tField, beginPosition.add(new Vector3(0, i, 0)), r);
            //setHollowHexagon(16, tField, beginPosition.add(new Vector3(0, i, 0)), r);
            //setHollowHexagon(32, tField, beginPosition.add(new Vector3(0, i, 0)), r);
        }
        
        
        setUpUglyAxisInSky(tField, r);
        tField.setActive(true);
    
    }
    
    private void setUpUglyAxisInSky(SingleBlockTriangleField tField, Random r) {
        int height = 1;
        tField.setBlock(new TriangleBlock(new Vector3(0,height,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(1,height,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(2,height,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(3,height,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(4,height,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(0,height,1), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(0,height,2), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
    }
    private Vector4f getColor(Vector3 position){
      return new Vector4f(FastMath.cos((2*position.x+(position.y + position.z))/10),FastMath.cos((2*position.x-(position.y + position.z))/10),FastMath.cos((position.y + position.z)/10),1f);
    //return new Vector4f(FastMath.cos((2*position.x+(position.y + position.z))),FastMath.cos((2*position.x-(position.y + position.z))),FastMath.cos((position.y + position.z)),1f);
    
    }

    private void putDoorInHexagon(int radius, SingleBlockTriangleField tField, Vector3 beginPosition, HexaDirection direction){
        if(direction == HexaDirection.ZPlus ||direction == HexaDirection.ZMinLeft|| direction == HexaDirection.ZPlusLeft)
            radius -= 1;
        if(!beginPosition.isEvenXZ())
            throw new Error();
        Vector3 updatedPosition = HexaHelper.moveIntoDirection(beginPosition, radius, direction);
        tField.removeBlock(updatedPosition);
        tField.removeBlock(HexaHelper.moveIntoDirection(updatedPosition, 1, HexaDirection.ZMin));
        tField.removeBlock(HexaHelper.moveIntoDirection(updatedPosition, 1, HexaDirection.ZMinLeft));
        tField.removeBlock(HexaHelper.moveIntoDirection(updatedPosition, 1, HexaDirection.ZMinRight));
        tField.removeBlock(HexaHelper.moveIntoDirection(updatedPosition, 1, HexaDirection.ZPlusLeft));
        tField.removeBlock(HexaHelper.moveIntoDirection(updatedPosition, 1, HexaDirection.ZPlus));
        tField.removeBlock(HexaHelper.moveIntoDirection(updatedPosition, 1, HexaDirection.ZPlusRight));
        tField.removeBlock(updatedPosition.add(1,0,0));
        tField.removeBlock(updatedPosition.add(-1,0,0));
        tField.removeBlock(updatedPosition.add(1,0,1));
        tField.removeBlock(updatedPosition.add(-1,0,1));
        tField.removeBlock(updatedPosition.add(1,0,-1));
        tField.removeBlock(updatedPosition.add(-1,0,-1));
        tField.removeBlock(updatedPosition.add(2,0,0));
        tField.removeBlock(updatedPosition.add(-2,0,0));
    }
    
    private void setHollowHexagon(int radius,SingleBlockTriangleField tField, Vector3 beginPosition, Random r){
        
        for (int x = -2*radius; x <= 2*radius; x+= 1){
              for (int z = -radius; z < radius; z+= 1) {
                if(z < -radius || z > radius - 1)
                    continue;
                if(x - z < -radius*2 || x - z > radius*2 - 1)
                    continue;
                if(x + z > radius*2 -2|| x + z <-2*radius -1)
                    continue;
                if(     (z >= -(radius-1)) && (z <= (radius-1) - 1) && 
                        (x - z >= -(radius-1)*2) && (x - z <= (radius-1)*2 - 1) && 
                        (x + z <= (radius-1)*2 -2) && (x + z >=-2*(radius-1) -1)    ) 
                    continue;
                Vector3 pos = beginPosition.add(new Vector3(x,0,z));
                  tField.setBlock(new TriangleBlock(pos, getColor(pos)));
              }
        }
    
    
    }
    private void setFilledHexagon(int radius, SingleBlockTriangleField tField, Vector3 beginPosition, Random r) {
        for (int x = -2*radius; x <= 2*radius; x+= 1){
              for (int z = -radius; z < radius; z+= 1) {
                if(z < -radius || z > radius - 1)
                    continue;
                if(x - z < -radius*2 || x - z > radius*2 - 1)
                    continue;
                if(x + z > radius*2 -2|| x + z <-2*radius -1)
                    continue;
                Vector3 pos = beginPosition.add(new Vector3(x,0,z));
                  tField.setBlock(new TriangleBlock(pos, getColor(pos)));
              }
        }
    }
}