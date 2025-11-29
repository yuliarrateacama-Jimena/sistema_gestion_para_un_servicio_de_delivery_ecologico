
EcoDelivery: Sistema de Gestión de Delivery Ecológico

EcoDelivery es un sistema de gestión implementado en Java para un servicio de delivery enfocado en productos ecológicos. Integra estructuras de datos personalizadas para manejar inventario, pedidos, entregas, categorías y historial de operaciones, con una interfaz de usuario basada en consola. El proyecto demuestra aplicaciones prácticas de algoritmos y estructuras de datos en un contexto real, priorizando eficiencia y usabilidad.

Requisitos
- Java 8 o superior.
- No se requieren dependencias externas; el proyecto es autónomo.

 Instalación
1. Clona el repositorio: `git clone <URL-del-repositorio>`.
2. Navega al directorio del proyecto: `cd EcoDelivery`.
3. Compila las clases: `javac -d bin src/**/*.java` (asumiendo estructura de paquetes en `src`).
4. Ejecuta la aplicación principal: `java -cp bin interfaz.MenuPrincipal`.
Uso
- Al iniciar, se muestra un menú principal con opciones para gestionar inventario, pedidos, categorías, entregas y reportes.
- Navega usando números de opciones; confirma acciones sensibles con S/N.
- Para datos de prueba: Ejecuta `DatosPrueba.cargarDatos()` en el código o manualmente a través de menús.

 Estructuras de Datos Implementadas
- Lineales**: Listas enlazadas para categorías y pedidos procesados; colas para entregas pendientes; pilas para historial.
- No Lineales**: Árbol AVL para inventario (búsquedas eficientes); heap en colas de prioridad para pedidos urgentes.
Contribuyendo
- Forkea el repositorio y crea un pull request con mejoras.
- Reporta issues para bugs o sugerencias.
