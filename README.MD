# BD_MySQL – Sistema de Gestión de Categorías de Clientes

Este proyecto implementa una interfaz gráfica en Java para gestionar categorías de clientes, utilizando una base de datos MySQL.
El script para la creación de BD en Workbench esta en script.txt

## 📁 Estructura del Proyecto

```
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
```

## 🚀 Cómo ejecutar el programa

Desde la terminal en Visual Studio Code:

```powershell
javac -d bin -cp "lib/mysql-connector-j-9.3.0.jar" src/modelo/*.java src/conexion/*.java src/dao/*.java src/gui/*.java
java -cp "bin;lib/mysql-connector-j-9.3.0.jar" gui.CategoriaClienteFrame
```

## 🛠️ Requisitos

- JDK 8 o superior
- MySQL Server
- MySQL Workbench
- Conector JDBC: `mysql-connector-j-9.3.0.jar`

## 📌 Notas

- Asegúrate de que el driver JDBC se encuentra correctamente en la carpeta `lib/`.
- Verifica que tu base de datos esté activa y que las credenciales de conexión estén correctamente configuradas.
- Si se produce un error relacionado con el driver JDBC, revisa que el nombre de clase del driver sea `com.mysql.cj.jdbc.Driver`.