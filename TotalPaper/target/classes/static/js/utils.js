class Utils {
    static showAlert(message, type = 'info') {
        const alert = document.createElement('div');
        alert.className = `alert alert-${type}`;
        alert.innerHTML = `
            <div class="alert-content">
                <span>${message}</span>
                <button class="alert-close" onclick="this.parentElement.parentElement.remove()">×</button>
            </div>
        `;
        
        document.body.insertBefore(alert, document.body.firstChild);
        
        setTimeout(() => {
            if (alert.parentElement) {
                alert.remove();
            }
        }, 5000);
    }

    static formatCurrency(amount) {
        return new Intl.NumberFormat('es-PE', {
            style: 'currency',
            currency: 'PEN'
        }).format(amount);
    }

    static formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('es-PE', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
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
                const errorText = await response.text();
                throw new Error(errorText || `Error ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error en la petición:', error);
            throw error;
        }
    }

    static confirmAction(message) {
        return new Promise((resolve) => {
            const confirmed = confirm(message);
            resolve(confirmed);
        });
    }
}