package bogus.graphics.shading;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import bogus.math.geom.Vec3;

/**
 * Implements the Phong lighting model as a shader
 */
public class PhongShader extends Shader {
    private List<Light> lights = new ArrayList<>();
    private Color ambientColor = new Color(20, 20, 20); // Default ambient light
    
    public PhongShader() {
        super("PhongShader");
    }
    
    /**
     * Add a light to this shader
     * 
     * @param light The light to add
     * @return This shader for chaining
     */
    public PhongShader addLight(Light light) {
        if (!lights.contains(light)) {
            lights.add(light);
        }
        return this;
    }
    
    /**
     * Remove a light from this shader
     * 
     * @param light The light to remove
     * @return This shader for chaining
     */
    public PhongShader removeLight(Light light) {
        lights.remove(light);
        return this;
    }
    
    /**
     * Set the ambient light color
     * 
     * @param ambientColor The ambient color
     * @return This shader for chaining
     */
    public PhongShader setAmbientColor(Color ambientColor) {
        this.ambientColor = ambientColor;
        return this;
    }
    
    @Override
    public Color shade(Vec3 position, Vec3 normal, Vec3 viewDir, Color baseColor, Material material) {
        // Start with ambient light
        int r = (ambientColor.getRed() * baseColor.getRed()) / 255;
        int g = (ambientColor.getGreen() * baseColor.getGreen()) / 255;
        int b = (ambientColor.getBlue() * baseColor.getBlue()) / 255;
        
        // Add contribution from each light
        for (Light light : lights) {
            Color lightColor = light.calculateLightingAt(position, normal, viewDir, material);
            r += lightColor.getRed();
            g += lightColor.getGreen();
            b += lightColor.getBlue();
        }
        
        // Clamp values
        r = Math.min(255, r);
        g = Math.min(255, g);
        b = Math.min(255, b);
        
        return new Color(r, g, b, baseColor.getAlpha());
    }
}