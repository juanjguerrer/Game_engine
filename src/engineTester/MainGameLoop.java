package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import objConverter.OBJLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		RawModel treeModel = OBJLoader.loadObjModel("tree",loader);
		TexturedModel tree = new TexturedModel(treeModel,new ModelTexture(loader.loadTexture("tree")));

		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
		new ModelTexture(loader.loadTexture("flower")));
		flower.getTexture().setHasTransparency(true);

		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
		new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
		new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTransparency(true);

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<500;i++){
			entities.add(new Entity(tree, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),
			0,0,0,3));
			entities.add(new Entity(grass, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),
            0,0,0,1));
			entities.add(new Entity(fern, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),
            0,0,0,0.6f));
			if(i%3==0)
				entities.add(new Entity(flower, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),
            	0,0,0,1.5f));
		}
		
		Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain3 = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain4 = new Terrain(-1,0,loader,new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();	
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			camera.move();
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processTerrain(terrain3);
			renderer.processTerrain(terrain4);
			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}