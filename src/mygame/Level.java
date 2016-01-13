/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Node;
import java.util.ArrayList;
import mygame.blocktrianglefield.MultiBlockTriangleField;
import mygame.blocktrianglefield.SingleMeshTriangleField;

/**
 *
 * @author Jasperhilven
 */
public class Level {

  private Node levelNode;
  private MultiBlockTriangleField dynamicField;
  private SingleMeshTriangleField staticField;
  private ArrayList<BlockMover> movers;
  private ArrayList<FlyingLight> lights;

  public Level() {
    this.levelNode = new Node();
    this.movers = new ArrayList<BlockMover>();
    this.lights = new ArrayList<FlyingLight>();
  }

  public void Update(float tpf) {
    for (BlockMover blockMover : movers) {
      blockMover.Update(tpf);
    }
    for (FlyingLight light : lights) {
      light.Update(tpf);
    }
  }

  public void AddLight(FlyingLight light) {
    levelNode.addLight(light.getLamp());
    lights.add(light);
  }

  public void AddMover(BlockMover mover) {
    movers.add(mover);
  }

  /**
   * @return the levelNode
   */
  public Node getLevelNode() {
    return levelNode;
  }

  /**
   * @param dynamicField the dynamicField to set
   */
  public void setDynamicField(MultiBlockTriangleField dynamicField) {
    this.dynamicField = dynamicField;
    levelNode.attachChild(dynamicField.getField());
  }

  /**
   * @param staticField the staticField to set
   */
  public void setStaticField(SingleMeshTriangleField staticField) {
    this.staticField = staticField;
    levelNode.attachChild(staticField.getField());
  }
}
