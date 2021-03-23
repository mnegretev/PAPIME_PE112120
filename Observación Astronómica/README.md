# App para observación astronómica:

Esta secuencia didáctica consiste en el desarrollo de una app para observación astronómica. Para esta secuencia se desarrollaron varios materiales:

* Código fuente para la aplicación móvil en dos versiones, una completa y una con código faltante que el estudiante debe completar.
* La secuencia didáctica, es decir, un documento con instrucciones para la implementación en aula de las actividades.
* Documento técnico de apoyo para los docentes con el resumen de las ecuaciones necesarias para el cálculo de las posiciones de los planetas.

### Código fuente de la aplicación

La app móvil se desarrolló en Android Studio y está pensada para poder ejecutarse en cualquier disposivo Android 6.0 o superior. Se desarrollaron dos versiones de la aplicación: una solución y una con código incompleto que servirá para que los estudiantes pongan en práctica las conceptos vistos en clase mediante la programación de las ecuaciones necesarias. Esta aplicación muestra con realidad aumentada las posiciones del Sol, la Luna, Mercurio, Venus, Marte, Júpiter y Saturno como serían vistos por un observador situado en la Tierra. Para determinar el punto en el que se encuentra el observador se utilizaron las coordenadas del GPS provistas por el mismo dispositivo. Para la realidad aumentada se utilizó la biblioteca OpenCV para Android en lenguaje Java. Mediante esta biblioteca se puede realizar el dibujado en tiempo real de las posiciones de los astros mencionados.

En la carpeta 'SoftwareApp' se encuentran ambas versiones del código, la completa y la que se utilizará con los estudiantes. La siguiente imagen es una captura de pantalla de la aplicación mostrando la posición de saturno y el sol:

<img src="https://github.com/mnegretev/PAPIME_PE112120/blob/master/Media/AppObservacionAstronomica.png" alt="App para observación astronómica" width="640"/>

Un video demostrando el funcionamiento de esta app se encuentra en
https://github.com/mnegretev/PAPIME_PE112120/blob/master/Media/StarGazer.mp4?raw=true

### Secuencia didáctica

El documento 'SecuenciaDidactica.pdf' contiene las instrucciones necesarias para implementación en aula de las actividades de aprendizaje. Este documento contiene los objetivos de aprendizaje, sugerencias para la contextualización, desarrollo y cierre; así como la distribución de horas sugeridas para cada actividad. Contiene también la lista de materiales a utilizar y una serie de enlaces a recursos que pueden resultar útiles para los estudiantes.
https://github.com/mnegretev/PAPIME_PE112120/blob/master/Observaci%C3%B3n%20Astron%C3%B3mica/SecuenciaDidactica.pdf

### Resumen de ecuaciones

En la carpeta 'Resumen_de_Ecuaciones' se encuentra un documento técnico dirigido a los docentes. Este documento no aborda la parte educativa sino la parte meramente relacionada con las matemáticas a fin de que los docentes dominen totalmente el tema. Por ser un texto académico, está escrito en LaTex, sin embargo, en la misma carpeta se encuentra una versión compilada del pdf:
https://github.com/mnegretev/PAPIME_PE112120/blob/master/Observaci%C3%B3n%20Astron%C3%B3mica/Resumen_de_Ecuaciones/Resumen_de_Ecuaciones.pdf