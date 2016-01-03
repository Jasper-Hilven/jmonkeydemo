package mygame;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class TurningBlockController extends AbstractControl{
 
    @Override
    protected void controlUpdate(float tpf) {
        Quaternion smallRotation = new Quaternion();
        smallRotation.fromAngles(0, tpf, 0);
        this.spatial.setLocalRotation(spatial.getLocalRotation().mult(smallRotation));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
  

}