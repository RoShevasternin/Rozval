#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform sampler2D u_texture;    // Основна текстура
uniform sampler2D u_mask;       // Текстура маски
uniform float u_alpha;          // Альфа-канал групи

void main() {
    vec4 maskColor = texture2D(u_mask, v_texCoord);  // Отримуємо колір з маски
    vec4 texColor = texture2D(u_texture, v_texCoord);     // Отримуємо колір з основної текстури

    // Використовуємо альфа-канал маски для визначення прозорості текстури
    texColor.a *= maskColor.a;

    // Враховуємо альфа-канал групи (u_alpha)
    texColor.a *= u_alpha;

    gl_FragColor = texColor;  // Повертаємо фінальний колір з урахуванням альфа-каналу групи
}
