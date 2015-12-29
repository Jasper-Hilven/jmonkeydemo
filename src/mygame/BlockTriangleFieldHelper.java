package mygame;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
public class BlockTriangleFieldHelper{

    
    public static Material getColoredBlockMaterial(AssetManager assetManager,ColorRGBA color){
    Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    //mat.setBoolean("UseMaterialColors",true);  // Set some parameters, e.g. blue.
    mat.setColor("Ambient", color);   // ... color of this object
    mat.setColor("Diffuse", color);   // ... color of light being reflected
    return mat;
  } 
  public static Material getVertexColoredBlockMaterial(AssetManager assetManager,ColorRGBA color){
    Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    mat.setFloat("Shininess", 15f);
    mat.setBoolean("UseVertexColor", true);
    mat.setBoolean("UseMaterialColors",true);  // Set some parameters, e.g. blue.
    mat.setColor("Ambient", color);   // ... color of this object
    mat.setColor("Diffuse", ColorRGBA.White);   // ... color of light being reflected
    mat.setColor("Specular", ColorRGBA.White);
    mat.setBoolean("VertexLighting", true);
    return mat;
  } 
}