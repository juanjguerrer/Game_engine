package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		String fileName = "football";
		ModelData data = OBJFileLoader.loadOBJ(fileName);

		RawModel treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(),
		data.getNormals(),data.getIndices());
		
		TexturedModel staticModel = new TexturedModel(treeModel,
		new ModelTexture(loader.loadTexture(fileName)));
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<15;i++){
			entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*70 - 35,2.5f,random.nextFloat() *-150 - 30),0,0,0,3));
		}
		
		Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass"))));
		terrains.add(new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grass"))));
		terrains.add(new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass"))));
		terrains.add(new Terrain(-1,0,loader,new ModelTexture(loader.loadTexture("grass"))));
		
		Camera camera = new Camera();	
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			camera.move();
			
			for(Terrain terrain:terrains){
				renderer.processTerrain(terrain);
			}
			for(Entity entity:entities){
				renderer.processEntity(entity);
				entity.increaseRotation(0, 1, 0);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}