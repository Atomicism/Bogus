package bogus.graphics.shading;

import java.awt.Color;

import bogus.math.geom.Vec3;

/**
 * An ambient light that provides a constant base illumination to all surfaces
 */
public class AmbientLight extends Light {
    public AmbientLight() {
    }
    
    public AmbientLight(Color color, float intensity) {
        super(color, intensity);
    }
    
    @Override
    public Color calculateLightingAt(Vec3 position, Vec3 normal, Vec3 viewDir, Material material) {
        // Ambient light provides uniform illumination regardless of position or orientation
        int r = (int)(color.getRed() * intensity);
        int g = (int)(color.getGreen() * intensity);
        int b = (int)(color.getBlue() * intensity);
        
        // Clamp values to valid range
        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));
        
        return new Color(r, g, b);
    }
}