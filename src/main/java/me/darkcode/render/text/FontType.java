package me.darkcode.render.text;

import me.darkcode.WindowReference;

import java.io.File;

public class FontType {

	private int textureAtlas;
	private TextMeshCreator loader;

	public FontType(int textureAtlas, WindowReference windowReference, File fontFile) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshCreator(windowReference, fontFile);
	}

	public int getTextureAtlas() {
		return textureAtlas;
	}

	public TextMeshData loadText(GUIText text) {
		return loader.createTextMesh(text);
	}

}
