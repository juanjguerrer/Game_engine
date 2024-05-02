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
		
		String fileName = "planet";
		ModelData data = OBJFileLoader.loadOBJ(fileName);

		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(),
		data.getNormals(),data.getIndices());
		
		TexturedModel sunModel = new TexturedModel(model,
		new ModelTexture(loader.loadTexture("sun2")));
		Entity solecito = new Entity(sunModel, new Vector3f(0,0,0), 0, 0, 0, 1);  
		
		TexturedModel mercuryModel = new TexturedModel(model,
		new ModelTexture(loader.loadTexture("mercury")));
		Entity mercury = new Entity(mercuryModel, new Vector3f(12,0,0), 0, 0, 0, 0.1f);

		TexturedModel venusModel = new TexturedModel(model,
		new ModelTexture(loader.loadTexture("venus")));
		Entity venus = new Entity(venusModel, new Vector3f(15,0,0), 0, 0, 0, 0.1f);

		TexturedModel earthModel = new TexturedModel(model,
		new ModelTexture(loader.loadTexture("earth")));
		Entity earth = new Entity(earthModel, new Vector3f(20,0,0), 0, 0, 0, 0.3f);

		TexturedModel marsModel = new TexturedModel(model,
		new ModelTexture(loader.loadTexture("mars")));
		Entity mars = new Entity(marsModel, new Vector3f(28,0,0), 0, 0, 0, 0.25f);

		TexturedModel jupyterModel = new TexturedModel(model,
		new ModelTexture(loader.loadTexture("jupyter")));
		Entity jupyter = new Entity(jupyterModel, new Vector3f(40,0,0), 0, 0, 0, 0.6f);

		TexturedModel saturnModel = new TexturedModel(model,
		new ModelTexture(loader.loadTexture("saturn")));
		Entity saturn = new Entity(saturnModel, new Vector3f(55,0,0), 0, 0, 0, 0.5f);

		ModelData ringsData = OBJFileLoader.loadOBJ("rings");
		RawModel ringsD = loader.loadToVAO(ringsData.getVertices(), ringsData.getTextureCoords(),
		ringsData.getNormals(),ringsData.getIndices());

		TexturedModel ringsModel = new TexturedModel(ringsD,
		new ModelTexture(loader.loadTexture("rings")));
		Entity rings = new Entity(ringsModel, new Vector3f(55,0,67), 0, 0, 0, 2.5f);


		TexturedModel uranusModel = new TexturedModel(model,
		new ModelTexture(loader.loadTexture("uranus")));
		Entity uranus = new Entity(uranusModel, new Vector3f(65,0,0), 0, 0, 0, 0.1f);

		TexturedModel neptuneModel = new TexturedModel(model,
		new ModelTexture(loader.loadTexture("neptune")));
		Entity neptune = new Entity(neptuneModel, new Vector3f(70,0,0), 0, 0, 0, 0.1f);

		Light light = new Light(new Vector3f(100,100,100),new Vector3f(1,1,1));
		List<Terrain> terrains = new ArrayList<Terrain>();
		//terrains.add(new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass"))));
		//terrains.add(new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grass"))));
		//terrains.add(new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass"))));
		//terrains.add(new Terrain(-1,0,loader,new ModelTexture(loader.loadTexture("grass"))));
		
		Camera camera = new Camera();	
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			camera.move();
			
			for(Terrain terrain:terrains){
				renderer.processTerrain(terrain);
			}
			renderer.processEntity(solecito);
			renderer.processEntity(mercury);
			renderer.processEntity(venus);
			renderer.processEntity(earth);
			renderer.processEntity(mars);
			renderer.processEntity(jupyter);
			renderer.processEntity(saturn);
			renderer.processEntity(rings);
			renderer.processEntity(uranus);
			renderer.processEntity(neptune);

			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}