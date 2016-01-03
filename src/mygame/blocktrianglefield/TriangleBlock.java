package mygame.blocktrianglefield;

import com.jme3.math.Vector4f;
import mygame.math.Vector3;

public class TriangleBlock {

    private Vector3 position;
    private Vector4f color;

    public TriangleBlock(Vector3 position, Vector4f color) {
        this.position = position;
        this.color = color;
    }

    /**
     * @return the position
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Vector3 position) {
        this.position = position;
    }

    /**
     * @return the color
     */
    public Vector4f getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Vector4f color) {
        this.color = color;
    }
}