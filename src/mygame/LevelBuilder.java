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
        int radius = 45;
        int ceilingHeight = 7;
        Vector3 beginPosition = new Vector3();
        Random r = new Random();
        setFilledHexagon(radius, tField, beginPosition, r);
        setFilledHexagon(radius, tField, beginPosition.add(new Vector3(0, ceilingHeight, 0)), r);
        for (int i = 1; i < ceilingHeight; i++)
        {
            setHollowHexagon(radius+1, tField, beginPosition.add(new Vector3(0, i, 0)), r);
            setHollowHexagon(17, tField, beginPosition.add(new Vector3(0, i, 0)), r);
        }
        setUpUglyAxisInSky(tField, r);
        
        
        tField.setActive(true);
    
    }
    
    private void setUpUglyAxisInSky(SingleBlockTriangleField tField, Random r) {
        tField.setBlock(new TriangleBlock(new Vector3(0,10,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(1,10,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(2,10,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(3,10,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(4,10,0), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(0,10,1), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
        tField.setBlock(new TriangleBlock(new Vector3(0,10,2), new Vector4f(r.nextFloat(), 0.5f, r.nextFloat(), 1)));
    }
    private static Vector4f black = new Vector4f(0f,0f,0f,1f);
    private Vector4f getColor(Vector3 position){
        if(position.y > 7)
            return black;
        return new Vector4f(FastMath.cos((2*position.x+(position.y + position.z))/10),FastMath.cos((2*position.x-(position.y + position.z))/10),FastMath.cos((position.y + position.z)/10),1f);
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