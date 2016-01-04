package mygame.blocktrianglefield;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.Control;
import com.jme3.util.BufferUtils;
import java.util.HashMap;
import mygame.TurningBlockController;
import mygame.math.Vector3;

public class MultiBlockTriangleField implements BlockTriangleField {

    private HashMap<Vector3, Spatial> blocks;
    private HashMap<Vector4f, Mesh> coloredBlocks;
    private Node field;
    private Material blockMaterial;
    private PhysicsSpace world;

    public Spatial getField() {
        return field;
    }

    public MultiBlockTriangleField(AssetManager assetManager, PhysicsSpace physics) {
        blocks = new HashMap<Vector3, Spatial>();
        field = new Node();
        field.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        blockMaterial = BlockTriangleFieldHelper.getVertexColoredBlockMaterial(assetManager);
        this.world = physics;
        coloredBlocks = new HashMap<Vector4f, Mesh>();
    }

    private Mesh getSingleBlockMesh(Vector4f color) {
        Mesh mesh = new Mesh();
        float basicRotation = FastMath.PI / 2f;
        float resize = (float) Math.sqrt(1d / 3d);
        float x0 = FastMath.cos(basicRotation + (FastMath.PI * 0f) / 3f);
        float z0 = FastMath.sin(basicRotation + (FastMath.PI * 0f) / 3f);
        float x1 = FastMath.cos(basicRotation + (FastMath.PI * 2f) / 3f);
        float z1 = FastMath.sin(basicRotation + (FastMath.PI * 2f) / 3f);
        float x2 = FastMath.cos(basicRotation + (FastMath.PI * 4f) / 3f);
        float z2 = FastMath.sin(basicRotation + (FastMath.PI * 4f) / 3f);
        float y0 = 0;
        float y1 = 1f / resize;
        Vector3f[] vertices = new Vector3f[18];
        Vector3f[] normals = new Vector3f[18];
        Vector4f[] colors = new Vector4f[18];
        for (int i = 0; i < 18; i++) {
            colors[i] = color;
        }
        final Vector3f p0 = new Vector3f(x1, y0, z1).mult(resize);
        final Vector3f p1 = new Vector3f(x0, y0, z0).mult(resize);
        final Vector3f p2 = new Vector3f(x2, y0, z2).mult(resize);
        final Vector3f p3 = new Vector3f(x1, y1, z1).mult(resize);
        final Vector3f p4 = new Vector3f(x0, y1, z0).mult(resize);
        final Vector3f p5 = new Vector3f(x2, y1, z2).mult(resize);
        final Vector3f nDown = new Vector3f(0, -1, 0);
        final Vector3f nUp = new Vector3f(0, 1, 0);
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

        vertices[6] = p1;
        vertices[7] = p4;
        vertices[8] = p0;
        vertices[9] = p3;
        normals[6] = nFirst;
        normals[7] = nFirst;
        normals[8] = nFirst;
        normals[9] = nFirst;

        vertices[10] = p0;
        vertices[11] = p3;
        vertices[12] = p2;
        vertices[13] = p5;
        normals[10] = nSecond;
        normals[11] = nSecond;
        normals[12] = nSecond;
        normals[13] = nSecond;

        vertices[14] = p2;
        vertices[15] = p5;
        vertices[16] = p1;
        vertices[17] = p4;
        normals[14] = nThird;
        normals[15] = nThird;
        normals[16] = nThird;
        normals[17] = nThird;

        int[] indexes = {2, 1, 0, 3, 4, 5, 6, 7, 8, 8, 7, 9, 10, 11, 12, 12, 11, 13, 14, 15, 16, 16, 15, 17};
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(colors));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes));
        mesh.updateBound();
        return mesh;
    }

    private String getBlockName(Vector3 position) {
        return position.toString();
    }

    public void removeBlock(Vector3 position) {
        if (!position.isPositive()) {
            throw new Error();
        }
        Spatial block = blocks.get(position);
        if (block == null) {
            return;
        }
        blocks.remove(position);
        field.detachChild(block);
        removePhysics(block);
    }

    public void setExistingBlock(Vector3 position, Spatial block) {
        if (block.getParent() != null) {
            throw new Error();
        }

        if (blocks.get(position) != null) {
            throw new Error();
        }

        blocks.put(position, block);
        field.attachChild(block);
        RigidBodyControl physicsBlock = block.getControl(RigidBodyControl.class);
        if (physicsBlock.getMass() != 0) {
            physicsBlock.setMass(0f);
        }
    }

    public Spatial takeBlock(Vector3 position) {
        Spatial block = blocks.get(position);
        if (block == null) {
            return null;
        }
        blocks.remove(position);
        field.detachChild(block);
        return block;
    }

    public void setActive(boolean active) {
    }

    public void setBlock(TriangleBlock tBlock) {
        Vector3 position = tBlock.getPosition();
        if (!position.isPositive()) {
            throw new Error();
        }
        Spatial block = blocks.get(position);
        if (block != null) {
            return;
        }
        boolean even = position.isEvenXZ();
        block = GetSingleBlock(even, getBlockName(position), tBlock.getColor());
        PutBlockInPlace(position, block);
        field.attachChild(block);
        blocks.put(position, block);
        AddPhysics(block);
    }

    private void PutBlockInPlace(Vector3 position, Spatial geo) {
        geo.setLocalTranslation(BlockTriangleFieldHelper.getWorldPosition(position));
        geo.setLocalRotation(BlockTriangleFieldHelper.getWorldRotation(position));
    }

    private void AddPhysics(Spatial block) {
        RigidBodyControl physicsBlock = new RigidBodyControl(0f);
        block.addControl(physicsBlock);
        world.add(physicsBlock);
    }

    private void removePhysics(Spatial block) {
        Control physicsBlock = block.getControl(RigidBodyControl.class);
        if (physicsBlock == null) {
            return;
        }
        block.removeControl(physicsBlock);
        world.remove(physicsBlock);
    }

    private Mesh GetColoredBlock(Vector4f color) {
        Mesh block = coloredBlocks.get(color);
        if (block == null) {
            block = getSingleBlockMesh(color);
            coloredBlocks.put(color, block);
        }
        return block;
    }

    private Geometry GetSingleBlock(boolean red, String name, Vector4f color) {
        Geometry geo = new Geometry(name, GetColoredBlock(color));
        geo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        geo.setMaterial(blockMaterial);
        //geo.addControl(new TurningBlockController());
        return geo;
    }
}