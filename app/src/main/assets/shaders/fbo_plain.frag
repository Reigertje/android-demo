precision mediump float;

uniform sampler2D diffuse_sampler;

uniform float wave_offset;

varying vec2 fragment_uv;

void main() {

        vec2 wave_uv = fragment_uv;

        wave_uv.y = fragment_uv.y - (0.003 + sin(10.0 * fragment_uv.x + wave_offset) * 0.003) * 0.5;

        vec4 diffuse = texture2D(diffuse_sampler, wave_uv);

    	gl_FragColor.rgba = diffuse;
}