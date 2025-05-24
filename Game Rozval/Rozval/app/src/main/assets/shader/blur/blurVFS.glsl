#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform sampler2D u_texture;
uniform float u_resolution;
uniform float u_radius;

void main() {
    vec4 sum = vec4(0.0);
    float blur = u_radius / u_resolution;

    sum += texture2D(u_texture, v_texCoord + vec2(0.0, -4.0 * blur)) * 0.05;
    sum += texture2D(u_texture, v_texCoord + vec2(0.0, -3.0 * blur)) * 0.09;
    sum += texture2D(u_texture, v_texCoord + vec2(0.0, -2.0 * blur)) * 0.12;
    sum += texture2D(u_texture, v_texCoord + vec2(0.0, -1.0 * blur)) * 0.15;
    sum += texture2D(u_texture, v_texCoord) * 0.16;
    sum += texture2D(u_texture, v_texCoord + vec2(0.0, 1.0 * blur)) * 0.15;
    sum += texture2D(u_texture, v_texCoord + vec2(0.0, 2.0 * blur)) * 0.12;
    sum += texture2D(u_texture, v_texCoord + vec2(0.0, 3.0 * blur)) * 0.09;
    sum += texture2D(u_texture, v_texCoord + vec2(0.0, 4.0 * blur)) * 0.05;

    if (u_radius == 0.0 && sum.a > 0.0) {
        sum.a = texture2D(u_texture, v_texCoord).a;
    }

    gl_FragColor = sum;
}
