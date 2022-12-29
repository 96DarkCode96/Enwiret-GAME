package me.darkcode.shader;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class FontShader extends ShaderProgram{

	public FontShader() {
		super("./src/main/resources/shaders/font.vert", "./src/main/resources/shaders/font.frag");
	}

	private int color;
	private int translation;
	private int width;
	private int edge;

	@Override
	protected void getAllUniformLocations() {
		color = getUniformLocation("color");
		translation = getUniformLocation("translation");
		width = getUniformLocation("width");
		edge = getUniformLocation("edge");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "texture_uv");
	}

	public void loadColor(Vector3f color){
		loadVector(this.color, color);
	}

	public void loadTranslation(Vector2f translation){
		load2DVector(this.translation, translation);
	}

	public void loadWidth(float width){
		loadFloat(this.width, width);
	}

	public void loadEdge(float edge){
		loadFloat(this.edge, edge);
	}

}
