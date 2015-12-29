package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.TechniqueDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.Control;
import com.jme3.util.BufferUtils;
import java.util.HashMap;

public class MultiBlockTriangleField implements BlockTriangleField{
  private HashMap<Vector3,Geometry> blocks ;
  private Node field;
  private Mesh singleBlockMesh;
  private Material blueBlockMaterial;
  private Material redBlockMaterial;
  private PhysicsSpace world;
  public Spatial getField(){
    return field;
  }
  
  public MultiBlockTriangleField(AssetManager assetManager,PhysicsSpace physics){
    blocks = new HashMap<Vector3, Geometry>();
    field = new Node();
    field.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
    setSingleBlockMesh();
    setBlueBlockMaterial(assetManager);
    setRedBlockMaterial(assetManager);
    this.world = physics;
  }
  
  private void setSingleBlockMesh(){
    Mesh mesh = new Mesh();
    float basicRotation = FastMath.PI/2f;
    float resize = (float) Math.sqrt(1d/3d);
    float x0 = FastMath.cos(basicRotation+(FastMath.PI*0f)/3f) ;
    
    float z0 = FastMath.sin(basicRotation+(FastMath.PI*0f)/3f)+ 0.5f;
    float x1 = FastMath.cos(basicRotation+(FastMath.PI*2f)/3f);
    float z1 = FastMath.sin(basicRotation+(FastMath.PI*2f)/3f)+ 0.5f;
    float x2 = FastMath.cos(basicRotation+(FastMath.PI*4f)/3f);
    float z2 = FastMath.sin(basicRotation+(FastMath.PI*4f)/3f)+ 0.5f;
    float y0 = 0;
    float y1 = 1f/resize;
    Vector3f [] vertices = new Vector3f[18];
    Vector3f[] normals = new Vector3f[18];
    final Vector3f p0 = new Vector3f(x1,y0,z1).mult(resize);
    final Vector3f p1 = new Vector3f(x0,y0,z0).mult(resize);
    final Vector3f p2 = new Vector3f(x2,y0,z2).mult(resize);    
    final Vector3f p3 = new Vector3f(x1,y1,z1).mult(resize);
    final Vector3f p4 = new Vector3f(x0,y1,z0).mult(resize);
    final Vector3f p5 = new Vector3f(x2,y1,z2).mult(resize);
    final Vector3f nDown = new Vector3f(0,-1,0);
    final Vector3f nUp = new Vector3f(0,1,0);
    final Vector3f nFirst = p0.add(p1).divide(2f).subtract(p2).normalize();
    final Vector3f nSecond = p2.add(p0).divide(2f).subtract(p1).normalize();
    final Vector3f nThird = p1.add(p2).divide(2f).subtract(p0).normalize();
    //Lower triangle
    vertices[0] = p0;
    vertices[1] = p1;
    vertices[2] = p2;    
    normals[0] = nDown;
    normals[1] = nDown;
    normals[2] = nDown;
    //Upper triangle
    
    vertices[3] = p3;
    vertices[4] = p4;
    vertices[5] = p5;
    normals[3] = nUp;
    normals[4] = nUp;
    normals[5] = nUp;
      
   
    //First square
    vertices[6] = p1;
    vertices[7] = p4;
    vertices[8] = p0;
    vertices[9] = p3;
    normals[6] = nFirst;
    normals[7] = nFirst;
    normals[8] = nFirst;
    normals[9] = nFirst;
    //second square
    vertices[10] = p0;
    vertices[11] = p3;
    vertices[12] = p2;
    vertices[13] = p5;
    normals[10] = nSecond;
    normals[11] = nSecond;
    normals[12] = nSecond;
    normals[13] = nSecond;
    
    //Third square
    vertices[14] = p2;    
    vertices[15] = p5;
    vertices[16] = p1;
    vertices[17] = p4;
    normals[14] = nThird;
    normals[15] = nThird;
    normals[16] = nThird;
    normals[17] = nThird;
    
    
/*    Vector2f[] texCoord = new Vector2f[6];
    texCoord[0] = new Vector2f(0,0);
    texCoord[1] = new Vector2f(1,0);
    texCoord[2] = new Vector2f(0,1);
    texCoord[3] = new Vector2f(1,1);
    texCoord[4] = new Vector2f(0.5f,0.5f);
    texCoord[5] = new Vector2f(0.7f,0.3f);
  */ 
    int [] indexes = { 2,1,0, 3,4,5, 6,7,8, 8,7,9 ,10,11,12, 12,11,13 ,14,15,16, 16,15,17};
    mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
    mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
    //mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
    mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indexes));
    mesh.updateBound();
    singleBlockMesh = mesh;
  }

  private void setBlueBlockMaterial(AssetManager assetManager){
    blueBlockMaterial = BlockTriangleFieldHelper.getColoredBlockMaterial(assetManager, ColorRGBA.Blue);
  }  
  private void setRedBlockMaterial(AssetManager assetManager){
    redBlockMaterial = BlockTriangleFieldHelper.getColoredBlockMaterial(assetManager, ColorRGBA.Red);
  }  

  
  public void setBlock(TriangleBlock tBlock){
    Vector3 position = tBlock.getPosition();
    if(!position.isPositive())
      throw new Error();
    Geometry block = blocks.get(position);
    if(block != null)
        return;
    boolean even = position.norm1() % 2 != 0;
    block = GetSingleBlock(even,getBlockName(position));
    PutBlockInPlace(position, block);
    field.attachChild(block);
    blocks.put(position, block);
    AddPhysics(block);
  }
  private String getBlockName(Vector3 position){
      return position.toString();
  }
  public void removeBlock(Vector3 position){
    if(!position.isPositive())
      throw new Error();
    Geometry block = blocks.get(position);
    if(block == null)
        return;
    blocks.remove(position);
    field.detachChild(block);
    removePhysics(block);
  }
  
  private void PutBlockInPlace(Vector3 position, Geometry geo){
      int i = position.x();
      int y = position.y();
      int j = position.z();
      float unEvenTranslation = ((float) Math.sqrt(3))*0.5f;
      boolean unEven = ((i + j+y) % 2) != 0;
      geo.setLocalTranslation(i*0.5f, 2+y, unEvenTranslation*j + (unEven? unEvenTranslation : 0));
      if(unEven)
      {
          Quaternion roll180 = new Quaternion();
          roll180.fromAngleAxis( FastMath.PI , new Vector3f(0,1,0) );
          geo.setLocalRotation(roll180);
      }
  }
  private void AddPhysics(Geometry block){
      RigidBodyControl physicsBlock = new RigidBodyControl(0f);
      block.addControl(physicsBlock);
      world.add(physicsBlock);
  }
  private void removePhysics(Geometry block){
    Control physicsBlock = block.getControl(RigidBodyControl.class);
    if(physicsBlock == null)
        return;
    block.removeControl(physicsBlock);
    world.remove(physicsBlock);
  }
  
  public void setActive(boolean active){
  
  }
  private Geometry GetSingleBlock(boolean red,String name){
    Geometry geo = new Geometry(name, singleBlockMesh); 
    geo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
    geo.setMaterial(red? redBlockMaterial:blueBlockMaterial);
    return geo;
  }
}