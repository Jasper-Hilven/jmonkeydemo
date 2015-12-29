package mygame;

import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

class FlyingLight{
  private Vector3f circularPosition;
  private float radius;
  private float currentAngle;
  private PointLight lamp_light;
    private final float speed;
  
  public FlyingLight(Vector3f circularPosition, float radius, float speed){
    this.speed=  speed;
      this.radius = radius;
    this.circularPosition = circularPosition;
      lamp_light = new PointLight();
    lamp_light.setColor(ColorRGBA.Orange);
    lamp_light.setRadius(40f);
    lamp_light.setPosition(circularPosition.add(new Vector3f(radius,0,0)));
  }
  
  public Light getLamp(){
    return lamp_light;
  }
  
  public void Update(float tpf){
    currentAngle += tpf*speed;
    if(currentAngle > 16*FastMath.PI)
      currentAngle -= 16*FastMath.PI;
    lamp_light.setPosition(circularPosition.add(new Vector3f(radius * FastMath.cos(currentAngle),0f,radius *FastMath.sin(currentAngle))));
  }






}