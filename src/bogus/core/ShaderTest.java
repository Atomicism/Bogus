package bogus.core;

import java.util.*;
import java.util.List;

import javax.swing.*;

import java.awt.*;
import java.awt.font.ShapeGraphicAttribute;
import java.awt.image.*;

import bogus.graphics.shading.*;
import bogus.math.geom.*;

public class ShaderTest {
    private List<LightedObject> objects = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();
    private Color ambientColor = new Color(25, 25, 25); // Low ambient light by default
    
    public ShaderTest() {
        // Add default ambient light
        // lights.add(new AmbientLight(new Color(50, 50, 50), 0.2f));
    }
    
    public void addObject(BufferedImage sprite, Material material, int x, int y, int width, int height) {
        objects.add(new LightedObject(sprite, material, x, y, width, height));
    }
    
    public void addLight(Light light) {
        lights.add(light);
    }
    
    public void render(Graphics2D g2d) {
        // Render all objects with lighting
        for (LightedObject object : objects) {
            object.material.apply(g2d, object.sprite, object.x, object.y, object.width, object.height, lights);
        }
    }
    
    private static class LightedObject {
        BufferedImage sprite;
        Material material;
        int x, y, width, height;
        
        public LightedObject(BufferedImage sprite, Material material, int x, int y, int width, int height) {
            this.sprite = sprite;
            this.material = material;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
    
    /**
     * Example of scene usage
     */
    public static void main(String[] args) {
        // Create a frame and panel for rendering
        JFrame frame = new JFrame("Material and Lighting Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setBackground(Color.BLACK);
                // Create a scene
                ShaderTest scene = new ShaderTest();
                
                // Create some materials
                Material metalMaterial = Material.Presets.metal(new Color(220, 220, 255));
                Material plasticMaterial = Material.Presets.plastic(Color.RED);
                Material glassMaterial = Material.Presets.glass(new Color(200, 255, 255));
                Material glowMaterial = Material.Presets.emissive(Color.ORANGE, 1.5f);
                
                // Create some sprites (or use null for simple rectangles)
                BufferedImage sprite = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D sg = sprite.createGraphics();
                sg.setColor(Color.WHITE);
                sg.fillOval(0, 0, 100, 100);
                sg.dispose();
                
                // Add objects
                scene.addObject(sprite, metalMaterial, 100, 100, 100, 100);
                scene.addObject(null, plasticMaterial, 250, 150, 120, 80);
                scene.addObject(null, glassMaterial, 400, 200, 150, 150);
                scene.addObject(null, glowMaterial, 300, 300, 50, 50);
                
                scene.addLight(new DirectionalLight(new Vec3(-1, -1, -1), new Color(200, 200, 255), 0.7f));
                
                // Render the scene
                scene.render(g2d);
            }
        };
        
        frame.add(panel);
        frame.setVisible(true);
    }
}
