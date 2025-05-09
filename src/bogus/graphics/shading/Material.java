package bogus.graphics.shading;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

import bogus.math.geom.Vec3;

/**
 * A comprehensive material system with physical properties and lighting support
 */
public class Material {
    // Base visual properties
    private Color diffuseColor = Color.WHITE;
    private Color specularColor = new Color(255, 255, 255);
    private Color emissiveColor = new Color(0, 0, 0);
    private float opacity = 1.0f;
    private BufferedImage diffuseMap = null;
    private BufferedImage normalMap = null;
    private BufferedImage specularMap = null;
    
    // Physical material properties
    private float roughness = 0.5f;      // 0.0 = smooth, 1.0 = rough
    private float metalness = 0.0f;      // 0.0 = dielectric, 1.0 = metallic
    private float reflectivity = 0.0f;   // 0.0 = no reflection, 1.0 = mirror
    private float emissiveStrength = 0.0f; // How much light the material emits
    
    // Advanced material properties
    private float refractionIndex = 1.0f; // For glass, water, etc.
    private float subsurfaceScattering = 0.0f; // For translucent materials like skin, wax, etc.
    
    // Custom properties for advanced effects
    private Map<String, Object> properties = new HashMap<>();
    
    /**
     * Creates a default material
     */
    public Material() {
    }
    
    /**
     * Creates a material with specific diffuse color
     */
    public Material(Color diffuseColor) {
        this.diffuseColor = diffuseColor;
    }
    
    /**
     * Creates a comprehensive material with common properties
     */
    public Material(Color diffuseColor, float roughness, float metalness, float reflectivity) {
        this.diffuseColor = diffuseColor;
        this.roughness = Math.max(0, Math.min(1, roughness));
        this.metalness = Math.max(0, Math.min(1, metalness));
        this.reflectivity = Math.max(0, Math.min(1, reflectivity));
    }
    
    // Getters and setters for base properties
    public Color getDiffuseColor() { return diffuseColor; }
    public void setDiffuseColor(Color color) { this.diffuseColor = color; }
    
    public Color getSpecularColor() { return specularColor; }
    public void setSpecularColor(Color color) { this.specularColor = color; }
    
    public Color getEmissiveColor() { return emissiveColor; }
    public void setEmissiveColor(Color color) { this.emissiveColor = color; }
    
    public float getOpacity() { return opacity; }
    public void setOpacity(float opacity) { this.opacity = Math.max(0, Math.min(1, opacity)); }
    
    public BufferedImage getDiffuseMap() { return diffuseMap; }
    public void setDiffuseMap(BufferedImage texture) { this.diffuseMap = texture; }
    
    public BufferedImage getNormalMap() { return normalMap; }
    public void setNormalMap(BufferedImage normalMap) { this.normalMap = normalMap; }
    
    public BufferedImage getSpecularMap() { return specularMap; }
    public void setSpecularMap(BufferedImage specularMap) { this.specularMap = specularMap; }
    
    // Getters and setters for physical properties
    public float getRoughness() { return roughness; }
    public void setRoughness(float roughness) { this.roughness = Math.max(0, Math.min(1, roughness)); }
    
    public float getMetalness() { return metalness; }
    public void setMetalness(float metalness) { this.metalness = Math.max(0, Math.min(1, metalness)); }
    
    public float getReflectivity() { return reflectivity; }
    public void setReflectivity(float reflectivity) { this.reflectivity = Math.max(0, Math.min(1, reflectivity)); }
    
    public float getEmissiveStrength() { return emissiveStrength; }
    public void setEmissiveStrength(float strength) { this.emissiveStrength = Math.max(0, strength); }
    
    public float getRefractionIndex() { return refractionIndex; }
    public void setRefractionIndex(float index) { this.refractionIndex = Math.max(1.0f, index); }
    
    public float getSubsurfaceScattering() { return subsurfaceScattering; }
    public void setSubsurfaceScattering(float amount) { this.subsurfaceScattering = Math.max(0, Math.min(1, amount)); }
    
