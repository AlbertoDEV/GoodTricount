/**
 * GoodTricount - Frontend Script
 *
 * Este archivo contiene la lógica del frontend de la aplicación GoodTricount.
 * Se ha integrado con los DTOs de Java para facilitar la comunicación con el backend.
 *
 * Los DTOs utilizados son:
 * - UserDTO: Para gestionar la información de usuarios
 * - ExpenseDTO: Para gestionar los gastos
 * - PaymentDTO: Para gestionar los pagos entre usuarios
 * - GroupDTO: Para gestionar los grupos y sus participantes
 *
 * Se han implementado funciones de utilidad para convertir entre objetos JavaScript y DTOs de Java.
 */

// --- Elementos del DOM ---
const loginScreen = document.getElementById('login-screen');
const registerScreen = document.getElementById('register-screen');
const mainScreen = document.getElementById('main-menu-screen');
const groupDetailScreen = document.getElementById('group-detail-screen');
const newGroupFormScreen = document.getElementById('new-group-form-screen');
const addExpenseFormScreen = document.getElementById('add-expense-form-screen');

const loginForm = document.getElementById('login-form');
const registerBtn = document.getElementById('register-btn');
const registerForm = document.getElementById('register-form');
const backToLoginBtn = document.getElementById('back-to-login-btn');
const groupsList = document.getElementById('groups-list');
const requestsList = document.getElementById('requests-list');
const newGroupBtn = document.getElementById('new-group-btn');
const backToMainBtn = document.getElementById('back-to-main-btn');
const backFromFormBtn = document.getElementById('back-from-form-btn');
const backFromExpenseFormBtn = document.getElementById('back-from-expense-form-btn');

const groupsNavBtn = document.getElementById('groups-btn');
const requestsNavBtn = document.getElementById('requests-btn');
const groupsSection = document.getElementById('groups-section');
const requestsSection = document.getElementById('requests-section');

const menuBtn = document.getElementById('menu-btn');
const dropdownContent = document.getElementById('dropdown-content');
const logoutBtn = document.getElementById('logout-btn');

const groupTitle = document.getElementById('group-title');
const groupContent = document.getElementById('group-content');
const addExpenseBtn = document.getElementById('add-expense-btn');

const newGroupForm = document.getElementById('new-group-form');
const addParticipantBtn = document.getElementById('add-participant-btn');
const participantsContainer = document.getElementById('participants-container');
const addExpenseInputBtn = document.getElementById('add-expense-input-btn');
const expensesContainer = document.getElementById('expenses-container');

const addExpenseForm = document.getElementById('add-expense-form');
const expensePayer = document.getElementById('expense-payer');
const expenseAmount = document.getElementById('expense-amount');
const expenseDescription = document.getElementById('expense-description');

// Language selector buttons
const langEsBtn = document.getElementById('lang-es');
const langEnBtn = document.getElementById('lang-en');
const langEsRegBtn = document.getElementById('lang-es-reg');
const langEnRegBtn = document.getElementById('lang-en-reg');
const langEsMenuBtn = document.getElementById('lang-es-menu');
const langEnMenuBtn = document.getElementById('lang-en-menu');

// --- Datos de la Aplicación (simulados) ---
let currentUser = null;
let currentLanguage = localStorage.getItem('language') || 'es'; // Default language is Spanish

// --- Utilidades para trabajar con los DTOs de Java ---
// Funciones para convertir entre objetos JavaScript y DTOs de Java

// API utilities
// Funciones para comunicarse con el backend
async function apiLogin(username, password, email) {
    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password, email }),
        });

        if (!response.ok) {
            throw new Error('Login failed');
        }

        // Check if the response is JSON before parsing
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        } else {
            // If the response is not JSON, it might be an empty success response
            return { success: true };
        }
    } catch (error) {
        console.error('Error during login:', error);
        return null;
    }
}

async function apiRegister(username, password, email, name) {
    try {
        const response = await fetch('/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password, email, name }),
        });

        if (!response.ok) {
            throw new Error('Registration failed');
        }

        return await response.json();
    } catch (error) {
        console.error('Error during registration:', error);
        return null;
    }
}

// UserDTO utilities
function createUserDTO(username, password, email, name) {
    return {
        username: username,
        password: password,
        email: email,
        name: name || username
    };
}

function userToDTO(user, username) {
    return createUserDTO(
        username,
        user.password,
        user.email,
        user.name
    );
}

