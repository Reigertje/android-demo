precision mediump float;

uniform sampler2D diffuse_sampler;
uniform vec4 multiply_color;
uniform vec4 mix_color;

varying vec2 fragment_uv;

void main() {
        vec4 diffuse = texture2D(diffuse_sampler, fragment_uv);

    	gl_FragColor.rgb = multiply_color.rgb * mix(diffuse.rgb, mix_color.rgb * diffuse.a, mix_color.a) * multiply_color.a;
    	gl_FragColor.a = multiply_color.a * diffuse.a;

}