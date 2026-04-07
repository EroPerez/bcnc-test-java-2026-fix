# bcnc-test-java-2026 🚀
## Introducción 📚
Este proyecto, denominado `bcnc-test-java-2026`, tiene como objetivo demostrar habilidades y conocimientos en programación Java. A continuación, se presentan los detalles y la estructura del proyecto.

## Descripción del Proyecto 📋

En la base de datos de comercio electrónico de la compañía disponemos de la tabla PRICES que refleja el precio final (pvp) y la tarifa que aplica a un producto de una cadena entre unas fechas determinadas. A continuación se muestra un ejemplo de la tabla con los campos relevantes:

PRICES
-------

| BRAND_ID | START_DATE              | END_DATE                | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|----------|-------------------------|-------------------------|------------|------------|----------|-------|------|
| 1        | 2020-06-14-00.00.00     | 2020-12-31-23.59.59     | 1          | 35455      | 0        | 35.50 | EUR  |
| 1        | 2020-06-14-15.00.00     | 2020-06-14-18.30.00     | 2          | 35455      | 1        | 25.45 | EUR  |
| 1        | 2020-06-15-00.00.00     | 2020-06-15-11.00.00     | 3          | 35455      | 1        | 30.50 | EUR  |
| 1        | 2020-06-15-16.00.00     | 2020-12-31-23.59.59     | 4          | 35455      | 1        | 38.95 | EUR  |

Campos: 

- BRAND_ID: foreign key de la cadena del grupo (1 = ZARA).
- START_DATE , END_DATE: rango de fechas en el que aplica el precio tarifa indicado.
- PRICE_LIST: Identificador de la tarifa de precios aplicable.
- PRODUCT_ID: Identificador código de producto.
- PRIORITY: Desambiguador de aplicación de precios. Si dos tarifas coinciden en un rago de fechas se aplica la de mayor prioridad (mayor valor numérico).
- PRICE: precio final de venta.
- CURR: iso de la moneda.

Se pide:

Construir una aplicación/servicio en SpringBoot que provea una end point rest de consulta  tal que:

Acepte como parámetros de entrada: fecha de aplicación, identificador de producto, identificador de cadena.
Devuelva como datos de salida: identificador de producto, identificador de cadena, tarifa a aplicar, fechas de aplicación y precio final a aplicar.

Se debe utilizar una base de datos en memoria (tipo h2) e inicializar con los datos del ejemplo, (se pueden cambiar el nombre de los campos y añadir otros nuevos si se quiere, elegir el tipo de dato que se considere adecuado para los mismos).
              
Desarrollar unos test al endpoint rest que  validen las siguientes peticiones al servicio con los datos del ejemplo:
                                                                                       
-          Test 1: petición a las 10:00 del día 14 del producto 35455   para la brand 1 (ZARA)
-          Test 2: petición a las 16:00 del día 14 del producto 35455   para la brand 1 (ZARA)
-          Test 3: petición a las 21:00 del día 14 del producto 35455   para la brand 1 (ZARA)
-          Test 4: petición a las 10:00 del día 15 del producto 35455   para la brand 1 (ZARA)
-          Test 5: petición a las 21:00 del día 16 del producto 35455   para la brand 1 (ZARA)
 
 
Se valorará:

- Diseño y construcción del servicio.
- Calidad de Código.
- Resultados correctos en los test.

## Estructura del Proyecto 🗂️
El proyecto consta de los siguientes archivos y directorios:
- `demo/`: Directorio que contiene el código fuente del proyecto.
- `README.md`: Este archivo, que proporciona información general sobre el proyecto.

## Requisitos del Sistema 📊
Para ejecutar este proyecto, se requieren los siguientes elementos:
- Java Development Kit (JDK) 21 o superior 📈
- Un entorno de desarrollo integrado (IDE) como Eclipse, IntelliJ IDEA, o similar 📊

## Compilación y Ejecución 🔄
1. **Clonar el repositorio**: Utilice el comando `git clone` para obtener una copia local del proyecto.
2. **Abrir en un IDE**: Abra el proyecto en su IDE preferido.
3. **Compilar**: Utilice el comando `javac` o la función de compilación de su IDE para compilar los archivos `.java`.
4. **Ejecutar**: Ejecute las clases compiladas utilizando `java` o la función de ejecución de su IDE.

## Contribuciones 🤝
Se agradece cualquier contribución al proyecto. Para contribuir, por favor:
1. **Fork del repositorio**: Cree una copia del proyecto en su cuenta de GitHub.
2. **Realizar cambios**: Haga los cambios deseados en su copia del proyecto.
3. **Crear pull request**: Envíe una solicitud de cambios al repositorio original.

## Licencia 📜
Este proyecto se encuentra bajo la licencia [MIT](https://opensource.org/licenses/MIT). Usted es libre de utilizar, modificar y distribuir el código fuente.

## Contacto 📲
Para cualquier consulta o asistencia, no dude en contactarnos a través de [correo electrónico](mailto:eperezm1986@gmail.com) o abrir una issue en el repositorio.

¡Gracias por considerar el proyecto `bcnc-test-java-2026`! 🙏