function userFromDTO(userDTO) {
    return {
        password: userDTO.password,
        email: userDTO.email,
        name: userDTO.name
    };
}

// ExpenseDTO utilities
function createExpenseDTO(payer, amount, description) {
    return {
        payer: payer,
        amount: amount,
        description: description
    };
}

function expenseToDTO(expense) {
    return createExpenseDTO(
        expense.payer,
        expense.amount,
        expense.description
    );
}

function expenseFromDTO(expenseDTO) {
    return {
        payer: expenseDTO.payer,
        amount: expenseDTO.amount,
        description: expenseDTO.description
    };
}

// PaymentDTO utilities
function createPaymentDTO(payer, receiver, amount, status, timestamp, confirmedTimestamp) {
    return {
        payer: payer,
        receiver: receiver,
        amount: amount,
        status: status,
        timestamp: timestamp,
        confirmedTimestamp: confirmedTimestamp
    };
}

function paymentToDTO(payment) {
    return createPaymentDTO(
        payment.payer,
        payment.receiver,
        payment.amount,
        payment.status,
        payment.timestamp,
        payment.confirmedTimestamp
    );
}

function paymentFromDTO(paymentDTO) {
    return {
        payer: paymentDTO.payer,
        receiver: paymentDTO.receiver,
        amount: paymentDTO.amount,
        status: paymentDTO.status,
        timestamp: paymentDTO.timestamp,
        confirmedTimestamp: paymentDTO.confirmedTimestamp
    };
}

// GroupDTO utilities
function createGroupDTO(id, name, participants, admins, expenses, payments) {
    return {
        id: id,
        name: name,
        participants: participants || [],
        admins: admins || [],
        expenses: expenses || [],
        payments: payments || []
    };
}

function groupToDTO(group) {
    return createGroupDTO(
        group.id,
        group.name,
        [...group.participants],
        [...group.admins],
        group.expenses.map(expenseToDTO),
        group.payments.map(paymentToDTO)
    );
}

function groupFromDTO(groupDTO) {
    return {
        id: groupDTO.id,
        name: groupDTO.name,
        participants: [...groupDTO.participants],
        admins: [...groupDTO.admins],
        expenses: groupDTO.expenses.map(expenseFromDTO),
        payments: groupDTO.payments.map(paymentFromDTO)
    };
}

// Función para añadir un participante a un grupo (similar al método addParticipant de GroupDTO)
function addParticipantToGroup(group, username) {
    if (!group.participants.includes(username)) {
        group.participants.push(username);
        return true;
    }
    return false;
}

// Función para añadir un administrador a un grupo (similar al método addAdmin de GroupDTO)
function addAdminToGroup(group, username) {
    if (!group.admins.includes(username)) {
        // Asegurar que el usuario es también un participante
        if (!group.participants.includes(username)) {
            group.participants.push(username);
        }
        group.admins.push(username);
        return true;
    }
    return false;
}

// Función para añadir un gasto a un grupo (similar al método addExpense de GroupDTO)
function addExpenseToGroup(group, expense) {
    if (expense) {
        group.expenses.push(expense);
        return true;
    }
    return false;
}

// Función para añadir un pago a un grupo (similar al método addPayment de GroupDTO)
function addPaymentToGroup(group, payment) {
    if (payment) {
        group.payments.push(payment);
        return true;
    }
    return false;
}

