package bogus.graphics.shading;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import bogus.math.geom.Vec3;

/**
 * Implements a cel-shading (toon shading) effect
 */
public class ToonShader extends Shader {
    private List<Light> lights = new ArrayList<>();
    private Color ambientColor = new Color(20, 20, 20);
    private int levels = 4; // Number of shading levels (bands)
    private boolean outlines = true; // Whether to draw outlines
    private float outlineThreshold = 0.3f; // Threshold for edge detection
    private Color outlineColor = Color.BLACK;
    
    public ToonShader() {
        super("ToonShader");
    }
    
    /**
     * Add a light to this shader
     * 
     * @param light The light to add
     * @return This shader for chaining
     */
    public ToonShader addLight(Light light) {
        if (!lights.contains(light)) {
            lights.add(light);
        }
        return this;
    }
    
    /**
     * Set the number of shading levels (bands)
     * 
     * @param levels Number of levels (2-10 recommended)
     * @return This shader for chaining
     */
    public ToonShader setLevels(int levels) {
        this.levels = Math.max(2, levels);
        return this;
    }
    
    /**
     * Set whether to draw outlines
     * 
     * @param outlines True to draw outlines
     * @return This shader for chaining
     */
    public ToonShader setOutlines(boolean outlines) {
        this.outlines = outlines;
        return this;
    }
    
    /**
     * Set the outline color
     * 
     * @param outlineColor The outline color
     * @return This shader for chaining
     */
    public ToonShader setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        return this;
    }
    
    /**
     * Set the outline threshold
     * 
     * @param threshold Threshold for edge detection (0.0-1.0)
     * @return This shader for chaining
     */
    public ToonShader setOutlineThreshold(float threshold) {
        this.outlineThreshold = Math.max(0.0f, Math.min(1.0f, threshold));
        return this;
    }
    
    @Override
    public Color shade(Vec3 position, Vec3 normal, Vec3 viewDir, Color baseColor, Material material) {
        // Calculate the maximum diffuse intensity from all lights
        float maxDiffuse = 0.0f;
        
        for (Light light : lights) {
            // For toon shading, we only care about the main directional contribution
            if (light instanceof DirectionalLight) {
                DirectionalLight dirLight = (DirectionalLight) light;
                Vec3 lightDir = dirLight.getDirection().multiply(-1);
                float diffuse = Math.max(0, normal.dot(lightDir));
                maxDiffuse = Math.max(maxDiffuse, diffuse * dirLight.getIntensity());
            }
        }
        
        // Apply cel-shading quantization
        float step = 1.0f / levels;
        float toonDiffuse = Math.round(maxDiffuse / step) * step;
        
        // Check if this is an edge for outline rendering
        boolean isEdge = false;
        if (outlines) {
            float edgeFactor = Math.abs(normal.dot(viewDir));
            isEdge = edgeFactor < outlineThreshold;
        }
        
        // Calculate final color
        Color result;
        if (isEdge) {
            result = outlineColor;
        } else {
            // Mix ambient and diffuse for the color
            float ambient = 0.2f; // Minimum light level
            float intensity = ambient + (1.0f - ambient) * toonDiffuse;
            
            int r = (int)(baseColor.getRed() * intensity);
            int g = (int)(baseColor.getGreen() * intensity);
            int b = (int)(baseColor.getBlue() * intensity);
            
            // Clamp values
            r = Math.min(255, Math.max(0, r));
            g = Math.min(255, Math.max(0, g));
            b = Math.min(255, Math.max(0, b));
            
            result = new Color(r, g, b, baseColor.getAlpha());
        }
        
        return result;
    }
}