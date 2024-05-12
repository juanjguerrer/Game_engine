package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.OBJLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
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
import guis.GuiRenderer;
import guis.GuiTexture;

public class MainGameLoop {
	private static final float RED = 0.53f;
	private static final float GREEN = 0.8f;
	private static final float BLUE = 0.92f;

	public static void main(String[] args){
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
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
		fernTextureAtlas);

		TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader),
		new ModelTexture(loader.loadTexture("lowPolyTree")));

		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), 
		new ModelTexture(loader.loadTexture("lamp")));
		
		lamp.getTexture().setUseFakeLighting(true);
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		
		Terrain[][] terrain;
		terrain = new Terrain[2][2];
		Terrain terrain1 = new Terrain(0,-1,loader,texturePack,
		blendMap, "heightmap"); // De 0 a 800 y de 0 a -800
		Terrain terrain2 = new Terrain(-1,-1,loader,texturePack,
		blendMap, "heightmap"); // De 0 a -800 y de 0 a -800
		Terrain terrain3 = new Terrain(0,0,loader,texturePack,
		blendMap, "heightmap"); // De 0 a 800 y de 0 a 800
		Terrain terrain4 = new Terrain(-1,0,loader,texturePack,
		blendMap, "heightmap"); // De 0 a -800 y de 0 a 800
		terrain[0][0] = terrain1;
		terrain[1][0] = terrain2;
		terrain[0][1] = terrain3;
		terrain[1][1] = terrain4;



		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<400;i++){
			if(i%2==0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain[get_i(x,z)][get_j(x,z)].getHeightOfTerrain(x, z);
				entities.add(new Entity(fern, random.nextInt(4),new Vector3f( x, y, z),0, random.nextFloat()*360,0,0.9f));
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain[get_i(x,z)][get_j(x,z)].getHeightOfTerrain(x, z);
				entities.add(new Entity(bobble, new Vector3f(x, y, z), 0,
				random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));

				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrain[get_i(x,z)][get_j(x,z)].getHeightOfTerrain(x, z);
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
			}
		}
		
		Light light = new Light(new Vector3f(0,100,-700),new Vector3f(RED,BLUE,GREEN));
		List<Light> lights = new ArrayList<Light>();
		lights.add(light);
		lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0,2,2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2,2,0), new Vector3f(1, 0.01f, 0.002f)));

		entities.add(new Entity(lamp, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1));

		MasterRenderer renderer = new MasterRenderer(loader);
		
		RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player player = new Player(stanfordBunny, new Vector3f(153, 5, -274), 0, 100, 0, 0.6f);
		Camera camera = new Camera(player);	
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"),
		new Vector2f(0.5f,0.5f), new Vector2f(0.25f,0.25f));
		guis.add(gui);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		while(!Display.isCloseRequested()){
			player.move(terrain[get_i(player.getPosition().x,player.getPosition().z)]
			[get_j(player.getPosition().x,player.getPosition().z)]); 
			camera.move();
			renderer.processEntity(player);
			renderer.processTerrains(terrain);
			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			renderer.render(lights, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

	private static int get_j(float x, float z) {
		if(x>=0 && z<=0){
			return 0;
		}
		else if(x<0 && z<0){
			return 0;
		}
		else if(x>0 && z>0){
			return 1;
		}
		else if(x<0 && z>0){
			return 1;
		}
		return 0;
	}

	private static int get_i(float x, float z) {
		if(x>=0 && z<=0){
			return 0;
		}
		else if(x<0 && z<0){
			return 1;
		}
		else if(x>0 && z>0){
			return 0;
		}
		else if(x<0 && z>0){
			return 1;
		}
		return 0;
	}

}
/*
 * hot to know in which cuadrant a coordinate pair is?
The first one consist of this points 0,0;0,-800; 800,-800;800,0
second one 0,0,
 */