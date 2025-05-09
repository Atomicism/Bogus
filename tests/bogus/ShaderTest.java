package tests.bogus;

import bogus.graphics.shading.*;
import bogus.math.geom.Vec3;

import java.awt.Color;

public class ShaderTest {

    // Minimal stub for Material
    static class DummyMaterial extends Material {}

    // Simple test shader that adjusts brightness
    static class BrightnessShader extends Shader {
        private String name;
        private final float factor;
        private boolean enabled = true;

        public BrightnessShader(String name, float factor) {
            super(name);
            this.factor = factor;
        }

        public String getName() {
            return name;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public Shader setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Color shade(Vec3 position, Vec3 normal, Vec3 viewDir, Color color, Material material) {
            int r = (int)(color.getRed() * factor);
            int g = (int)(color.getGreen() * factor);
            int b = (int)(color.getBlue() * factor);
            return new Color(
                Math.min(255, r),
                Math.min(255, g),
                Math.min(255, b),
                color.getAlpha()
            );
        }
    }

    public static void main(String[] args) {
        ShaderManager manager = ShaderManager.getInstance();
        manager.clearActiveShaders();

        // Create shaders
        Shader bright = new PhongShader();
        Shader dim = new ToonShader();

        // Register and activate shaders
        manager.registerShader(bright);
        manager.registerShader(dim);
        manager.addActiveShader(bright);
        manager.addActiveShader(dim);

        // Setup inputs
        Vec3 pos = new Vec3(0, 0, 0);
        Vec3 norm = new Vec3(0, 1, 0);
        Vec3 view = new Vec3(0, 0, -1);
        Color base = new Color(100, 100, 100);
        DummyMaterial mat = new DummyMaterial();

        // Apply shaders
        Color result = manager.applyShaders(pos, norm, view, base, mat);

        System.out.println("Base color:   " + base);
        System.out.println("Final color:  " + result); // should be around (75, 75, 75)

        // Disable one shader and test again
        dim.setEnabled(false);
        Color result2 = manager.applyShaders(pos, norm, view, base, mat);
        System.out.println("Final color with 'dim' disabled: " + result2); // should be around (150, 150, 150)

        // Clear shaders
        manager.clearActiveShaders();
        Color result3 = manager.applyShaders(pos, norm, view, base, mat);
        System.out.println("Final color with no shaders:     " + result3); // should be the same as base
    }
}
