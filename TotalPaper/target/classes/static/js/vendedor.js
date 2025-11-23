class VendedorApp {
    constructor() {
        this.productos = [];
        this.pedidoActual = [];
        this.currentUser = Utils.getCurrentUser();
        this.init();
    }

    async init() {
        if (!this.currentUser || this.currentUser.rol !== 'VENDEDOR') {
            window.location.href = '/login';
            return;
        }

        this.setupEventListeners();
        await this.cargarProductos();
        this.actualizarUI();
    }

    setupEventListeners() {
        document.getElementById('searchProduct').addEventListener('input', (e) => {
            this.buscarProductos(e.target.value);
        });

        document.getElementById('confirmarPedido').addEventListener('click', () => {
            this.confirmarPedido();
        });

        document.getElementById('limpiarPedido').addEventListener('click', () => {
            this.limpiarPedido();
        });
    }

    async cargarProductos() {
        try {
            this.productos = await Utils.makeRequest('/api/consulta/productos');
            this.mostrarProductos(this.productos);
        } catch (error) {
            Utils.showAlert('Error al cargar los productos', 'error');
        }
    }

    async buscarProductos(query) {
        if (query.length < 2) {
            this.mostrarProductos(this.productos);
            return;
        }

        try {
            const productos = await Utils.makeRequest(`/api/consulta/productos/buscar?query=${encodeURIComponent(query)}`);
            this.mostrarProductos(productos);
        } catch (error) {
            console.error('Error en búsqueda:', error);
        }
    }

    mostrarProductos(productos) {
        const container = document.getElementById('productosList');
        container.innerHTML = '';

        if (productos.length === 0) {
            container.innerHTML = '<div class="text-center">No se encontraron productos</div>';
            return;
        }

        productos.forEach(producto => {
            const productoElement = this.crearElementoProducto(producto);
            container.appendChild(productoElement);
        });
    }

    crearElementoProducto(producto) {
        const div = document.createElement('div');
        div.className = 'card producto-card';
        div.innerHTML = `
            <div class="producto-info">
                <h4 class="producto-nombre">${producto.detalle}</h4>
                <div class="producto-details">
                    <span class="producto-codigo">Código: ${producto.codigo}</span>
                    <span class="producto-precio">${Utils.formatCurrency(producto.precio)}</span>
                </div>
                <button class="btn btn-primary btn-anadir" data-codigo="${producto.codigo}">
                    ➕ Añadir al Pedido
                </button>
            </div>
        `;

        div.querySelector('.btn-anadir').addEventListener('click', () => {
            this.anadirAlPedido(producto);
        });

        return div;
    }

    anadirAlPedido(producto) {
        const existingItem = this.pedidoActual.find(item => item.codigo === producto.codigo);
        
        if (existingItem) {
            existingItem.cantidad += 1;
        } else {
            this.pedidoActual.push({
                codigo: producto.codigo,
                detalle: producto.detalle,
                precio: producto.precio,
                cantidad: 1
            });
        }

        this.actualizarResumenPedido();
        Utils.showAlert(`${producto.detalle} añadido al pedido`, 'success');
    }

    eliminarDelPedido(codigo) {
        const producto = this.pedidoActual.find(item => item.codigo === codigo);
        this.pedidoActual = this.pedidoActual.filter(item => item.codigo !== codigo);
        this.actualizarResumenPedido();
        Utils.showAlert(`${producto.detalle} eliminado del pedido`, 'info');
    }

    actualizarResumenPedido() {
        const container = document.getElementById('resumenPedido');
        const totalElement = document.getElementById('totalPedido');
        const confirmarBtn = document.getElementById('confirmarPedido');

        container.innerHTML = '';

        if (this.pedidoActual.length === 0) {
            container.innerHTML = '<div class="text-center">No hay productos en el pedido</div>';
            totalElement.textContent = Utils.formatCurrency(0);
            confirmarBtn.disabled = true;
            return;
        }

        let total = 0;

        this.pedidoActual.forEach(item => {
            const subtotal = item.precio * item.cantidad;
            total += subtotal;

            const itemElement = document.createElement('div');
            itemElement.className = 'card mb-1 pedido-item-card';
            itemElement.innerHTML = `
                <div class="pedido-item">
                    <div class="item-header">
                        <h5 class="item-nombre">${item.detalle}</h5>
                        <button class="btn btn-danger btn-eliminar" data-codigo="${item.codigo}">
                            🗑️
                        </button>
                    </div>
                    <div class="item-details">
                        <span><strong>Código:</strong> ${item.codigo}</span>
                        <span><strong>Precio:</strong> ${Utils.formatCurrency(item.precio)}</span>
                        <span>
                            <strong>Cantidad:</strong> 
                            <input type="number" min="1" value="${item.cantidad}" 
                                   class="cantidad-input" data-codigo="${item.codigo}" 
                                   style="width: 60px; margin: 0 0.5rem;">
                        </span>
                        <span><strong>Subtotal: ${Utils.formatCurrency(subtotal)}</strong></span>
                    </div>
                </div>
            `;

            itemElement.querySelector('.btn-eliminar').addEventListener('click', () => {
                this.eliminarDelPedido(item.codigo);
            });

            itemElement.querySelector('.cantidad-input').addEventListener('change', (e) => {
                const nuevaCantidad = parseInt(e.target.value);
                if (nuevaCantidad > 0) {
                    item.cantidad = nuevaCantidad;
                    this.actualizarResumenPedido();
                } else {
                    e.target.value = item.cantidad;
                }
            });

            container.appendChild(itemElement);
        });

        totalElement.textContent = Utils.formatCurrency(total);
        confirmarBtn.disabled = false;
    }

    async confirmarPedido() {
        const clienteNombre = document.getElementById('clienteNombre').value.trim();
        
        if (!clienteNombre) {
            Utils.showAlert('Por favor, ingrese el nombre del cliente', 'error');
            return;
        }

        if (this.pedidoActual.length === 0) {
            Utils.showAlert('El pedido debe contener al menos un producto', 'error');
            return;
        }

        const confirmed = await Utils.confirmAction('¿Confirmar la creación de este pedido?');
        if (!confirmed) return;

        try {
            const pedidoDTO = {
                clienteNombre: clienteNombre,
                productos: this.pedidoActual.map(item => ({
                    codigo: item.codigo,
                    cantidad: item.cantidad
                }))
            };

            await Utils.makeRequest('/api/venta/pedido', {
                method: 'POST',
                body: JSON.stringify(pedidoDTO)
            });

            Utils.showAlert('✅ Pedido creado exitosamente', 'success');
            this.limpiarPedido();

        } catch (error) {
            Utils.showAlert('❌ Error al crear el pedido: ' + error.message, 'error');
        }
    }

    limpiarPedido() {
        this.pedidoActual = [];
        document.getElementById('clienteNombre').value = '';
        this.actualizarResumenPedido();
        Utils.showAlert('Pedido limpiado', 'info');
    }

    actualizarUI() {
        document.getElementById('userName').textContent = this.currentUser.nombre;
        document.getElementById('userCode').textContent = this.currentUser.codigo;
    }
}

// Inicializar la aplicación cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new VendedorApp();
});