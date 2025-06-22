# BD_MySQL – Sistema de Gestión de Categorías de Clientes

Este proyecto implementa una interfaz gráfica en Java para gestionar categorías de clientes, utilizando una base de datos MySQL.

## 📁 Estructura del Proyecto
BD_MySQL/ ├── bin/                      # Archivos compilados (.class) ├── lib/                      # Librerías externas (MySQL Connector) │   └── mysql-connector-j-9.3.0.jar ├── src/                      # Código fuente │   ├── conexion/ │   ├── dao/ │   ├── modelo/ │   └── gui/ ├── script.txt                # Script SQL para crear la base de datos └── README.md                 # Este archivo


## 🚀 Cómo ejecutar el programa

Desde la terminal en VsCode:

```powershell
javac -d bin -cp "lib/mysql-connector-j-9.3.0.jar" src/modelo/*.java src/conexion/*.java src/dao/*.java src/gui/*.java
java -cp "bin;lib/mysql-connector-j-9.3.0.jar" gui.CategoriaClienteFrame

🛠️ Requisitos
- JDK 8 o superior
- MySQL Server
- MySQL Workbench (opcional)
- Conector JDBC: mysql-connector-j-9.3.0.jar

