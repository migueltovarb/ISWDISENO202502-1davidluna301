// admin.js
class AdminApp {
    constructor() {
        this.currentUser = Utils.getCurrentUser();
        this.usuarios = [];
        this.productos = [];
        this.pedidos = [];
        this.init();
    }

    async init() {
        if (!this.currentUser || this.currentUser.rol !== 'ADMINISTRADOR') {
            window.location.href = '/login';
            return;
        }

        this.setupEventListeners();
        await this.cargarDatosIniciales();
        this.actualizarUI();
    }

    setupEventListeners() {
        // Navegación entre secciones
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                this.mostrarSeccion(link.dataset.section);
            });
        });

        // Formularios
        document.getElementById('formCrearUsuario').addEventListener('submit', (e) => {
            e.preventDefault();
            this.crearUsuario();
        });

        document.getElementById('formCrearProducto').addEventListener('submit', (e) => {
            e.preventDefault();
            this.crearProducto();
        });

        // Exportación
        document.getElementById('btnExportar').addEventListener('click', () => {
            this.exportarPedidos();
        });
    }

    async cargarDatosIniciales() {
        try {
            Utils.showLoading();
            
            await Promise.all([
                this.cargarUsuarios(),
                this.cargarProductos(),
                this.cargarPedidos()
            ]);

        } catch (error) {
            Utils.showAlert('Error al cargar los datos', 'error');
            console.error('Error:', error);
        } finally {
            Utils.hideLoading();
        }
    }

    async cargarUsuarios() {
        // En una implementación real, aquí harías una petición para obtener los usuarios
        // Por ahora, simulamos la carga
        this.usuarios = [];
        this.mostrarUsuarios();
    }

    async cargarProductos() {
        try {
            this.productos = await Utils.makeRequest('/api/consulta/productos');
            this.mostrarProductos();
        } catch (error) {
            console.error('Error cargando productos:', error);
        }
    }

    async cargarPedidos() {
        try {
            this.pedidos = await Utils.makeRequest('/api/venta/pedidos');
            this.mostrarPedidos();
        } catch (error) {
            console.error('Error cargando pedidos:', error);
        }
    }

    mostrarSeccion(sectionId) {
        // Ocultar todas las secciones
        document.querySelectorAll('.section').forEach(section => {
            section.classList.add('hidden');
        });

        // Mostrar la sección seleccionada
        document.getElementById(sectionId).classList.remove('hidden');

        // Actualizar navegación activa
        document.querySelectorAll('.nav-link').forEach(link => {
            link.classList.remove('active');
        });
        document.querySelector(`[data-section="${sectionId}"]`).classList.add('active');
    }

    // Gestión de Usuarios
    async crearUsuario() {
        const formData = new FormData(document.getElementById('formCrearUsuario'));
        const usuarioDTO = {
            codigo: formData.get('codigo'),
            nombre: formData.get('nombre'),
            password: formData.get('password'),
            rol: 'VENDEDOR'
        };

        try {
            Utils.showLoading();
            
            const usuarioCreado = await Utils.makeRequest('/api/gestion/usuario', {
                method: 'POST',
                body: JSON.stringify(usuarioDTO)
            });

            Utils.showAlert('Usuario creado exitosamente', 'success');
            document.getElementById('formCrearUsuario').reset();
            
            // Recargar lista de usuarios
            await this.cargarUsuarios();

        } catch (error) {
            Utils.showAlert('Error al crear usuario: ' + error.message, 'error');
        } finally {
            Utils.hideLoading();
        }
    }

    async eliminarUsuario(codigo) {
        const confirmed = await Utils.confirmAction(
            `¿Está seguro de eliminar al usuario ${codigo}? Esta acción no se puede deshacer.`
        );

        if (!confirmed) return;

        try {
            await Utils.makeRequest(`/api/gestion/usuario/${codigo}`, {
                method: 'DELETE'
            });

            Utils.showAlert('Usuario eliminado exitosamente', 'success');
            await this.cargarUsuarios();

        } catch (error) {
            Utils.showAlert('Error al eliminar usuario: ' + error.message, 'error');
        }
    }

    mostrarUsuarios() {
        const container = document.getElementById('usuariosList');
        container.innerHTML = '';

        this.usuarios.forEach(usuario => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${usuario.codigo}</td>
                <td>${usuario.nombre}</td>
                <td>${usuario.rol}</td>
                <td>
                    <button class="btn btn-danger btn-sm" 
                            onclick="adminApp.eliminarUsuario('${usuario.codigo}')"
                            ${usuario.codigo === 'ADMIN001' ? 'disabled' : ''}>
                        Eliminar
                    </button>
                </td>
            `;
            container.appendChild(row);
        });
    }

    // Gestión de Productos
    async crearProducto() {
        const formData = new FormData(document.getElementById('formCrearProducto'));
        const productoDTO = {
            codigo: formData.get('codigo'),
            detalle: formData.get('detalle'),
            precio: parseFloat(formData.get('precio'))
        };

        try {
            Utils.showLoading();
            
            const productoCreado = await Utils.makeRequest('/api/gestion/producto', {
                method: 'POST',
                body: JSON.stringify(productoDTO)
            });

            Utils.showAlert('Producto creado exitosamente', 'success');
            document.getElementById('formCrearProducto').reset();
            
            await this.cargarProductos();

        } catch (error) {
            Utils.showAlert('Error al crear producto: ' + error.message, 'error');
        } finally {
            Utils.hideLoading();
        }
    }

    async eliminarProducto(codigo) {
        const confirmed = await Utils.confirmAction(
            `¿Está seguro de eliminar el producto ${codigo}?`
        );

        if (!confirmed) return;

        try {
            await Utils.makeRequest(`/api/gestion/producto/${codigo}`, {
                method: 'DELETE'
            });

            Utils.showAlert('Producto eliminado exitosamente', 'success');
            await this.cargarProductos();

        } catch (error) {
            Utils.showAlert('Error al eliminar producto: ' + error.message, 'error');
        }
    }

    mostrarProductos() {
        const container = document.getElementById('productosList');
        container.innerHTML = '';

        this.productos.forEach(producto => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${producto.codigo}</td>
                <td>${producto.detalle}</td>
                <td>${Utils.formatCurrency(producto.precio)}</td>
                <td>${producto.activo ? 'Activo' : 'Inactivo'}</td>
                <td>
                    <button class="btn btn-danger btn-sm" 
                            onclick="adminApp.eliminarProducto('${producto.codigo}')">
                        Eliminar
                    </button>
                </td>
            `;
            container.appendChild(row);
        });
    }

    // Gestión de Pedidos
    mostrarPedidos() {
        const container = document.getElementById('pedidosList');
        container.innerHTML = '';

        this.pedidos.forEach(pedido => {
            const total = pedido.productos.reduce((sum, item) => 
                sum + (item.precio * item.cantidad), 0
            );

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${pedido.id}</td>
                <td>${pedido.clienteNombre}</td>
                <td>${pedido.vendedorCodigo}</td>
                <td>${Utils.formatDate(pedido.fecha)}</td>
                <td>${pedido.estado}</td>
                <td>${Utils.formatCurrency(total)}</td>
                <td>${pedido.productos.length} productos</td>
            `;
            container.appendChild(row);
        });
    }

    async exportarPedidos() {
        try {
            Utils.showLoading();
            
            const response = await fetch('/api/venta/exportar/xlsx');
            
            if (!response.ok) {
                throw new Error('Error al exportar pedidos');
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'pedidos.xlsx';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);

            Utils.showAlert('Pedidos exportados exitosamente', 'success');

        } catch (error) {
            Utils.showAlert('Error al exportar pedidos: ' + error.message, 'error');
        } finally {
            Utils.hideLoading();
        }
    }

    actualizarUI() {
        document.getElementById('adminName').textContent = this.currentUser.nombre;
        document.getElementById('adminCode').textContent = this.currentUser.codigo;
        
        // Mostrar la sección de pedidos por defecto
        this.mostrarSeccion('gestion-pedidos');
    }
}

// Instancia global para acceso desde HTML
const adminApp = new AdminApp();