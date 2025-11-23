document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
    checkExistingAuth();
});

async function handleLogin(e) {
    e.preventDefault();
    
    const codigo = document.getElementById('codigo').value.trim();
    const password = document.getElementById('password').value;
    
    if (!codigo || !password) {
        Utils.showAlert('Por favor, complete todos los campos', 'error');
        return;
    }

    try {
        const response = await Utils.makeRequest('/api/gestion/login', {
            method: 'POST',
            body: JSON.stringify({ codigo, password })
        });

        Utils.setCurrentUser(response);
        Utils.showAlert(`Bienvenido ${response.nombre}`, 'success');
        
        setTimeout(() => {
            if (response.rol === 'VENDEDOR') {
                window.location.href = '/vendedor';
            } else if (response.rol === 'ADMINISTRADOR') {
                window.location.href = '/admin';
            }
        }, 1000);
        
    } catch (error) {
        Utils.showAlert('Credenciales incorrectas', 'error');
    }
}

function checkExistingAuth() {
    const user = Utils.getCurrentUser();
    const currentPath = window.location.pathname;
    
    if (user) {
        if (currentPath === '/login' || currentPath === '/') {
            if (user.rol === 'VENDEDOR') {
                window.location.href = '/vendedor';
            } else if (user.rol === 'ADMINISTRADOR') {
                window.location.href = '/admin';
            }
        }
    } else {
        if (currentPath !== '/login' && currentPath !== '/') {
            window.location.href = '/login';
        }
    }
}

function logout() {
    if (confirm('¿Está seguro de que desea cerrar sesión?')) {
        Utils.clearAuth();
        window.location.href = '/login';
    }
}