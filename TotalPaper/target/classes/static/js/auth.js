// auth.js - Actualizado
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    checkExistingAuth();
});

async function handleLogin(e) {
    e.preventDefault();
    
    const codigo = document.getElementById('codigo').value;
    const password = document.getElementById('password').value;
    
    try {
        Utils.showLoading();
        
        const response = await Utils.makeRequest('/api/gestion/login', {
            method: 'POST',
            body: JSON.stringify({ codigo, password })
        });

        Utils.setCurrentUser(response);
        
        // Redirigir según el rol
        if (response.rol === 'VENDEDOR') {
            window.location.href = '/vendedor';
        } else if (response.rol === 'ADMINISTRADOR') {
            window.location.href = '/admin';
        } else {
            throw new Error('Rol no reconocido: ' + response.rol);
        }
        
    } catch (error) {
        Utils.showAlert('Error: ' + error.message, 'error');
        console.error('Error en login:', error);
    } finally {
        Utils.hideLoading();
    }
}

function checkExistingAuth() {
    const user = Utils.getCurrentUser();
    const currentPath = window.location.pathname;
    
    if (user) {
        // Si ya está autenticado y está en login, redirigir
        if (currentPath === '/login' || currentPath === '/') {
            if (user.rol === 'VENDEDOR') {
                window.location.href = '/vendedor';
            } else if (user.rol === 'ADMINISTRADOR') {
                window.location.href = '/admin';
            }
        }
    } else {
        // Si no está autenticado y no está en login, redirigir a login
        if (currentPath !== '/login' && currentPath !== '/') {
            window.location.href = '/login';
        }
    }
}

function logout() {
    if (confirm('¿Estás seguro de que deseas cerrar sesión?')) {
        Utils.clearAuth();
        window.location.href = '/login';
    }
}