/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.blocktrianglefield;

import com.jme3.scene.Spatial;
import mygame.math.Vector3;

/**
 *
 * @author Jasper
 */
public interface BlockTriangleField {

    Spatial getField();

    void setBlock(TriangleBlock position);

    void removeBlock(Vector3 position);

    void setActive(boolean active);
}
