#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;
varying vec4 v_color;

uniform sampler2D u_texture;
uniform float u_resolution;
uniform float u_radius;
uniform float u_alpha;

void main() {
    vec4 sum = vec4(0.0);
    float blur = u_radius / u_resolution;

    sum += texture2D(u_texture, v_texCoord + vec2(-4.0 * blur, 0.0)) * 0.05;
    sum += texture2D(u_texture, v_texCoord + vec2(-3.0 * blur, 0.0)) * 0.09;
    sum += texture2D(u_texture, v_texCoord + vec2(-2.0 * blur, 0.0)) * 0.12;
    sum += texture2D(u_texture, v_texCoord + vec2(-1.0 * blur, 0.0)) * 0.15;
    sum += texture2D(u_texture, v_texCoord) * 0.16;
    sum += texture2D(u_texture, v_texCoord + vec2(1.0 * blur, 0.0)) * 0.15;
    sum += texture2D(u_texture, v_texCoord + vec2(2.0 * blur, 0.0)) * 0.12;
    sum += texture2D(u_texture, v_texCoord + vec2(3.0 * blur, 0.0)) * 0.09;
    sum += texture2D(u_texture, v_texCoord + vec2(4.0 * blur, 0.0)) * 0.05;

    if (u_radius == 0.0 && sum.a > 0.0) {
        sum.a = u_alpha * texture2D(u_texture, v_texCoord).a;
    } else {
        sum.a *= u_alpha;
    }

    gl_FragColor = sum;
}
