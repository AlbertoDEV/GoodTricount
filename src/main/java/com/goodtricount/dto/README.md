# GoodTricount DTOs

Este paquete contiene los Data Transfer Objects (DTOs) para la aplicación GoodTricount. Estos DTOs son clases Java simples que se utilizan para transferir datos entre diferentes capas de la aplicación.

## Clases DTO

### UserDTO
Representa la información de un usuario en el sistema.

**Campos:**
- `username`: Nombre de usuario (identificador único)
- `password`: Contraseña del usuario
- `email`: Correo electrónico del usuario
- `name`: Nombre completo del usuario

### ExpenseDTO
Representa un gasto realizado en un grupo.

**Campos:**
- `payer`: Nombre de usuario de quien pagó
- `amount`: Cantidad pagada (BigDecimal)
- `description`: Descripción del gasto

### PaymentDTO
Representa un pago entre usuarios para saldar deudas.

**Campos:**
- `payer`: Nombre de usuario de quien paga
- `receiver`: Nombre de usuario de quien recibe
- `amount`: Cantidad pagada (BigDecimal)
- `status`: Estado del pago ("pending" o "confirmed")
- `timestamp`: Fecha y hora de creación del pago
- `confirmedTimestamp`: Fecha y hora de confirmación del pago

### GroupDTO
Representa un grupo de gastos compartidos.

**Campos:**
- `id`: Identificador único del grupo
- `name`: Nombre del grupo
- `participants`: Lista de nombres de usuario de los participantes
- `admins`: Lista de nombres de usuario de los administradores
- `expenses`: Lista de gastos (ExpenseDTO)
- `payments`: Lista de pagos (PaymentDTO)

**Métodos adicionales:**
- `addParticipant(String username)`: Añade un participante al grupo
- `addAdmin(String username)`: Añade un administrador al grupo
- `addExpense(ExpenseDTO expense)`: Añade un gasto al grupo
- `addPayment(PaymentDTO payment)`: Añade un pago al grupo

## Ejemplo de Uso

Consulta la clase `DTOExample.java` para ver un ejemplo completo de cómo utilizar estos DTOs.

```java
// Crear usuarios
UserDTO user1 = new UserDTO("pepe", "123", "pepe@mail.com", "Pepe");
UserDTO user2 = new UserDTO("juan", "123", "juan@mail.com", "Juan");

// Crear un grupo
GroupDTO group = new GroupDTO("g1", "Viaje a Roma");

// Añadir participantes
group.addParticipant(user1.getUsername());
group.addParticipant(user2.getUsername());

// Añadir administrador
group.addAdmin(user1.getUsername());

// Añadir gastos
ExpenseDTO expense = new ExpenseDTO(user1.getUsername(), new BigDecimal("150"), "Hotel");
group.addExpense(expense);
```

## Notas de Implementación

- Todos los DTOs implementan `Serializable` para permitir su serialización.
- Se utiliza `BigDecimal` para los valores monetarios para evitar problemas de precisión.
- Se utiliza `LocalDateTime` para las marcas de tiempo.
- Las colecciones se inicializan como `ArrayList` vacíos para evitar `NullPointerException`.