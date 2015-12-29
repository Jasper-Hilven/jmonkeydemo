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
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener{

  private BulletAppState bulletAppState;
  private CharacterControl player;
  private Vector3f walkDirection = new Vector3f();
  private BlockTriangleField tField;
  private boolean left = false, right = false, up = false, down = false;
 
  //Temporary vectors used on each frame.
  //They here to avoid instanciating new vectors on each frame
  private Vector3f camDir = new Vector3f(1,-1,0);
  private Vector3f camLeft = new Vector3f();
 
  public static void main(String[] args) {
    Main app = new Main();
    app.start();
  }
 
  public void simpleInitApp() {
    /** Set up Physics */
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
 
    // We re-use the flyby camera for rotation, while positioning is handled by physics
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    flyCam.setMoveSpeed(100);
    setUpKeys();
    setUpLight();
 
    // We load the scene from the zip file and adjust its size.
    
    // We set up collision detection for the scene by creating a
    // compound collision shape and a static RigidBodyControl with mass zero.
    tField = new SingleBlockTriangleField(assetManager,bulletAppState.getPhysicsSpace());
    rootNode.attachChild(tField.getField());
    int maxX = 100;
    int maxY = 100;
    int maxZ = 100;
    /*for (int x = 0; x < maxX; x+= 1) {
          for (int y = 0; y < maxY; y+= 1){
            tField.setBlock(new Vector3(x,y,0));
            tField.setBlock(new Vector3(x,y,maxZ-1));
          }
      }*/
    for (int x = -100; x < 100; x+= 1){
          for (int z = -100; z < 100; z+= 1) {
              tField.setBlock(new TriangleBlock(new Vector3(x,0,z), new Vector4f((10+(x % 10))/10f, (10+(x+z % 10))/10f, (10+(z % 10))/10f, 1)));
            //tField.setBlock(new Vector3(0,y,z));
            //tField.setBlock(new Vector3(maxX-1,y,z));
          }
    }
    for(int y = 1; y < 5; y++){
    for(int wallPart = 0; wallPart < 50; wallPart++){
        for(int z = 0; z < 4; z++){
        tField.setBlock(new TriangleBlock(new Vector3(-wallPart,y,z), new Vector4f(0.5f, 0.5f, 0.5f, 1f)));
        }
    
    
    }
    }
    tField.setActive(true);
    // We set up collision detection for the player by creating
    // a capsule collision shape and a CharacterControl.
    // The CharacterControl offers extra settings for
    // size, stepheight, jumping, falling, and gravity.
    // We also put the player in its starting position.
    CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1f, 1.7f, 1);
    player = new CharacterControl(capsuleShape, 0.05f);
    
    player.setPhysicsLocation(new Vector3f(0, 10, 0));
    player.setJumpSpeed(0f);
    // We attach the scene and the player to the rootnode and the physics space,
    // to make them appear in the game world.
    bulletAppState.getPhysicsSpace().add(player);
  }
 
  private void setUpLight() {
    // We add light so we see the scene
    AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.White.mult(0.2f));
    rootNode.addLight(al);
 
    DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.White);
    dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
    rootNode.addLight(dl);
  
    final int SHADOWMAP_SIZE=1024;
    DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
    dlsr.setLight(dl);
    viewPort.addProcessor(dlsr);
     
    DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
    dlsf.setLight(dl);
    dlsf.setEnabled(true);
    FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
    fpp.addFilter(dlsf);
    viewPort.addProcessor(fpp);
  
  }
 
  /** We over-write some navigational key mappings here, so we can
   * add physics-controlled walking and jumping: */
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
 
  /** These are our custom actions triggered by key presses.
   * We do not walk yet, we just keep track of the direction the user pressed. */
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
 
  /**
   * This is the main event loop--walking happens here.
   * We check in which direction the player is walking by interpreting
   * the camera direction forward (camDir) and to the side (camLeft).
   * The setWalkDirection() command is what lets a physics-controlled player walk.
   * We also make sure here that the camera moves with player.
   */
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
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
    }
  
}