// Translation dictionaries
const translations = {
    'en': {
        // Login and Register
        'app_slogan': 'The same as other applications, but done right',
        'username': 'Username',
        'password': 'Password',
        'email': 'Email',
        'login': 'Login',
        'register': 'Register',
        'back_to_login': 'Back to Login',

        // Main Menu
        'groups': 'Groups',
        'requests': 'Requests',
        'new_group': '+ New Group',
        'logout': 'Logout',
        'my_groups': 'My Groups',
        'pending_requests': 'Pending Requests',
        'invitation_to': 'Invitation to:',
        'accept': 'Accept',
        'language': 'Language:',

        // Group Details
        'back': 'Back',
        'add_expense': 'Add Expense',
        'expense_summary': 'Expense Summary',
        'paid': 'paid',
        'for': 'for',
        'balance_by_participant': 'Balance by Participant',
        'who_owes_whom': 'Who Owes Whom',
        'owes': 'owes',
        'to': 'to',
        'mark_as_paid': 'Mark as Paid',
        'confirm_payment': 'Confirm Payment',
        'payment_pending': 'Payment pending confirmation',
        'payment_confirmed': 'Payment confirmed',

        // New Group Form
        'create_new_group': 'Create New Group',
        'cancel': 'Cancel',
        'group_name': 'Group Name',
        'participants': 'Participants:',
        'add_participant': 'Add Participant',
        'participant_username': 'Participant username',
        'admin': 'Admin',
        'expenses': 'Expenses:',
        'add_expense_btn': 'Add Expense',
        'create_group': 'Create Group',

        // Add Expense Form
        'add_expense_title': 'Add Expense',
        'payer': 'Who pays (username)',
        'amount': 'Amount (€)',
        'description': 'Description',

        // Alerts
        'invalid_credentials': 'Invalid username or password.',
        'complete_all_fields': 'Please complete all fields.',
        'username_exists': 'This username is already in use. Please choose another one.',
        'email_exists': 'This email is already in use. Please use another one.',
        'registration_success': 'Registration successful! You have been automatically logged in.',
        'registration_failed': 'Registration failed. Please try again later.',
        'invitation_accepted': 'You have accepted the invitation!',
        'invalid_participants': 'One or more participant users do not exist. Please check the names.',
        'group_created': 'Group "{0}" created successfully.',
        'payer_not_exists': 'The payer user does not exist. Please check the name.',
        'payer_not_participant': 'The payer user is not a participant in the group.',
        'expense_added': 'Expense "{0}" added successfully.',
        'logout_success': 'You have successfully logged out.'
    },
    'es': {
        // Login and Register
        'app_slogan': 'Lo mismo que otras aplicaciones, pero bien',
        'username': 'Nombre de usuario',
        'password': 'Contraseña',
        'email': 'Correo electrónico',
        'login': 'Iniciar Sesión',
        'register': 'Registrarse',
        'back_to_login': 'Volver al Login',

        // Main Menu
        'groups': 'Grupos',
        'requests': 'Solicitudes',
        'new_group': '+ Nuevo grupo',
        'logout': 'Desconectarte',
        'my_groups': 'Mis Grupos',
        'pending_requests': 'Solicitudes Pendientes',
        'invitation_to': 'Invitación a:',
        'accept': 'Aceptar',
        'language': 'Idioma:',

        // Group Details
        'back': 'Volver',
        'add_expense': 'Añadir Gasto',
        'expense_summary': 'Resumen de Gastos',
        'paid': 'pagó',
        'for': 'por',
        'balance_by_participant': 'Balance por Participante',
        'who_owes_whom': 'Quién debe a quién',
        'owes': 'debe',
        'to': 'a',
        'mark_as_paid': 'Marcar como Pagado',
        'confirm_payment': 'Confirmar Pago',
        'payment_pending': 'Pago pendiente de confirmación',
        'payment_confirmed': 'Pago confirmado',

        // New Group Form
        'create_new_group': 'Crear Nuevo Grupo',
        'cancel': 'Cancelar',
        'group_name': 'Nombre del grupo',
        'participants': 'Participantes:',
        'add_participant': 'Añadir participante',
        'participant_username': 'Usuario del participante',
        'admin': 'Admin',
        'expenses': 'Gastos:',
        'add_expense_btn': 'Añadir gasto',
        'create_group': 'Crear Grupo',

        // Add Expense Form
        'add_expense_title': 'Añadir Gasto',
        'payer': 'Quien paga (usuario)',
        'amount': 'Cantidad (€)',
        'description': 'Descripción',

        // Alerts
        'invalid_credentials': 'Usuario o contraseña incorrectos.',
        'complete_all_fields': 'Por favor, completa todos los campos.',
        'username_exists': 'Este nombre de usuario ya está en uso. Por favor, elige otro.',
        'email_exists': 'Este correo electrónico ya está en uso. Por favor, utiliza otro.',
        'registration_success': '¡Registro exitoso! Has iniciado sesión automáticamente.',
        'registration_failed': 'El registro ha fallado. Por favor, inténtalo de nuevo más tarde.',
        'invitation_accepted': '¡Has aceptado la invitación!',
        'invalid_participants': 'Uno o más usuarios participantes no existen. Por favor, revisa los nombres.',
        'group_created': 'Grupo "{0}" creado con éxito.',
        'payer_not_exists': 'El usuario pagador no existe. Por favor, revisa el nombre.',
        'payer_not_participant': 'El usuario pagador no es un participante del grupo.',
        'expense_added': 'Gasto "{0}" añadido con éxito.',
        'logout_success': 'Has cerrado sesión correctamente.'
    }
};

