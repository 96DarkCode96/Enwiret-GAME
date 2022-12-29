package me.darkcode.render;

import me.darkcode.WindowReference;

public interface Renderer {

    boolean needRender(WindowReference windowReference);

    void render(WindowReference windowReference, int tick);

    void destroy();

    void mousePos(float x, float y);

    void mouseKey(int button, int action, int mods);

    void keyboardKey(int key, int scancode, int action, int mods);

    void charKey(int codepoint);
}