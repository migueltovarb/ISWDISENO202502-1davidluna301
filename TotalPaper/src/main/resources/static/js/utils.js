// utils.js
class Utils {
    static showLoading() {
        // Implementar loading spinner si es necesario
        console.log('Cargando...');
    }

    static hideLoading() {
        // Ocultar loading spinner
        console.log('Carga completada');
    }

    static showAlert(message, type = 'info', container = null) {
        const alert = document.createElement('div');
        alert.className = `alert alert-${type}`;
        alert.textContent = message;
        
        if (container) {
            container.insertBefore(alert, container.firstChild);
        } else {
            document.body.insertBefore(alert, document.body.firstChild);
        }
        
        setTimeout(() => {
            alert.remove();
        }, 5000);
    }

    static formatCurrency(amount) {
        return new Intl.NumberFormat('es-PE', {
            style: 'currency',
            currency: 'PEN'
        }).format(amount);
    }

    static formatDate(date) {
        return new Date(date).toLocaleDateString('es-PE', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    static getAuthToken() {
        return localStorage.getItem('authToken');
    }

    static getCurrentUser() {
        const user = localStorage.getItem('currentUser');
        return user ? JSON.parse(user) : null;
    }

    static setCurrentUser(user) {
        localStorage.setItem('currentUser', JSON.stringify(user));
    }

    static clearAuth() {
        localStorage.removeItem('currentUser');
        localStorage.removeItem('authToken');
    }

    static async makeRequest(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
            }
        };

        const user = this.getCurrentUser();
        if (user && user.codigo) {
            defaultOptions.headers['X-Vendedor-Codigo'] = user.codigo;
        }

        const finalOptions = { ...defaultOptions, ...options };
        
        try {
            const response = await fetch(url, finalOptions);
            
            if (!response.ok) {
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error en la petición:', error);
            throw error;
        }
    }

    static confirmAction(message) {
        return new Promise((resolve) => {
            // Implementación simple de confirmación
            const confirmed = confirm(message);
            resolve(confirmed);
        });
    }
}