// Function to get translation
function t(key, ...args) {
    let text = translations[currentLanguage][key] || key;

    // Replace placeholders with arguments
    if (args.length) {
        args.forEach((arg, i) => {
            text = text.replace(`{${i}}`, arg);
        });
    }

    return text;
}

// Function to switch language
function switchLanguage(lang) {
    if (lang && translations[lang]) {
        currentLanguage = lang;
        localStorage.setItem('language', lang);
        updateUILanguage();
    }
}

// Function to update all UI elements with the current language
function updateUILanguage() {
    // Update HTML elements with data-translate attribute
    document.querySelectorAll('[data-translate]').forEach(element => {
        const key = element.getAttribute('data-translate');
        if (element.tagName === 'INPUT' && element.getAttribute('placeholder')) {
            element.setAttribute('placeholder', t(key));
        } else {
            element.textContent = t(key);
        }
    });

    // Update document language
    document.documentElement.lang = currentLanguage;

    // Re-render current screen if needed
    if (currentUser) {
        renderMainMenu();
        if (currentGroup) {
            renderGroupDetail(currentGroup);
        }
    }
}

// Almacenamiento de usuarios usando UserDTO
let users = {};

// Almacenamiento de grupos usando GroupDTO
let groups = [];

// Variable para almacenar el grupo actual cuando se está añadiendo un gasto
let currentGroup = null;

// Estructura para las solicitudes de grupo
function createGroupRequest(groupId, groupName, toUser) {
    return {
        groupId: groupId,
        groupName: groupName,
        toUser: toUser
    };
}

let requests = [];

// --- Funciones para manejar la UI y la lógica ---

// Cambiar de pantalla
function showScreen(screen) {
    document.querySelectorAll('.screen').forEach(s => {
        s.classList.add('hidden');
        s.classList.remove('active');
    });
    screen.classList.remove('hidden');
    screen.classList.add('active');
}

// Llenar el menú principal con datos
function renderMainMenu() {
    groupsList.innerHTML = '';
    requestsList.innerHTML = '';

    // Renderizar Grupos
    const userGroups = groups.filter(g => g.participants.includes(currentUser.username));
    userGroups.forEach(group => {
        const li = document.createElement('li');
        li.textContent = group.name;
        li.dataset.groupId = group.id;
        li.addEventListener('click', () => {
            renderGroupDetail(group);
            showScreen(groupDetailScreen);
        });
        groupsList.appendChild(li);
    });

    // Renderizar Solicitudes
    const userRequests = requests.filter(r => r.toUser === currentUser.username);
    userRequests.forEach(req => {
        const li = document.createElement('li');
        li.textContent = `${t('invitation_to')} ${req.groupName}`;
        const acceptBtn = document.createElement('button');
        acceptBtn.textContent = t('accept');
        acceptBtn.onclick = () => {
            const groupToJoin = groups.find(g => g.id === req.groupId);
            if (groupToJoin) {
                groupToJoin.participants.push(currentUser.username);
                // Eliminar la solicitud
                requests = requests.filter(r => r !== req);
                renderMainMenu(); // Actualizar la vista
                alert(t('invitation_accepted'));
            }
        };
        li.appendChild(acceptBtn);
        requestsList.appendChild(li);
    });
}

