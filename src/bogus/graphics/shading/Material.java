package bogus.graphics.shading;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

import bogus.math.geom.Vec3;

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

    public static Color processPixelWithShaders(int x, int y, int baseRGB, Vec3 normal, Material material, List<Light> lights) {
        int alpha = (baseRGB >> 24) & 0xFF;
        
        // Skip fully transparent pixels
        if (alpha == 0) {
            return new Color(0, 0, 0, 0);
        }
        
        // Extract color components
        int baseR = (baseRGB >> 16) & 0xFF;
        int baseG = (baseRGB >> 8) & 0xFF;
        int baseB = baseRGB & 0xFF;
        Color baseColor = new Color(baseR, baseG, baseB, alpha);
        
        // Position of this pixel in 3D space (assuming Z=0 plane)
        Vec3 position = new Vec3(x, y, 0);
        
        // Vector from pixel to camera (assuming orthographic view pointing in -Z)
        Vec3 viewDir = new Vec3(0, 0, 1);
        
        // Get the shader manager
        ShaderManager shaderManager = ShaderManager.getInstance();
        
        // If no active shaders, use a basic lighting calculation
        if (shaderManager.getActiveShaderCount() == 0) {
            // Add emissive contribution
            float r = material.getEmissiveColor().getRed() * material.getEmissiveStrength() / 255f;
            float g = material.getEmissiveColor().getGreen() * material.getEmissiveStrength() / 255f;
            float b = material.getEmissiveColor().getBlue() * material.getEmissiveStrength() / 255f;
            
            // Process each light
            for (Light light : lights) {
                Color lightColor = light.calculateLightingAt(position, normal, viewDir, material);
                r += lightColor.getRed() / 255f;
                g += lightColor.getGreen() / 255f;
                b += lightColor.getBlue() / 255f;
            }
            
            // Apply lighting to base color
            int litR = Math.min(255, (int)(baseR * r));
            int litG = Math.min(255, (int)(baseG * g));
            int litB = Math.min(255, (int)(baseB * b));
            
            return new Color(litR, litG, litB, alpha);
        } else {
            // Use the shader manager to process this pixel
            return shaderManager.applyShaders(position, normal, viewDir, baseColor, material);
        }
    }

    public static BufferedImage processLightingWithShaders(Material material, Material.RenderContext context) {
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
            g.setColor(material.getDiffuseColor());
            g.fillRect(0, 0, result.getWidth(), result.getHeight());
        }
        
        // Get the base image data
        int width = result.getWidth();
        int height = result.getHeight();
        
        // Create lighting computation buffer
        BufferedImage lightBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        // Pre-compute surface normals (either from normal map or default)
        Vec3[][] normals = material.computeSurfaceNormals(width, height);
        
        // For each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the base color
                int baseRGB = result.getRGB(x, y);
                
                // Get the normal at this pixel
                Vec3 normal = normals[x][y];
                
                // Process this pixel with shaders
                Color shadedColor = processPixelWithShaders(x, y, baseRGB, normal, material, context.lights);
                
                // Set the result pixel
                lightBuffer.setRGB(x, y, shadedColor.getRGB());
            }
        }
        
        g.dispose();
        return lightBuffer;
    }

    public Vec3[][] computeSurfaceNormals(int width, int height) {
        Vec3[][] normals = new Vec3[width][height];
        
        if (normalMap != null) {
            // Extract normals from normal map
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Map coordinates to normal map coordinates
                    int nx = (int) ((float) x / width * normalMap.getWidth());
                    int ny = (int) ((float) y / height * normalMap.getHeight());
                    
                    // Get the normal map color
                    int rgb = normalMap.getRGB(nx, ny);
                    
                    // Extract RGB components (normal map channels)
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    
                    // Convert from [0,255] to [-1,1] range
                    float nx_val = (r / 255.0f) * 2.0f - 1.0f;
                    float ny_val = (g / 255.0f) * 2.0f - 1.0f;
                    float nz_val = (b / 255.0f) * 2.0f - 1.0f;
                    
                    // Create and normalize the normal vector
                    Vec3 normal = new Vec3(nx_val, ny_val, nz_val);
                    normal.normalize();
                    
                    normals[x][y] = normal;
                }
            }
        } else {
            // Use default normals (pointing upward)
            Vec3 defaultNormal = new Vec3(0.0f, 0.0f, 1.0f);
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    normals[x][y] = defaultNormal;
                }
            }
        }
        
        return normals;
    }

        /**
     * Render context containing all information needed for rendering.
     */
    public static class RenderContext {
        public BufferedImage sprite;
        public int width;
        public int height;
        public List<Light> lights;
        //public Camera camera;
        
        /**
         * Creates a render context with a sprite.
         * 
         * @param sprite The sprite to render
         * @param lights The light sources
         * @param camera The camera position
         */
        public RenderContext(BufferedImage sprite, List<Light> lights/*, Camera camera*/) {
            this.sprite = sprite;
            this.width = sprite != null ? sprite.getWidth() : 0;
            this.height = sprite != null ? sprite.getHeight() : 0;
            this.lights = lights;
            //this.camera = camera;
        }
        
        /**
         * Creates a render context with explicit dimensions.
         * 
         * @param width The width of the render area
         * @param height The height of the render area
         * @param lights The light sources
         * @param camera The camera position
         */
        public RenderContext(int width, int height, List<Light> lights/*, Camera camera*/) {
            this.sprite = null;
            this.width = width;
            this.height = height;
            this.lights = lights;
            //this.camera = camera;
        }
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
        
        public static Material marble(Color baseColor) {
            Material m = new Material(baseColor);
            m.setRoughness(0.2f);
            m.setReflectivity(0.3f);
            return m;
        }
        
        public static Material wood(Color woodColor) {
            Material m = new Material(woodColor);
            m.setRoughness(0.7f);
            m.setSpecularColor(new Color(80, 60, 30));
            return m;
        }
        
        public static Material skin(Color skinTone) {
            Material m = new Material(skinTone);
            m.setRoughness(0.3f);
            m.setSubsurfaceScattering(0.5f);
            return m;
        }
    }
}