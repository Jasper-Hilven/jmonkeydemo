package mygame;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.blocktrianglefield.BlockTriangleFieldHelper;
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
  float actionTime = 0f;
  boolean stoleBlocks = false;

  public BlockMover(Vector3 zMinPosition, int barHeight, MultiBlockTriangleField field) {
    if (!zMinPosition.isEvenXZ()) {
      throw new Error();
    }
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
    this.node = new Node();
  }

  public Node getNode() {
    return node;
  }

  public void Update(float tpf) {
    actionTime += tpf;
    if (actionTime > 8f) {
      actionTime -= 8f;
    }
    if (2f > actionTime) {
      node.setLocalRotation(Matrix3f.IDENTITY);
    }
    if (actionTime >= 2f && 4f > actionTime && !stoleBlocks) {
      stealTheBlocks();
      return;
    }
    if (actionTime >= 4f && 6f > actionTime && stoleBlocks) {
      return;
    }
    if (actionTime >= 6f && 8f > actionTime && stoleBlocks) {
      putTheBlocksBack();
      return;
    }

  }

  private void stealTheBlocks() {
    if (stoleBlocks) {
      throw new Error();
    }
    Vector3f nodeWorldPosition = node.getWorldTranslation();
    for (int i = 0; i < 6; i++) {
      Vector3 stealPosition = movingPositions[i];
      blocks[i] = field.takeBlock(stealPosition);
      Vector3f blockWorldPosition =
              BlockTriangleFieldHelper.getWorldPosition(stealPosition);
      if (blocks[i] != null) {
        node.attachChild(blocks[i]);
        blocks[i].setLocalTranslation(
                blockWorldPosition.subtract(nodeWorldPosition));
      }
    }
    stoleBlocks = true;
  }

  private void putTheBlocksBack() {
    if (!stoleBlocks) {
      throw new Error();
    }

    for (int i = 0; i < 6; i++) {
      if (blocks[i] == null) {
        continue;
      }

      node.detachChild(blocks[i]);
      field.setExistingBlock(movingPositions[(i + 1) % 6], node);
    }
    stoleBlocks = false;
  }
}