// Calcular y mostrar los saldos del grupo
function renderGroupDetail(group) {
    // Guardar el grupo actual para usarlo al añadir gastos
    currentGroup = group;

    groupTitle.textContent = group.name;
    const isUserAdmin = group.admins.includes(currentUser.username);

    // Mostrar u ocultar el botón de añadir gasto
    addExpenseBtn.classList.toggle('hidden', !isUserAdmin);

    // Calcular saldos
    const balances = {};
    group.participants.forEach(p => balances[p] = 0);

    group.expenses.forEach(expense => {
        const amountPerPerson = expense.amount / group.participants.length;
        group.participants.forEach(p => {
            if (p === expense.payer) {
                balances[p] += expense.amount - amountPerPerson;
            } else {
                balances[p] -= amountPerPerson;
            }
        });
    });

    // Crear la vista de saldos
    groupContent.innerHTML = `<h3>${t('expense_summary')}</h3>`;

    // Mostrar cada gasto individual
    group.expenses.forEach(expense => {
        groupContent.innerHTML += `<p><strong>${expense.payer}</strong> ${t('paid')} ${expense.amount}€ ${t('for')} "${expense.description}"</p>`;
    });

    // Mostrar el balance de cada participante
    groupContent.innerHTML += `<h3>${t('balance_by_participant')}</h3>`;
    Object.entries(balances).forEach(([participant, balance]) => {
        const sign = balance > 0 ? '+' : (balance < 0 ? '-' : '');
        groupContent.innerHTML += `<p><strong>${participant}</strong>: ${sign}${Math.abs(balance).toFixed(2)}€</p>`;
    });

    groupContent.innerHTML += `<h3>${t('who_owes_whom')}</h3>`;

    // Lógica para simplificar los saldos (opcional y más compleja)
    const sortedBalances = Object.entries(balances).sort(([, a], [, b]) => a - b);
    let i = 0, j = sortedBalances.length - 1;

    while (i < j) {
        const [debtor, debt] = sortedBalances[i];
        const [creditor, credit] = sortedBalances[j];
        let amount;

        // Create a unique ID for this debt
        const debtId = `debt-${debtor}-${creditor}`;

        // Check if there's a pending or confirmed payment for this debt
        const pendingPayment = group.payments.find(p =>
            p.payer === debtor &&
            p.receiver === creditor &&
            p.status === 'pending');

        const confirmedPayment = group.payments.find(p =>
            p.payer === debtor &&
            p.receiver === creditor &&
            p.status === 'confirmed');

        // Create the debt display with appropriate buttons
        let debtHtml = `<div class="debt-item" id="${debtId}">`;

        if (Math.abs(debt) > credit) {
            amount = credit;
            debtHtml += `<p><strong>${debtor}</strong> ${t('owes')} ${credit.toFixed(2)}€ ${t('to')} <strong>${creditor}</strong>`;

            // Add payment status or buttons
            if (confirmedPayment) {
                debtHtml += ` <span class="payment-confirmed">✓ ${t('payment_confirmed')}</span>`;
            } else if (pendingPayment) {
                debtHtml += ` <span class="payment-pending">⌛ ${t('payment_pending')}</span>`;

                // Add confirm button for the creditor
                if (currentUser.username === creditor) {
                    debtHtml += `<button class="confirm-payment-btn" data-payer="${debtor}" data-receiver="${creditor}" data-amount="${credit.toFixed(2)}">${t('confirm_payment')}</button>`;
                }
            } else {
                // Add mark as paid button for the debtor
                if (currentUser.username === debtor) {
                    debtHtml += `<button class="mark-paid-btn" data-payer="${debtor}" data-receiver="${creditor}" data-amount="${credit.toFixed(2)}">${t('mark_as_paid')}</button>`;
                }
            }

            debtHtml += `</p></div>`;
            groupContent.innerHTML += debtHtml;
            sortedBalances[i][1] += credit;
            sortedBalances[j][1] = 0;
            j--;
        } else if (Math.abs(debt) < credit) {
            amount = Math.abs(debt);
            debtHtml += `<p><strong>${debtor}</strong> ${t('owes')} ${Math.abs(debt).toFixed(2)}€ ${t('to')} <strong>${creditor}</strong>`;

            // Add payment status or buttons
            if (confirmedPayment) {
                debtHtml += ` <span class="payment-confirmed">✓ ${t('payment_confirmed')}</span>`;
            } else if (pendingPayment) {
                debtHtml += ` <span class="payment-pending">⌛ ${t('payment_pending')}</span>`;

                // Add confirm button for the creditor
                if (currentUser.username === creditor) {
                    debtHtml += `<button class="confirm-payment-btn" data-payer="${debtor}" data-receiver="${creditor}" data-amount="${Math.abs(debt).toFixed(2)}">${t('confirm_payment')}</button>`;
                }
            } else {
                // Add mark as paid button for the debtor
                if (currentUser.username === debtor) {
                    debtHtml += `<button class="mark-paid-btn" data-payer="${debtor}" data-receiver="${creditor}" data-amount="${Math.abs(debt).toFixed(2)}">${t('mark_as_paid')}</button>`;
                }
            }

            debtHtml += `</p></div>`;
            groupContent.innerHTML += debtHtml;
            sortedBalances[j][1] += debt;
            sortedBalances[i][1] = 0;
            i++;
        } else {
            amount = credit;
            debtHtml += `<p><strong>${debtor}</strong> ${t('owes')} ${credit.toFixed(2)}€ ${t('to')} <strong>${creditor}</strong>`;

            // Add payment status or buttons
            if (confirmedPayment) {
                debtHtml += ` <span class="payment-confirmed">✓ ${t('payment_confirmed')}</span>`;
            } else if (pendingPayment) {
                debtHtml += ` <span class="payment-pending">⌛ ${t('payment_pending')}</span>`;

                // Add confirm button for the creditor
                if (currentUser.username === creditor) {
                    debtHtml += `<button class="confirm-payment-btn" data-payer="${debtor}" data-receiver="${creditor}" data-amount="${credit.toFixed(2)}">${t('confirm_payment')}</button>`;
                }
            } else {
                // Add mark as paid button for the debtor
                if (currentUser.username === debtor) {
                    debtHtml += `<button class="mark-paid-btn" data-payer="${debtor}" data-receiver="${creditor}" data-amount="${credit.toFixed(2)}">${t('mark_as_paid')}</button>`;
                }
            }

            debtHtml += `</p></div>`;
            groupContent.innerHTML += debtHtml;
            sortedBalances[i][1] = 0;
            sortedBalances[j][1] = 0;
            i++;
            j--;
        }
    }

    // Add event listeners for payment buttons
    document.querySelectorAll('.mark-paid-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const payer = this.getAttribute('data-payer');
            const receiver = this.getAttribute('data-receiver');
            const amount = parseFloat(this.getAttribute('data-amount'));

            // Add a new pending payment using PaymentDTO
            const newPayment = createPaymentDTO(
                payer,
                receiver,
                amount,
                'pending',
                new Date().toISOString()
            );

            // Usar la función addPaymentToGroup para añadir el pago
            addPaymentToGroup(currentGroup, newPayment);

            // Re-render the group detail to update the UI
            renderGroupDetail(currentGroup);
        });
    });

    document.querySelectorAll('.confirm-payment-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const payer = this.getAttribute('data-payer');
            const receiver = this.getAttribute('data-receiver');
            const amount = parseFloat(this.getAttribute('data-amount'));

            // Find the pending payment
            const paymentIndex = currentGroup.payments.findIndex(p =>
                p.payer === payer &&
                p.receiver === receiver &&
                p.status === 'pending');

            if (paymentIndex !== -1) {
                // Update the payment status to confirmed
                currentGroup.payments[paymentIndex].status = 'confirmed';
                currentGroup.payments[paymentIndex].confirmedTimestamp = new Date().toISOString();

                // Re-render the group detail to update the UI
                renderGroupDetail(currentGroup);
            }
        });
    });
}

