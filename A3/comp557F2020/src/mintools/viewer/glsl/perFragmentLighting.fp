#version 400

uniform vec3 kd;
uniform vec3 ks;
uniform vec3 lightPos;
uniform float shininess;
uniform vec3 lightColor;
uniform bool enableLighting;

uniform sampler2D shadowMap; 
uniform float sigma;

in vec3 positionForFP;
in vec3 normalForFP;
in vec4 positionLightCVV;

out vec4 fragColor;
 
 // some help with shadow map antialiasing from :
 // http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-16-shadow-mapping/
 vec2 poissonDisk[16] = vec2[]( 
   vec2( -0.94201624, -0.39906216 ), 
   vec2( 0.94558609, -0.76890725 ), 
   vec2( -0.094184101, -0.92938870 ), 
   vec2( 0.34495938, 0.29387760 ), 
   vec2( -0.91588581, 0.45771432 ), 
   vec2( -0.81544232, -0.87912464 ), 
   vec2( -0.38277543, 0.27676845 ), 
   vec2( 0.97484398, 0.75648379 ), 
   vec2( 0.44323325, -0.97511554 ), 
   vec2( 0.53742981, -0.47373420 ), 
   vec2( -0.26496911, -0.41893023 ), 
   vec2( 0.79197514, 0.19090188 ), 
   vec2( -0.24188840, 0.99706507 ), 
   vec2( -0.81409955, 0.91437590 ), 
   vec2( 0.19984126, 0.78641367 ), 
   vec2( 0.14383161, -0.14100790 ) 
);

void main(void) {
	vec3 rgb = kd;
	if ( enableLighting ) {
		vec3 normal = normalize(normalForFP);
		if ( ! gl_FrontFacing ) normal = -1 * normalForFP;
		vec3 lightDirection = normalize( lightPos - positionForFP );
		float diffuse = max( dot( normal, lightDirection ), 0 );
		vec3 viewDirection = normalize( - positionForFP );
		vec3 halfVector = normalize( lightDirection + viewDirection );
		float specular = max( 0, dot( normal, halfVector ) );
		if (diffuse == 0.0) {
		    specular = 0.0;
		    rgb = vec3(0);
		} else {
	   		specular = pow( specular, shininess );
		
			vec3 scatteredLight = kd * lightColor * diffuse;
			vec3 reflectedLight = ks * lightColor * specular;
			
			vec4 vLn = positionLightCVV / positionLightCVV.w ; // normalize by w
		    vec3 vLnr = vLn.xyz * 0.5  + vec3( 0.5, 0.5, 0.5 );  // and then remapped into values from 0 to 1
		    float shadow = 1.0;
		    if ( positionLightCVV.w > 0.0 ) {
	 	   		for (int i=0;i<16;i++){
	  				float distanceFromLight = texture( shadowMap, vLnr.xy + poissonDisk[i]/700.0 ).r + sigma; // avoid self shadowing
	        		shadow -= distanceFromLight < vLnr.z ? (1.0/16.0) : 0 ;
		    	}      
	    	}
			
		    rgb = min( shadow * (scatteredLight + reflectedLight), vec3(1,1,1) );
		}
 	} 
	fragColor = vec4( rgb ,1 );
}