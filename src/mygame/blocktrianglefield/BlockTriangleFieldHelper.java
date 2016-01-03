package mygame.blocktrianglefield;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import mygame.math.Vector3;

public class BlockTriangleFieldHelper {

    public static Material getColoredBlockMaterial(AssetManager assetManager, ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //mat.setBoolean("UseMaterialColors",true);  // Set some parameters, e.g. blue.
        mat.setColor("Ambient", color);   // ... color of this object
        mat.setColor("Diffuse", color);   // ... color of light being reflected
        return mat;
    }

    public static Material getVertexColoredBlockMaterial(AssetManager assetManager) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 15f);
        mat.setBoolean("UseVertexColor", true);
        mat.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        mat.setColor("Ambient", ColorRGBA.White);   // ... color of this object
        mat.setColor("Diffuse", ColorRGBA.White);   // ... color of light being reflected
        mat.setColor("Specular", ColorRGBA.White);
        mat.setBoolean("VertexLighting", true);
        return mat;
    }
    
    public static Vector3 getNearestBlockPosition(Vector3f worldPosition){
        int y = Math.round(worldPosition.y);
        int x = Math.round(worldPosition.x*2f);
        throw new Error();
    }
    
    
    public static Vector3f getWorldPosition(Vector3 position) {
        int x = position.x();
        int y = position.y();
        int z = position.z();
        float unEvenTranslation = - 0.5f/FastMath.sqrt(3);
        float zTranslation = 1.5f/FastMath.sqrt(3);
        float xTranslation = 0.5f;
        boolean unEven = !position.isEvenXZ();
        Vector3f localTranslation = new Vector3f(x * xTranslation, y, zTranslation * z + (unEven ? unEvenTranslation : 0));
        return localTranslation;
    }
    private static Quaternion unit = Quaternion.IDENTITY;
    private static Quaternion rotateY;
    static{
      rotateY = new Quaternion();
      rotateY.fromAngleAxis(FastMath.PI, new Vector3f(0, 1, 0));
    }
    public static Quaternion getWorldRotation(Vector3 position){
        return position.isEvenXZ() ? rotateY : unit;
    }
}