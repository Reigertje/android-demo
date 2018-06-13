uniform mat4 mvp;

attribute vec2 vertex_xy;
attribute vec2 vertex_uv;

varying vec2 fragment_uv;

void main() {
    fragment_uv = vertex_uv;
    gl_Position =  mvp * vec4(vertex_xy, 0.0, 1.0);
}