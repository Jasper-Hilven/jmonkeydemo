package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class Main extends SimpleApplication implements ActionListener {

  private BulletAppState bulletAppState;
  private CharacterControl player;
  private Vector3f walkDirection = new Vector3f();
  private boolean left = false, right = false, up = false, down = false;
  private Vector3f camDir = new Vector3f(1, -1, 0);
  private Vector3f camLeft = new Vector3f();
  private ScreenshotAppState screenShotState;
  private Level level;  
  
  public static void main(String[] args) {
    Main app = new Main();
    app.start();
  }

  public void simpleInitApp() {
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    setUpKeys();
    setUpLight();
    level = (new LevelBuilder()).buildLevel( assetManager, bulletAppState.getPhysicsSpace());
    rootNode.attachChild(level.getLevelNode());
    setUpPlayer();
  }

  private void setUpLight() {
    viewPort.setBackgroundColor(new ColorRGBA(.2f, .2f, .2f, 1f));
    // We add light so we see the scene
    AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.DarkGray);
    rootNode.addLight(al);
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
      right = isPressed;
    } else if (binding.equals("Up")) {
      up = isPressed;
    } else if (binding.equals("Down")) {
      down = isPressed;
    }
  }
  int updateCount = 0;

  @Override
  public void simpleUpdate(float tpf) {
    updateCount++;
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
    level.Update(tpf);
  }

  private void setUpPlayer() {

    CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1f, 1.2f, 1);
    player = new CharacterControl(capsuleShape, 0.05f);
    player.setPhysicsLocation(new Vector3f(7, 3, 7));
    player.setJumpSpeed(0f);
    bulletAppState.getPhysicsSpace().add(player);
  }
}