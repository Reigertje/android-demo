precision mediump float;

uniform sampler2D diffuse_sampler;
uniform vec4 multiply_color;
uniform vec4 mix_color;

varying vec2 fragment_uv;

void main() {
        vec4 diffuse = texture2D(diffuse_sampler, fragment_uv);

        vec3 weights = vec3(0.2126, 0.7152, 0.0722); // /luminance weights

        vec3 color_rgb = multiply_color.rgb * mix(diffuse.rgb, mix_color.rgb * diffuse.a, mix_color.a) * multiply_color.a;

        vec3 weighed = weights * color_rgb;

        float sum = weighed.r + weighed.g + weighed.b;

    	gl_FragColor.rgb = 0.5 * vec3(sum, sum, sum);


    	gl_FragColor.a = multiply_color.a * diffuse.a;
}