// --- Event Listeners ---

// Manejar el login
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;

    try {
        // Intentar autenticar con la base de datos
        const user = await apiLogin(username, password, email);

        if (user && user.success) {
            // Login exitoso
            currentUser = { username, name: user.name };
            showScreen(mainScreen);
            renderMainMenu();
        } else {
            // Si la API falla, intentar con los usuarios locales (para compatibilidad)
            if (users[username] && users[username].password === password) {
                currentUser = { username, name: users[username].name };
                showScreen(mainScreen);
                renderMainMenu();
            } else {
                alert(t('invalid_credentials'));
            }
        }
    } catch (error) {
        console.error('Error during login:', error);
        alert(t('invalid_credentials'));
    }
});

// Cambiar entre Grupos y Solicitudes
groupsNavBtn.addEventListener('click', () => {
    groupsSection.classList.remove('hidden');
    requestsSection.classList.add('hidden');
    groupsNavBtn.classList.add('active');
    requestsNavBtn.classList.remove('active');
});

requestsNavBtn.addEventListener('click', () => {
    requestsSection.classList.remove('hidden');
    groupsSection.classList.add('hidden');
    requestsNavBtn.classList.add('active');
    groupsNavBtn.classList.remove('active');
});

// Botón para volver al menú principal
backToMainBtn.addEventListener('click', () => {
    showScreen(mainScreen);
    renderMainMenu();
});

