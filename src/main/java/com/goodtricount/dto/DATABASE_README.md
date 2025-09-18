# GoodTricount Database Connection

This package contains classes for connecting to and interacting with a PostgreSQL database for the GoodTricount application.

## Connection Configuration

The database connection is configured in the `DatabaseConnection` class. The connection URL is specified in the following format:

```
${{ Postgres.DATABASE_URL }}
```

Where `DATABASE_URL` is the actual connection URL:

```
postgresql://postgres:GchuyqDtKfFufefxFnYGEGmIEbSJPryY@postgres.railway.internal:5432/railway
```

## Database Schema

The database schema is defined in the `BBDD.md` file and includes the following tables:

- `Users`: Stores user information
- `Groups`: Stores group information
- `GroupParticipants`: Stores the relationship between groups and participants
- `GroupAdmins`: Stores the relationship between groups and admins
- `Expenses`: Stores expense information
- `Payments`: Stores payment information

## Classes Overview

### DatabaseConnection

Handles the connection to the PostgreSQL database.

**Methods:**
- `getConnection()`: Get a connection to the database
- `closeConnection()`: Close the database connection

### UserDAO

Data Access Object for User entities.

**Methods:**
- `insertUser(UserDTO user)`: Insert a new user
- `getUserByUsername(String username)`: Get a user by username
- `getAllUsers()`: Get all users
- `updateUser(UserDTO user)`: Update a user
- `deleteUser(String username)`: Delete a user
- `userExists(String username)`: Check if a user exists

### GroupDAO

Data Access Object for Group entities.

**Methods:**
- `insertGroup(GroupDTO group)`: Insert a new group
- `getGroupById(String groupId)`: Get a group by ID
- `getAllGroups()`: Get all groups
- `getGroupsForUser(String username)`: Get groups for a user
- `updateGroup(GroupDTO group)`: Update a group
- `deleteGroup(String groupId)`: Delete a group
- `addParticipant(String groupId, String username)`: Add a participant to a group
- `addAdmin(String groupId, String username)`: Add an admin to a group
- `isParticipant(String groupId, String username)`: Check if a user is a participant
- `isAdmin(String groupId, String username)`: Check if a user is an admin

### ExpenseDAO

Data Access Object for Expense entities.

**Methods:**
- `insertExpense(String groupId, ExpenseDTO expense)`: Insert a new expense
- `getExpenseById(int expenseId)`: Get an expense by ID
- `getExpensesForGroup(String groupId)`: Get expenses for a group
- `updateExpense(int expenseId, ExpenseDTO expense)`: Update an expense
- `deleteExpense(int expenseId)`: Delete an expense
- `getTotalExpensesForGroup(String groupId)`: Get the total amount of expenses for a group
- `getTotalExpensesPaidByUser(String groupId, String username)`: Get the total amount of expenses paid by a user

### PaymentDAO

Data Access Object for Payment entities.

**Methods:**
- `insertPayment(String groupId, PaymentDTO payment)`: Insert a new payment
- `getPaymentById(int paymentId)`: Get a payment by ID
- `getPaymentsForGroup(String groupId)`: Get payments for a group
- `getPaymentsMadeByUser(String groupId, String username)`: Get payments made by a user
- `getPaymentsReceivedByUser(String groupId, String username)`: Get payments received by a user
- `updatePayment(int paymentId, PaymentDTO payment)`: Update a payment
- `confirmPayment(int paymentId)`: Confirm a payment
- `deletePayment(int paymentId)`: Delete a payment
- `getTotalPaymentsMadeByUser(String groupId, String username)`: Get the total amount of payments made by a user
- `getTotalPaymentsReceivedByUser(String groupId, String username)`: Get the total amount of payments received by a user

### DatabaseManager

Provides a unified interface for all database operations.

**Methods:**
- `getInstance()`: Get the singleton instance of the DatabaseManager
- `initializeDatabase()`: Initialize the database by creating all necessary tables
- `getUserDAO()`: Get the UserDAO instance
- `getGroupDAO()`: Get the GroupDAO instance
- `getExpenseDAO()`: Get the ExpenseDAO instance
- `getPaymentDAO()`: Get the PaymentDAO instance
- `closeConnection()`: Close the database connection
- `testConnection()`: Test the database connection

## Usage Example

```java
// Get the DatabaseManager instance
DatabaseManager dbManager = DatabaseManager.getInstance();

// Initialize the database
dbManager.initializeDatabase();

// Get the UserDAO
UserDAO userDAO = dbManager.getUserDAO();

// Create a new user
UserDTO user = new UserDTO("username", "password", "email@example.com", "Full Name");
userDAO.insertUser(user);

// Get a user
UserDTO retrievedUser = userDAO.getUserByUsername("username");

// Close the connection when done
dbManager.closeConnection();
```

For a complete example, see the `DatabaseTest` class.