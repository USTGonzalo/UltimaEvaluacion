# 📱 Gestor de Movimientos -- Android App

Gestor de Movimientos es una aplicación Android desarrollada en **Java**
que permite registrar, visualizar y administrar tus **ingresos y
gastos** de manera simple y ordenada.\
Incluye soporte para fotografías, categorías personalizadas y conversión
de montos mediante la API de **ExchangeRateHost**.

## 🧩 Características principales

### ✔️ Registro de movimientos

Cada movimiento incluye: - **Monto** - **Fecha** - **Tipo** (Ingreso /
Gasto) - **Categoría** - **Fotografía opcional** - **ID autogenerado**

### ✔️ Gestor de categorías

-   Crear, ver y eliminar categorías.
-   Asociar cada movimiento a una categoría concreta.

### ✔️ Edición de movimientos

-   Seleccionar un movimiento desde una lista de IDs.
-   Editar sus valores.
-   Actualizar o eliminar registros.

### ✔️ Visualización

-   Lista con tarjetas mostrando ID, fecha, monto, tipo y categoría.

### ✔️ Conversión automática de divisas

Usa la API:

https://api.exchangerate.host/live

## 🗄️ Arquitectura del proyecto

    app/
     ├── java/com/example/gestion/
     ├── res/
     ├── AndroidManifest.xml

## 🧬 Base de datos (SQLite)

### Movements

  Campo      Tipo         Descripción
  ---------- ------------ ---------------------
  id         INTEGER PK   Identificador
  mount      TEXT         Monto
  date       TEXT         Fecha
  type       INTEGER      1 ingreso / 0 gasto
  category   INTEGER      Categoría
  photo      TEXT         URI imagen

### Categories

  Campo   Tipo
  ------- ---------
  id      INTEGER
  name    TEXT

## 🌐 API utilizada

ExchangeRateHost -- https://api.exchangerate.host/live

## 🚀 Cómo compilar

-   Clonar repo\
-   Abrir en Android Studio\
-   Sincronizar Gradle\
-   Ejecutar

## 🧑‍💻 Autor

Desarrollado por **Gonzalo Fuentes**.
