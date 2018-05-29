precision mediump float;

uniform sampler2D diffuse_sampler;

varying vec2 fragment_uv;

void main() {
    vec4 diffuse = texture2D(diffuse_sampler, fragment_uv);
	gl_FragColor = diffuse;
}