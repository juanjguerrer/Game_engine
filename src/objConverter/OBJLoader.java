package objConverter;

import models.RawModel;
import renderEngine.Loader;

public class OBJLoader {

    public static RawModel loadObjModel(String objFilename, Loader loader){
        ModelData data = OBJFileLoader.loadOBJ(objFilename);
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        return model;
    }
}
