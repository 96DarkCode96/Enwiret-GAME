package me.darkcode.render;

import me.darkcode.WindowReference;

public interface Renderer {

    public boolean needRender(WindowReference windowReference);
    public void render(WindowReference windowReference, int tick);
    public void destroy();

}