// Botón para mostrar el formulario de nuevo grupo
newGroupBtn.addEventListener('click', () => {
    showScreen(newGroupFormScreen);
    // Limpiar el formulario
    newGroupForm.reset();
    participantsContainer.innerHTML = `<p data-translate="participants">${t('participants')}</p>`;
    expensesContainer.innerHTML = `<p data-translate="expenses">${t('expenses')}</p>`;
});

// Botón para volver del formulario de nuevo grupo
backFromFormBtn.addEventListener('click', () => {
    showScreen(mainScreen);
});

// Lógica para añadir participantes dinámicamente
addParticipantBtn.addEventListener('click', () => {
    const participantCount = participantsContainer.querySelectorAll('.participant-item').length;
    const div = document.createElement('div');
    div.classList.add('participant-item');
    div.innerHTML = `<input type="text" placeholder="${t('participant_username')}" required>
                     <label><input type="checkbox" class="admin-checkbox"> ${t('admin')}</label>`;
    participantsContainer.appendChild(div);
});

// Lógica para añadir gastos dinámicamente
addExpenseInputBtn.addEventListener('click', () => {
    const div = document.createElement('div');
    div.classList.add('expense-item');
    // Se podría hacer un dropdown de usuarios aquí, por ahora solo un input
    div.innerHTML = `<input type="text" placeholder="${t('payer')}" required>
                     <input type="number" placeholder="${t('amount')}" required step="0.01">
                     <input type="text" placeholder="${t('description')}">`;
    expensesContainer.appendChild(div);
});

// Lógica del formulario de nuevo grupo
newGroupForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const groupName = document.getElementById('group-name').value;
    const participants = Array.from(participantsContainer.querySelectorAll('.participant-item input[type="text"]'))
        .map(input => input.value);
    const admins = Array.from(participantsContainer.querySelectorAll('.participant-item input[type="text"]'))
        .filter((input, index) => participantsContainer.querySelectorAll('.admin-checkbox')[index].checked)
        .map(input => input.value);
    const expenses = Array.from(expensesContainer.querySelectorAll('.expense-item')).map(item =>
        createExpenseDTO(
            item.querySelector('input:nth-of-type(1)').value,
            parseFloat(item.querySelector('input:nth-of-type(2)').value),
            item.querySelector('input:nth-of-type(3)').value
        )
    );

    // Añadir al usuario actual como admin por defecto
    if (!admins.includes(currentUser.username)) {
        admins.push(currentUser.username);
    }

    // Validar que los participantes existen (aquí se haría una llamada a la base de datos)
    const validParticipants = participants.every(p => users[p] !== undefined);
    if (!validParticipants) {
        alert(t('invalid_participants'));
        return;
    }

    // Crear un nuevo grupo usando GroupDTO
    const uniqueParticipants = [...new Set([...participants, currentUser.username])]; // Asegurar que el creador está en la lista
    const newGroup = createGroupDTO(
        `g${groups.length + 1}`,
        groupName,
        uniqueParticipants,
        admins,
        expenses,
        []
    );

    groups.push(newGroup);

    // Aquí podrías añadir una lógica para enviar solicitudes a los otros participantes

    alert(t('group_created', groupName));
    showScreen(mainScreen);
    renderMainMenu();
});

// Mostrar pantalla de registro
registerBtn.addEventListener('click', () => {
    showScreen(registerScreen);
});

// Volver a la pantalla de login
backToLoginBtn.addEventListener('click', () => {
    showScreen(loginScreen);
});

// Manejar el registro de usuarios
registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('reg-username').value;
    const password = document.getElementById('reg-password').value;
    const email = document.getElementById('reg-email').value;
    const name = username; // Usar el username como nombre por defecto

    // Validar que todos los campos estén completos
    if (!username || !password || !email) {
        alert(t('complete_all_fields'));
        return;
    }

    try {
        const response = await fetch('/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password, email, name }),
        });

        if (response.ok) {
            const user = await response.json();
            // Registro exitoso
            // Iniciar sesión automáticamente con el nuevo usuario
            currentUser = { username, name: user.name };
            showScreen(mainScreen);
            renderMainMenu();

            alert(t('registration_success'));
        } else {
            const errorMessage = await response.text();
            if (errorMessage === 'Username already exists') {
                alert(t('username_exists'));
            } else if (errorMessage === 'Email already exists') {
                alert(t('email_exists'));
            } else {
                alert(t('registration_failed'));
            }
        }
    } catch (error) {
        console.error('Error during registration:', error);
        alert(t('registration_failed'));
    }
});

