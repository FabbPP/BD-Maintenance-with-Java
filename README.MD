# BD_MySQL – Sistema de Gestión de Categorías de Clientes

Este proyecto implementa una interfaz gráfica en Java para gestionar categorías de clientes, utilizando una base de datos MySQL.

## 📁 Estructura del Proyecto
C:.
├───bin
│   ├───conexion
│   ├───dao
│   ├───gui
│   └───modelo
├───lib
└───src
    ├───conexion
    ├───dao
    ├───gui
    └───modelo


## 🚀 Cómo ejecutar el programa

Desde la terminal en VsCode:

```powershell
javac -d bin -cp "lib/mysql-connector-j-9.3.0.jar" src/modelo/*.java src/conexion/*.java src/dao/*.java src/gui/*.java
java -cp "bin;lib/mysql-connector-j-9.3.0.jar" gui.CategoriaClienteFrame
powershell´´´

🛠️ Requisitos
- JDK 8 o superior
- MySQL Server
- MySQL Workbench
- Conector JDBC: mysql-connector-j-9.3.0.jar