    /**
     * Set a custom property
     */
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }
    
    /**
     * Get a custom property
     */
    @SuppressWarnings("unchecked")
    public <T> T getProperty(String name, T defaultValue) {
        Object value = properties.get(name);
        if (value == null) return defaultValue;
        return (T) value;
    }
    
    /**
     * Apply this material to render an object with lighting
     * 
     * @param g2d Graphics context
     * @param sprite The sprite/image to render
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     * @param lights List of light sources to apply
     */
    public void apply(Graphics2D g2d, BufferedImage sprite, int x, int y, int width, int height, List<Light> lights) {
        // Create a rendering context
        RenderContext context = new RenderContext();
        context.sprite = sprite;
        context.x = x;
        context.y = y;
        context.width = width;
        context.height = height;
        context.lights = lights;
        
        // Store the original transform for later restoration
        AffineTransform originalTransform = g2d.getTransform();
        Composite originalComposite = g2d.getComposite();
        
        // Process the lighting for the sprite
        BufferedImage renderedImage = processLighting(context);
        
        // Set opacity if needed
        if (opacity < 1.0f) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        }
        
        // Draw the final image
        g2d.drawImage(renderedImage, x, y, width, height, null);
        
        // Restore original state
        g2d.setTransform(originalTransform);
        g2d.setComposite(originalComposite);
    }
    
    /**
     * Process lighting for the material and sprite
     */
    private BufferedImage processLighting(RenderContext context) {
        // Create a new image for the result
        BufferedImage result = new BufferedImage(
            context.sprite != null ? context.sprite.getWidth() : context.width,
            context.sprite != null ? context.sprite.getHeight() : context.height,
            BufferedImage.TYPE_INT_ARGB
        );
        
        // Get the graphics for the result
        Graphics2D g = result.createGraphics();
        
        // If we have a sprite, start with that
        if (context.sprite != null) {
            g.drawImage(context.sprite, 0, 0, null);
        } else {
            // Otherwise fill with the diffuse color
            g.setColor(diffuseColor);
            g.fillRect(0, 0, result.getWidth(), result.getHeight());
        }
        
        // Get the base image data
        int width = result.getWidth();
        int height = result.getHeight();
        
        // Create lighting computation buffer
        BufferedImage lightBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        // Pre-compute surface normals (either from normal map or default)
        Vec3[][] normals = computeSurfaceNormals(width, height);
        
        // For each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the base color
                int baseRGB = result.getRGB(x, y);
                int alpha = (baseRGB >> 24) & 0xFF;
                
                // Skip fully transparent pixels
                if (alpha == 0) {
                    lightBuffer.setRGB(x, y, 0);
                    continue;
                }
                
                // Extract color components
                int baseR = (baseRGB >> 16) & 0xFF;
                int baseG = (baseRGB >> 8) & 0xFF;
                int baseB = baseRGB & 0xFF;
                
                // Add emissive light contribution
                float r = emissiveColor.getRed() * emissiveStrength / 255f;
                float gr = emissiveColor.getGreen() * emissiveStrength / 255f;
                float b = emissiveColor.getBlue() * emissiveStrength / 255f;
                
                // Get normal at this point
                Vec3 normal = normals[x][y];
                
                // Vector from pixel to camera (assuming orthographic view pointing in -Z)
                Vec3 viewDir = new Vec3(0, 0, 1);
                
                // Position of this pixel in 3D space (assuming Z=0 plane)
                Vec3 position = new Vec3(x, y, 0);
                
                // For each light source
                for (Light light : context.lights) {
                    // Calculate light contribution for this pixel
                    Color lightColor = light.calculateLightingAt(position, normal, viewDir, this);
                    
                    // Add light contribution
                    r += lightColor.getRed() / 255f;
                    gr += lightColor.getGreen() / 255f;
                    b += lightColor.getBlue() / 255f;
                }
                
                // Multiply the base color by the lighting
                int litR = Math.min(255, (int)(baseR * r));
                int litG = Math.min(255, (int)(baseG * gr));
                int litB = Math.min(255, (int)(baseB * b));
                
                // Combine the lit color with the original alpha
                int litRGB = (alpha << 24) | (litR << 16) | (litG << 8) | litB;
                lightBuffer.setRGB(x, y, litRGB);
            }
        }
        
        g.dispose();
        return lightBuffer;
    }
    
    /**
     * Compute surface normals for the material
     */
    private Vec3[][] computeSurfaceNormals(int width, int height) {
        Vec3[][] normals = new Vec3[width][height];
        
        if (normalMap != null) {
            // Use normal map if available
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Normal maps store XYZ as RGB
                    if (x < normalMap.getWidth() && y < normalMap.getHeight()) {
                        int rgb = normalMap.getRGB(x, y);
                        float nx = ((rgb >> 16) & 0xFF) / 127.5f - 1.0f;
                        float ny = ((rgb >> 8) & 0xFF) / 127.5f - 1.0f;
                        float nz = (rgb & 0xFF) / 127.5f - 1.0f;
                        normals[x][y] = new Vec3(nx, ny, nz).normalize();
                    } else {
                        // Default normal pointing up (Z-axis in screen space)
                        normals[x][y] = new Vec3(0, 0, 1);
                    }
                }
            }
        } else {
            // Default normals pointing toward the camera
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    normals[x][y] = new Vec3(0, 0, 1);
                }
            }
        }
        
        return normals;
    }
    
    /**
     * Rendering context for the material
     */
    public static class RenderContext {
        public BufferedImage sprite;
        public int x, y, width, height;
        public List<Light> lights;
    }
    
    /**
     * Create material presets for common materials
     */
    public static class Presets {
        public static Material plastic(Color color) {
            Material m = new Material(color);
            m.setRoughness(0.4f);
            m.setSpecularColor(new Color(220, 220, 220));
            return m;
        }
        
        public static Material metal(Color color) {
            Material m = new Material(color);
            m.setMetalness(1.0f);
            m.setRoughness(0.1f);
            m.setReflectivity(0.8f);
            return m;
        }
        
        public static Material glass(Color tint) {
            Material m = new Material(tint);
            m.setOpacity(0.5f);
            m.setRoughness(0.05f);
            m.setReflectivity(0.5f);
            m.setRefractionIndex(1.5f);
            return m;
        }
        
        public static Material rubber(Color color) {
            Material m = new Material(color);
            m.setRoughness(0.9f);
            m.setSpecularColor(new Color(40, 40, 40));
            return m;
        }
        
        public static Material emissive(Color color, float strength) {
            Material m = new Material(color);
            m.setEmissiveColor(color);
            m.setEmissiveStrength(strength);
            return m;
        }
    }
}