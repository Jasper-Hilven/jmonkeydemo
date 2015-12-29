package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;


public class Main extends SimpleApplication implements ActionListener{

  private BulletAppState bulletAppState;
  private CharacterControl player;
  private Vector3f walkDirection = new Vector3f();
  private boolean left = false,right = false, up = false, down = false;
  private Vector3f camDir = new Vector3f(1,-1,0);
  private Vector3f camLeft = new Vector3f();
 private FlyingLight lamp1;
  private FlyingLight lamp2;

  public static void main(String[] args) {
    Main app = new Main();
    app.start();
  }
 
  public void simpleInitApp() {
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    setUpKeys();
    setUpLight();
    (new LevelBuilder()).setUpLevel(rootNode, assetManager, bulletAppState);
    setUpPlayer();
  }
 
  private void setUpLight() {
    lamp1 = new FlyingLight(new Vector3f(0,6,0),18,2f);    
    lamp2 = new FlyingLight(new Vector3f(0,6,0),18,2.2f);
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    // We add light so we see the scene
    /*AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.DarkGray.mult(0.1f));
    rootNode.addLight(al);
 */
   /* DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.DarkGray.mult(0.1f));
    dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
    rootNode.addLight(dl);
  */
    /*final int SHADOWMAP_SIZE=1024;
    DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
    dlsr.setLight(dl);
    viewPort.addProcessor(dlsr);
     
    DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
    dlsf.setLight(dl);
    dlsf.setEnabled(true);
    FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
    fpp.addFilter(dlsf);
    */ 
    
    rootNode.addLight(lamp1.getLamp());
    rootNode.addLight(lamp2.getLamp());
    
    
    //viewPort.addProcessor(fpp);
  
  }
 
  private void setUpKeys() {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
  }
 
  
  public void onAction(String binding, boolean isPressed, float tpf) {
    if (binding.equals("Left")) {
      left = isPressed;
    } else if (binding.equals("Right")) {
      right= isPressed;
    } else if (binding.equals("Up")) {
      up = isPressed;
    } else if (binding.equals("Down")) {
      down = isPressed;
    }
  }
 
  
  @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        walkDirection.y = 0;
        walkDirection.normalizeLocal();
        walkDirection.divideLocal(6f);
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
        lamp1.Update(tpf);
        lamp2.Update(tpf);
  }

    private void setUpPlayer() {

        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1f, 1.2f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setPhysicsLocation(new Vector3f(0, 3, 0));
        player.setJumpSpeed(0f);
        bulletAppState.getPhysicsSpace().add(player);
    }
}