// Manejar el menú desplegable
menuBtn.addEventListener('click', () => {
    dropdownContent.classList.toggle('hidden');
});

// Cerrar el menú desplegable al hacer clic fuera de él
document.addEventListener('click', (e) => {
    if (!menuBtn.contains(e.target) && !dropdownContent.contains(e.target)) {
        dropdownContent.classList.add('hidden');
    }
});

// Manejar el cierre de sesión
logoutBtn.addEventListener('click', () => {
    currentUser = null;
    showScreen(loginScreen);
    dropdownContent.classList.add('hidden');
    // Limpiar los campos del formulario de login
    document.getElementById('username').value = '';
    document.getElementById('password').value = '';
    document.getElementById('email').value = '';
    alert(t('logout_success'));
});

// Botón para mostrar el formulario de añadir gasto
addExpenseBtn.addEventListener('click', () => {
    // Establecer el valor por defecto del pagador como el usuario actual
    expensePayer.value = currentUser.username;
    expenseAmount.value = '';
    expenseDescription.value = '';

    showScreen(addExpenseFormScreen);
});

// Botón para volver del formulario de añadir gasto
backFromExpenseFormBtn.addEventListener('click', () => {
    showScreen(groupDetailScreen);
});

// Lógica del formulario de añadir gasto
addExpenseForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const payer = expensePayer.value;
    const amount = parseFloat(expenseAmount.value);
    const description = expenseDescription.value;

    // Validar que el pagador existe y es un participante del grupo
    if (!users[payer]) {
        alert(t('payer_not_exists'));
        return;
    }

    if (!currentGroup.participants.includes(payer)) {
        alert(t('payer_not_participant'));
        return;
    }

    // Añadir el gasto al grupo actual usando ExpenseDTO
    const newExpense = createExpenseDTO(payer, amount, description);

    // Usar la función addExpenseToGroup para añadir el gasto
    addExpenseToGroup(currentGroup, newExpense);

    // Actualizar la vista del grupo
    renderGroupDetail(currentGroup);
    showScreen(groupDetailScreen);

    alert(t('expense_added', description));
});

// --- Language Selector Event Listeners ---

// Helper function to update active language button
function updateLanguageButtons(lang) {
    // Remove active class from all buttons
    [langEsBtn, langEnBtn, langEsRegBtn, langEnRegBtn, langEsMenuBtn, langEnMenuBtn].forEach(btn => {
        if (btn) btn.classList.remove('active');
    });

    // Add active class to the buttons for the current language
    if (lang === 'es') {
        if (langEsBtn) langEsBtn.classList.add('active');
        if (langEsRegBtn) langEsRegBtn.classList.add('active');
        if (langEsMenuBtn) langEsMenuBtn.classList.add('active');
    } else if (lang === 'en') {
        if (langEnBtn) langEnBtn.classList.add('active');
        if (langEnRegBtn) langEnRegBtn.classList.add('active');
        if (langEnMenuBtn) langEnMenuBtn.classList.add('active');
    }
}

// Login screen language buttons
if (langEsBtn) {
    langEsBtn.addEventListener('click', () => {
        switchLanguage('es');
        updateLanguageButtons('es');
    });
}

if (langEnBtn) {
    langEnBtn.addEventListener('click', () => {
        switchLanguage('en');
        updateLanguageButtons('en');
    });
}

// Register screen language buttons
if (langEsRegBtn) {
    langEsRegBtn.addEventListener('click', () => {
        switchLanguage('es');
        updateLanguageButtons('es');
    });
}

if (langEnRegBtn) {
    langEnRegBtn.addEventListener('click', () => {
        switchLanguage('en');
        updateLanguageButtons('en');
    });
}

// Menu language buttons
if (langEsMenuBtn) {
    langEsMenuBtn.addEventListener('click', () => {
        switchLanguage('es');
        updateLanguageButtons('es');
        dropdownContent.classList.add('hidden'); // Hide dropdown after selection
    });
}

if (langEnMenuBtn) {
    langEnMenuBtn.addEventListener('click', () => {
        switchLanguage('en');
        updateLanguageButtons('en');
        dropdownContent.classList.add('hidden'); // Hide dropdown after selection
    });
}

// Initialize UI with the correct language
document.addEventListener('DOMContentLoaded', () => {
    updateUILanguage();
    updateLanguageButtons(currentLanguage);
});
