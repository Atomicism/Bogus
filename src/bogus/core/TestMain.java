package bogus.core;

import java.util.Arrays;

import bogus.graphics.shading.*;
import bogus.math.geom.Vec3;

public class TestMain {
    public static void main(String[] args) {
        ShaderManager shaderManager = ShaderManager.getInstance();
        shaderManager.addActiveShader(new ToonShader());

        shaderManager.applyShaders(
            new Vec3(0, 0, 0),
            new Vec3(0, 0, 0);
        )
}
