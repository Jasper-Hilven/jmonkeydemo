package mygame.blocktrianglefield;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.HashMap;
import mygame.math.Vector3;

public class SingleBlockTriangleField implements BlockTriangleField {

    private HashMap<Vector3, TriangleBlock> positionToBlock;
    private Node attachPoint;
    private boolean active;
    private Material mat;
    private PhysicsSpace physics;
    private RigidBodyControl latestPhysicsControl;
    private Geometry latestBlocks;

    public SingleBlockTriangleField(AssetManager assetManager, PhysicsSpace physics) {
        this.mat = BlockTriangleFieldHelper.getVertexColoredBlockMaterial(assetManager, ColorRGBA.Yellow);
        this.physics = physics;
        positionToBlock = new HashMap<Vector3, TriangleBlock>();
        attachPoint = new Node();
    }

    public Mesh getMeshFromPositions() {
        Mesh mesh = new Mesh();
        int nbBlocks = positionToBlock.size();
        Vector3f[] vertices = new Vector3f[nbBlocks * 18];
        Vector3f[] normals = new Vector3f[nbBlocks * 18];
        Vector4f[] colors = new Vector4f[nbBlocks * 18];
        int[] indices = new int[nbBlocks * 24];
        int count = 0;
        for (TriangleBlock triangleBlock : positionToBlock.values()) {

            putSingleBlockMeshInFieldMesh(triangleBlock, count, vertices, normals, colors, indices);
            count++;
        }
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(colors));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices));
        mesh.updateBound();
        return mesh;
    }

    private void replaceLatestMesh() {
        Mesh mesh = getMeshFromPositions();
        Geometry ret = new Geometry("Latest Geom");
        ret.setMesh(mesh);
        ret.setMaterial(mat);
        if (latestBlocks != null) {
            attachPoint.detachChild(latestBlocks);
        }
        attachPoint.attachChild(ret);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(ret);
        RigidBodyControl blocksphysic = new RigidBodyControl(sceneShape, 0f);
        ret.addControl(blocksphysic);

        if (latestPhysicsControl != null) {
            physics.remove(latestPhysicsControl);
            latestBlocks.removeControl(latestPhysicsControl);
        }
        physics.add(blocksphysic);

        latestBlocks = ret;
        latestPhysicsControl = blocksphysic;

    }

    private void putSingleBlockMeshInFieldMesh(TriangleBlock triangleBlock, int nthBlock, Vector3f[] vertices, Vector3f[] normals, Vector4f[] colors, int[] indices) {

        Vector3 position = triangleBlock.getPosition();
        Vector4f color = triangleBlock.getColor();

        boolean unEven = !position.isEven();
        float unEvenTranslation = ((float) Math.sqrt(3)) * 0.5f;
        Vector3f localTranslation = new Vector3f(position.x() * 0.5f, 2 + position.y(), unEvenTranslation * position.z() + (unEven ? unEvenTranslation : 0));
        Quaternion rotateIfUnEven = new Quaternion();
        rotateIfUnEven.fromAngleAxis(unEven ? FastMath.PI : 0, new Vector3f(0, 1, 0));


        final float basicRotation = FastMath.PI / 2f;
        final float resize = (float) Math.sqrt(1d / 3d);
        final float x0 = FastMath.cos(basicRotation + (FastMath.PI * 0f) / 3f);
        final float z0 = FastMath.sin(basicRotation + (FastMath.PI * 0f) / 3f) + 0.5f;
        final float x1 = FastMath.cos(basicRotation + (FastMath.PI * 2f) / 3f);
        final float z1 = FastMath.sin(basicRotation + (FastMath.PI * 2f) / 3f) + 0.5f;
        final float x2 = FastMath.cos(basicRotation + (FastMath.PI * 4f) / 3f);
        final float z2 = FastMath.sin(basicRotation + (FastMath.PI * 4f) / 3f) + 0.5f;
        final float y0 = 0;
        final float y1 = 1f / resize;
        final Vector3f p0l = new Vector3f(x1, y0, z1).mult(resize);
        final Vector3f p1l = new Vector3f(x0, y0, z0).mult(resize);
        final Vector3f p2l = new Vector3f(x2, y0, z2).mult(resize);
        final Vector3f p3l = new Vector3f(x1, y1, z1).mult(resize);
        final Vector3f p4l = new Vector3f(x0, y1, z0).mult(resize);
        final Vector3f p5l = new Vector3f(x2, y1, z2).mult(resize);
        final Vector3f p0 = rotateIfUnEven.mult(p0l).add(localTranslation);
        final Vector3f p1 = rotateIfUnEven.mult(p1l).add(localTranslation);
        final Vector3f p2 = rotateIfUnEven.mult(p2l).add(localTranslation);
        final Vector3f p3 = rotateIfUnEven.mult(p3l).add(localTranslation);
        final Vector3f p4 = rotateIfUnEven.mult(p4l).add(localTranslation);
        final Vector3f p5 = rotateIfUnEven.mult(p5l).add(localTranslation);

        final Vector3f nDown = new Vector3f(0, -1, 0);
        final Vector3f nUp = new Vector3f(0, 1, 0);
        final Vector3f nFirst = p0.add(p1).divide(2f).subtract(p2).normalize();
        final Vector3f nSecond = p2.add(p0).divide(2f).subtract(p1).normalize();
        final Vector3f nThird = p1.add(p2).divide(2f).subtract(p0).normalize();
        int vertexBase = nthBlock * 18;
        int normalBase = nthBlock * 18;
        int indexBase = nthBlock * 24;
        //Lower triangle
        vertices[vertexBase + 0] = p0;
        vertices[vertexBase + 1] = p1;
        vertices[vertexBase + 2] = p2;
        normals[normalBase + 0] = nDown;
        normals[normalBase + 1] = nDown;
        normals[normalBase + 2] = nDown;

        //Upper triangle
        vertices[vertexBase + 3] = p3;
        vertices[vertexBase + 4] = p4;
        vertices[vertexBase + 5] = p5;
        normals[normalBase + 3] = nUp;
        normals[normalBase + 4] = nUp;
        normals[normalBase + 5] = nUp;

        for (int i = 0; i < 18; i++) {
            colors[vertexBase + i] = color;
        }

        //First square
        vertices[vertexBase + 6] = p1;
        vertices[vertexBase + 7] = p4;
        vertices[vertexBase + 8] = p0;
        vertices[vertexBase + 9] = p3;
        normals[normalBase + 6] = nFirst;
        normals[normalBase + 7] = nFirst;
        normals[normalBase + 8] = nFirst;
        normals[normalBase + 9] = nFirst;
        //second square
        vertices[vertexBase + 10] = p0;
        vertices[vertexBase + 11] = p3;
        vertices[vertexBase + 12] = p2;
        vertices[vertexBase + 13] = p5;
        normals[normalBase + 10] = nSecond;
        normals[normalBase + 11] = nSecond;
        normals[normalBase + 12] = nSecond;
        normals[normalBase + 13] = nSecond;

        //Third square
        vertices[vertexBase + 14] = p2;
        vertices[vertexBase + 15] = p5;
        vertices[vertexBase + 16] = p1;
        vertices[vertexBase + 17] = p4;
        normals[normalBase + 14] = nThird;
        normals[normalBase + 15] = nThird;
        normals[normalBase + 16] = nThird;
        normals[normalBase + 17] = nThird;

        indices[indexBase + 0] = 2 + vertexBase;
        indices[indexBase + 1] = 1 + vertexBase;
        indices[indexBase + 2] = 0 + vertexBase;
        indices[indexBase + 3] = 3 + vertexBase;
        indices[indexBase + 4] = 4 + vertexBase;
        indices[indexBase + 5] = 5 + vertexBase;
        indices[indexBase + 6] = 6 + vertexBase;
        indices[indexBase + 7] = 7 + vertexBase;
        indices[indexBase + 8] = 8 + vertexBase;
        indices[indexBase + 9] = 8 + vertexBase;
        indices[indexBase + 10] = 7 + vertexBase;
        indices[indexBase + 11] = 9 + vertexBase;
        indices[indexBase + 12] = 10 + vertexBase;
        indices[indexBase + 13] = 11 + vertexBase;
        indices[indexBase + 14] = 12 + vertexBase;
        indices[indexBase + 15] = 12 + vertexBase;
        indices[indexBase + 16] = 11 + vertexBase;
        indices[indexBase + 17] = 13 + vertexBase;
        indices[indexBase + 18] = 14 + vertexBase;
        indices[indexBase + 19] = 15 + vertexBase;
        indices[indexBase + 20] = 16 + vertexBase;
        indices[indexBase + 21] = 16 + vertexBase;
        indices[indexBase + 22] = 15 + vertexBase;
        indices[indexBase + 23] = 17 + vertexBase;
    }

    public void setActive(boolean active) {
        if (active == this.active) {
            return;
        }
        this.active = active;
        if (active) {
            replaceLatestMesh();
        }
    }

    public void setBlock(TriangleBlock block) {
        if (positionToBlock.containsKey(block.getPosition())) {
            return;
        }
        positionToBlock.put(block.getPosition(), block);
        if (active) {
            replaceLatestMesh();
        }
    }

    public Spatial getField() {
        return attachPoint;
    }

    public void removeBlock(Vector3 position) {
        if (!positionToBlock.containsKey(position)) {
            return;
        }
        positionToBlock.remove(position);
        if (active) {
            replaceLatestMesh();
        }
    }
}
