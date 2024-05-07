package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.OBJLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		//****** TERRAIN TEXTURE  ******//
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));	
		//************************************************************************************************

		RawModel treeModel = OBJLoader.loadObjModel("tree",loader);
		TexturedModel tree = new TexturedModel(treeModel,new ModelTexture(loader.loadTexture("tree")));

		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
		new ModelTexture(loader.loadTexture("grassTexture")));
		
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
		new ModelTexture(loader.loadTexture("flower")));
		
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
		new ModelTexture(loader.loadTexture("fern")));

		TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader),
		new ModelTexture(loader.loadTexture("lowPolyTree")));

		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<400;i++){
			if(i%7==0){
				entities.add(new Entity(grass, new Vector3f(random.nextFloat()*400 - 200,0,random.nextFloat() * -400),
				0,0,0,1));
				entities.add(new Entity(flower, new Vector3f(random.nextFloat()*400 - 200,0,random.nextFloat() * -400),
            	0,0,0,2.3f));
			}

			entities.add(new Entity(tree, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),
			0,0,0,3));
			if(i%3==0){
				entities.add(new Entity(fern, new Vector3f(random.nextFloat()*400 - 200,0,random.nextFloat() * -400),
				0,random.nextFloat()*360,0,0.9f));

				entities.add(new Entity(bobble, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat()*-600),
				0,random.nextFloat()*360,0,random.nextFloat()*0.1f + 0.6f));

				entities.add(new Entity(tree, new Vector3f(random.nextFloat()*800 -400, 0, random.nextFloat()*-600),
				0, 0, 0, random.nextFloat()+4));
			}
		}
			
		
		Light light = new Light(new Vector3f(20000,40000,20000),new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0,-1,loader,texturePack,blendMap);
		Terrain terrain2 = new Terrain(-1,-1,loader,texturePack,blendMap);
		Terrain terrain3 = new Terrain(0,0,loader,texturePack,blendMap);
		Terrain terrain4 = new Terrain(-1,0,loader,texturePack,blendMap);
		
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player player = new Player(stanfordBunny, new Vector3f(100, 0, -50), 0, 180, 0, 0.6f);
		Camera camera = new Camera(player);	
		
		while(!Display.isCloseRequested()){
			camera.move();
			player.move();
			renderer.processEntity(player);
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