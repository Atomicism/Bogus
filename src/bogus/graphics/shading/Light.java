package bogus.graphics.shading;

import java.awt.*;

import bogus.math.geom.Vec3;

/**
 * Abstract base class for all types of lights
 */
public abstract class Light {
    protected Color color = Color.WHITE;
    protected float intensity = 1.0f;
    protected boolean castsShadows = false;
    
    public Light() {
    }
    
    public Light(Color color, float intensity) {
        this.color = color;
        this.intensity = intensity;
    }
    
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    
    public float getIntensity() { return intensity; }
    public void setIntensity(float intensity) { this.intensity = Math.max(0, intensity); }
    
    public boolean getCastsShadows() { return castsShadows; }
    public void setCastsShadows(boolean castsShadows) { this.castsShadows = castsShadows; }
    
    /**
     * Calculate lighting at a specific point
     * 
     * @param position The world-space position being lit
     * @param normal The surface normal at the position
     * @param viewDir The direction from the position to the camera
     * @param material The material at the position
     * @return The calculated light color
     */
    public abstract Color calculateLightingAt(Vec3 position, Vec3 normal, Vec3 viewDir, Material material);
    
    /**
     * Implements the Phong lighting model
     */
    protected Color calculatePhongLighting(Vec3 position, Vec3 normal, Vec3 viewDir, 
                                          Vec3 lightDir, Material material, float attenuation) {
        // Diffuse reflection
        float diffuseFactor = Math.max(0, normal.dot(lightDir));
        
        // Specular reflection (Phong model)
        Vec3 reflectDir = lightDir.multiply(-1).reflect(normal);
        float specularFactor = (float)Math.pow(Math.max(0, viewDir.dot(reflectDir)), 
                                              (1.0f - material.getRoughness()) * 100);
        
        // Use material properties to adjust light response
        Color specularColor = material.getSpecularColor();
        float metalness = material.getMetalness();
        
        // Metals have colored specular, while non-metals have white specular
        int specR = specularColor.getRed();
        int specG = specularColor.getGreen();
        int specB = specularColor.getBlue();
        
        // Calculate final color with attenuation
        int r = (int)(color.getRed() * (diffuseFactor + specularFactor * specR/255f * metalness) * intensity * attenuation);
        int g = (int)(color.getGreen() * (diffuseFactor + specularFactor * specG/255f * metalness) * intensity * attenuation);
        int b = (int)(color.getBlue() * (diffuseFactor + specularFactor * specB/255f * metalness) * intensity * attenuation);
        
        // Clamp values to valid range
        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));
        
        return new Color(r, g, b);
    }
}

