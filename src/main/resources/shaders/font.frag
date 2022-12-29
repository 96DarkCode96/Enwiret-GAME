#version 400 core

in vec2 pass_texture;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;

uniform float width;
uniform float edge;

void main() {

    float distance = 1-texture(fontAtlas, pass_texture).a;
    float alpha = 1-smoothstep(width, width+edge, distance);

    out_color = vec4(color, alpha);
}