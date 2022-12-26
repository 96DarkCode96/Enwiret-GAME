#version 400 core

in vec2 texture_uv_pass;

out vec4 color_out;

uniform sampler2D textureSampler;

void main() {

    color_out = texture(textureSampler, texture_uv_pass);

}