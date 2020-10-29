#version 400

uniform float ambient;
uniform vec3 kd;
uniform vec3 ks;
uniform float shine;
uniform vec3 lightDir;
uniform vec3 lightColor;
uniform vec3 halfVector;

in vec4 color;
in vec3 normalForFP;

out vec4 fragColor;
out float diffuse;
out vec3 ld;

// Objective 7, GLSL lighting
// Student Name: Alexandra Livadas
// Student ID: 260709168

void main(void) {
   //Lambertian:
   float diffuse = max(0.0, dot(normalForFP, lightDir));
   vec3 ld = lightColor * diffuse;
   
   //Blinn-Phong
   float specular = max(0.0, dot(normalForFP, halfVector));
   
   //Combining Lambertian and Blinn-Phong
   if (diffuse == 0.0) {
   		specular = 0.0;
   }
   else {
   		specular = pow(specular, shine);
   }
   vec3 scatteredLight = ambient + ld*kd;
   vec3 reflectedLight = lightColor * specular * ks;
   vec3 rgb = min(color.rgb*scatteredLight + reflectedLight, vec3(1.0));
   fragColor = vec4(rgb, color.a);   
}