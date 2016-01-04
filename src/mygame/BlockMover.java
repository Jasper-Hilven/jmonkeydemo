package mygame;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.blocktrianglefield.MultiBlockTriangleField;
import mygame.math.HexaDirection;
import mygame.math.HexaHelper;
import mygame.math.Vector3;

public class BlockMover {

    private final Vector3 zMinPosition;
    private final Vector3[] movingPositions;
    private final Spatial[] blocks;
    private final int barHeight;
    private final MultiBlockTriangleField field;
    private final Node node;

    public BlockMover(Vector3 zMinPosition, int barHeight, MultiBlockTriangleField field, Node node) {
        if(!zMinPosition.isEvenXZ())
            throw new Error();
        this.zMinPosition = zMinPosition;
        this.barHeight = barHeight;
        this.field = field;
        this.movingPositions = new Vector3[6];
        movingPositions[0] = zMinPosition;
        movingPositions[1] = HexaHelper.moveIntoDirection(zMinPosition, 1, HexaDirection.ZPlusLeft);
        movingPositions[2] = HexaHelper.moveIntoDirection(movingPositions[1], 1, HexaDirection.ZPlus);
        movingPositions[3] = HexaHelper.moveIntoDirection(movingPositions[2], 1, HexaDirection.ZPlusRight);
        movingPositions[4] = HexaHelper.moveIntoDirection(movingPositions[2], 1, HexaDirection.ZMinRight);
        movingPositions[5] = HexaHelper.moveIntoDirection(movingPositions[2], 1, HexaDirection.ZMin);
        this.blocks = new Spatial[6];
        this.node = node;
    }
    private void stealTheBlocks(){
        for (int i = 0; i < 6; i++)
        {blocks[i] = field.takeBlock(movingPositions[i]);
         if(blocks[i] != null)
             node.attachChild(blocks[i]);
            //Todo fix relative position and orientation
        }

    
    }
    
    
    
}