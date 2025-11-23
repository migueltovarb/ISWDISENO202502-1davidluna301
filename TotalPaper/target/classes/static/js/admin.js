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
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                this.mostrarSeccion(link.dataset.section);
            });
        });

        document.getElementById('formCrearUsuario').addEventListener('submit', (e) => {
            e.preventDefault();
            this.crearUsuario();
        });

        document.getElementById('formCrearProducto').addEventListener('submit', (e) => {
            e.preventDefault();
            this.crearProducto();
        });

        document.getElementById('btnExportar').addEventListener('click', () => {
            this.exportarPedidos();
        });
    }

    async cargarDatosIniciales() {
        try {
            await Promise.all([
                this.cargarUsuarios(),
                this.cargarProductos(),
                this.cargarPedidos()
            ]);
            Utils.showAlert('Datos cargados correctamente', 'success');
        } catch (error) {
            Utils.showAlert('Error al cargar los datos: ' + error.message, 'error');
        }
    }

    async cargarUsuarios() {
        try {
            this.usuarios = await Utils.makeRequest('/api/gestion/usuarios');
            this.mostrarUsuarios();
        } catch (error) {
            console.error('Error cargando usuarios:', error);
            throw error;
        }
    }

    async cargarProductos() {
        try {
            this.productos = await Utils.makeRequest('/api/gestion/productos');
            this.mostrarProductos();
        } catch (error) {
            console.error('Error cargando productos:', error);
            throw error;
        }
    }

    async cargarPedidos() {
        try {
            this.pedidos = await Utils.makeRequest('/api/venta/pedidos');
            this.mostrarPedidos();
        } catch (error) {
            console.error('Error cargando pedidos:', error);
            throw error;
        }
    }

    mostrarSeccion(sectionId) {
        document.querySelectorAll('.section').forEach(section => {
            section.classList.add('hidden');
        });
        document.getElementById(sectionId).classList.remove('hidden');

        document.querySelectorAll('.nav-link').forEach(link => {
            link.classList.remove('active');
        });
        document.querySelector(`[data-section="${sectionId}"]`).classList.add('active');
    }

    async crearUsuario() {
        const formData = new FormData(document.getElementById('formCrearUsuario'));
        const usuarioDTO = {
            codigo: formData.get('codigo'),
            nombre: formData.get('nombre'),
            password: formData.get('password'),
            rol: 'VENDEDOR'
        };

        if (!usuarioDTO.codigo || !usuarioDTO.nombre || !usuarioDTO.password) {
            Utils.showAlert('Por favor, complete todos los campos', 'error');
            return;
        }

        try {
            await Utils.makeRequest('/api/gestion/usuario', {
                method: 'POST',
                body: JSON.stringify(usuarioDTO)
            });

            Utils.showAlert('✅ Usuario creado exitosamente', 'success');
            document.getElementById('formCrearUsuario').reset();
            await this.cargarUsuarios();

        } catch (error) {
            Utils.showAlert('❌ Error al crear usuario: ' + error.message, 'error');
        }
    }

    async eliminarUsuario(codigo) {
        if (codigo === 'ADMIN001') {
            Utils.showAlert('No se puede eliminar el administrador principal', 'error');
            return;
        }

        const confirmed = await Utils.confirmAction(`¿Está seguro de eliminar al usuario ${codigo}?`);
        if (!confirmed) return;

        try {
            await Utils.makeRequest(`/api/gestion/usuario/${codigo}`, {
                method: 'DELETE'
            });

            Utils.showAlert('✅ Usuario eliminado exitosamente', 'success');
            await this.cargarUsuarios();

        } catch (error) {
            Utils.showAlert('❌ Error al eliminar usuario: ' + error.message, 'error');
        }
    }

    mostrarUsuarios() {
        const container = document.getElementById('usuariosList');
        
        if (this.usuarios.length === 0) {
            container.innerHTML = '<tr><td colspan="4" class="text-center">No hay usuarios registrados</td></tr>';
            return;
        }

        container.innerHTML = this.usuarios.map(usuario => `
            <tr>
                <td>${usuario.codigo}</td>
                <td>${usuario.nombre}</td>
                <td>${usuario.rol}</td>
                <td>
                    <button class="btn btn-danger btn-sm" 
                            onclick="adminApp.eliminarUsuario('${usuario.codigo}')"
                            ${usuario.codigo === 'ADMIN001' ? 'disabled' : ''}>
                        🗑️ Eliminar
                    </button>
                </td>
            </tr>
        `).join('');
    }

    async crearProducto() {
        const formData = new FormData(document.getElementById('formCrearProducto'));
        const productoDTO = {
            codigo: formData.get('codigo'),
            detalle: formData.get('detalle'),
            precio: parseFloat(formData.get('precio'))
        };

        if (!productoDTO.codigo || !productoDTO.detalle || !productoDTO.precio) {
            Utils.showAlert('Por favor, complete todos los campos', 'error');
            return;
        }

        if (productoDTO.precio <= 0) {
            Utils.showAlert('El precio debe ser mayor a 0', 'error');
            return;
        }

        try {
            await Utils.makeRequest('/api/gestion/producto', {
                method: 'POST',
                body: JSON.stringify(productoDTO)
            });

            Utils.showAlert('✅ Producto creado exitosamente', 'success');
            document.getElementById('formCrearProducto').reset();
            await this.cargarProductos();

        } catch (error) {
            Utils.showAlert('❌ Error al crear producto: ' + error.message, 'error');
        }
    }

    async eliminarProducto(codigo) {
        const confirmed = await Utils.confirmAction(`¿Está seguro de eliminar el producto ${codigo}?`);
        if (!confirmed) return;

        try {
            await Utils.makeRequest(`/api/gestion/producto/${codigo}`, {
                method: 'DELETE'
            });

            Utils.showAlert('✅ Producto eliminado exitosamente', 'success');
            await this.cargarProductos();

        } catch (error) {
            Utils.showAlert('❌ Error al eliminar producto: ' + error.message, 'error');
        }
    }

    mostrarProductos() {
        const container = document.getElementById('productosList');
        
        if (this.productos.length === 0) {
            container.innerHTML = '<tr><td colspan="5" class="text-center">No hay productos registrados</td></tr>';
            return;
        }

        container.innerHTML = this.productos.map(producto => `
            <tr>
                <td>${producto.codigo}</td>
                <td>${producto.detalle}</td>
                <td>${Utils.formatCurrency(producto.precio)}</td>
                <td><span class="badge ${producto.activo ? 'badge-success' : 'badge-danger'}">${producto.activo ? 'Activo' : 'Inactivo'}</span></td>
                <td>
                    <button class="btn btn-danger btn-sm" 
                            onclick="adminApp.eliminarProducto('${producto.codigo}')">
                        🗑️ Eliminar
                    </button>
                </td>
            </tr>
        `).join('');
    }

    mostrarPedidos() {
        const container = document.getElementById('pedidosList');
        
        if (this.pedidos.length === 0) {
            container.innerHTML = '<tr><td colspan="7" class="text-center">No hay pedidos registrados</td></tr>';
            return;
        }

        container.innerHTML = this.pedidos.map(pedido => {
            const total = pedido.productos.reduce((sum, item) => 
                sum + (item.precio * item.cantidad), 0
            );

            return `
                <tr>
                    <td>${pedido.id.substring(0, 8)}...</td>
                    <td>${pedido.clienteNombre}</td>
                    <td>${pedido.vendedorCodigo}</td>
                    <td>${Utils.formatDate(pedido.fecha)}</td>
                    <td><span class="badge badge-info">${pedido.estado}</span></td>
                    <td>${Utils.formatCurrency(total)}</td>
                    <td>
                        <small>${pedido.productos.map(p => 
                            `${p.detalle} (x${p.cantidad})`
                        ).join(', ')}</small>
                    </td>
                </tr>
            `;
        }).join('');
    }

    async exportarPedidos() {
        try {
            const response = await fetch('/api/venta/exportar/xlsx');
            
            if (!response.ok) {
                throw new Error('Error al exportar pedidos');
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'pedidos_papeleria.xlsx';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);

            Utils.showAlert('✅ Pedidos exportados exitosamente a Excel', 'success');

        } catch (error) {
            Utils.showAlert('❌ Error al exportar pedidos: ' + error.message, 'error');
        }
    }

    actualizarUI() {
        document.getElementById('adminName').textContent = this.currentUser.nombre;
        document.getElementById('adminCode').textContent = this.currentUser.codigo;
        this.mostrarSeccion('gestion-pedidos');
    }
}

const adminApp = new AdminApp();