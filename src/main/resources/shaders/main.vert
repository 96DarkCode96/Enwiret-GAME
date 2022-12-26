#version 400 core

in vec3 position;
in vec2 texture_uv;
in vec3 normals;

out vec2 texture_uv_pass;

uniform mat4 transform_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;

void main() {

    gl_Position = projection_matrix * view_matrix * transform_matrix * vec4(position, 1.0);
    texture_uv_pass = texture_uv;

}