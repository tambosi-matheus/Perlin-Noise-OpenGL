package br.pucpr.cg;

import br.pucpr.mage.*;
import br.pucpr.mage.camera.Camera;
import br.pucpr.mage.camera.CameraFPS;
import br.pucpr.mage.phong.DirectionalLight;
import br.pucpr.mage.phong.Material;
import org.joml.Matrix4f;

import static br.pucpr.mage.MathUtil.asString;
import static br.pucpr.mage.MathUtil.mul;
import static org.joml.Math.toDegrees;
import static org.joml.Math.toRadians;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class TerrainScene implements Scene
{
    private Keyboard keys = Keyboard.getInstance();

    private final float WALK_SPEED = 20f;
    private final float TURN_SPEED = toRadians(120f);

    private Shader shader;
    private Mesh mesh;
    private CameraFPS camera = new CameraFPS();
    private DirectionalLight light;
    private Material material;

    @Override
    public void init()
    {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        shader = Shader.loadProgram("phong");
        mesh = MeshFactory.createTerrain(shader, 1000, 1000, 1, 8f).setWireframe(true);
        //mesh = MeshFactory.createTerrain(true, shader, 1000, 1000, 0.1f, 10f).setWireframe(false);

        camera.getPosition().set(0f, 50f, 0f);
        camera.setAngleY(toRadians(0));

        light = new DirectionalLight()
                .setDirection(0.8f, -1.0f, 1.0f)
                .setAmbient(0.1f)
                .setColor(1.0f, 1.0f, 0.98f);

        material = new Material()
                .setColor(0.9f)
                .setSpecular(0.3f).setPower(64);
    }

    @Override
    public void update(float secs)
    {
        if (keys.isPressed(GLFW_KEY_ESCAPE))
        {
            glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
            return;
        }

        var strafe = keys.isDown(GLFW_KEY_LEFT_SHIFT);

        if (keys.isDown(GLFW_KEY_UP))
            camera.move(WALK_SPEED, secs);
        else if (keys.isDown(GLFW_KEY_DOWN))
            camera.move(-WALK_SPEED, secs);

        if (keys.isDown(GLFW_KEY_Z))
            camera.getPosition().add(mul(camera.getUp(), -1.0f));
        else if (keys.isDown(GLFW_KEY_C))
            camera.getPosition().add(mul(camera.getUp(), 1.0f));


        if (strafe)
        {
            if (keys.isDown(GLFW_KEY_LEFT))
                camera.strafe(WALK_SPEED, secs);
            else if (keys.isDown(GLFW_KEY_RIGHT))
                camera.strafe(-WALK_SPEED, secs);

        } else
        {
            if (keys.isDown(GLFW_KEY_LEFT))
                camera.turn(TURN_SPEED, secs);
            else if (keys.isDown(GLFW_KEY_RIGHT))
                camera.turn(-TURN_SPEED, secs);
        }
    }

    @Override
    public void draw()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.bind()
            .set(camera)
            .set(light)
        .unbind();

        mesh
            .setUniform("uWorld", new Matrix4f())
            .setItem("material", material)
        .draw(shader);
    }

    @Override
    public void deinit() {
    }

    public static void main(String[] args) {
        new Window(new TerrainScene(), "Terrain sample", 1024, 768).show();
    }
}