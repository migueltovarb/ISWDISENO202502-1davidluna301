// service/VentaService.java
package com.papeleria.service;

import com.papeleria.model.Pedido;
import com.papeleria.model.Producto;
import com.papeleria.repository.PedidoRepository;
import com.papeleria.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.math.BigDecimal; // Importación faltante

@Service
public class VentaService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Pedido registrarPedido(Pedido pedido) {
        // Validaciones
        if (pedido.getClienteNombre() == null || pedido.getClienteNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es requerido");
        }
        
        if (pedido.getProductos() == null || pedido.getProductos().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto");
        }

        // Calcular totales y validar productos
        for (Pedido.ProductoPedido productoPedido : pedido.getProductos()) {
            Producto producto = productoRepository.findByCodigo(productoPedido.getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoPedido.getCodigo()));
            
            if (!producto.isActivo()) {
                throw new IllegalArgumentException("Producto no disponible: " + productoPedido.getCodigo());
            }
            
            // Asignar detalles del producto si no están presentes
            if (productoPedido.getDetalle() == null) {
                productoPedido.setDetalle(producto.getDetalle());
            }
            if (productoPedido.getPrecio() == null) {
                productoPedido.setPrecio(producto.getPrecio());
            }
        }

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAllByOrderByFechaDesc();
    }

    public ResponseEntity<ByteArrayResource> generarXLSX() {
        try {
            List<Pedido> pedidos = listarPedidos();
            
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Pedidos");
            
            // Crear estilo de cabecera
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            
            // Crear fila de cabecera
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Cliente", "Vendedor", "Fecha", "Estado", "Total", "Productos"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenar datos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            int rowNum = 1;
            
            for (Pedido pedido : pedidos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pedido.getId());
                row.createCell(1).setCellValue(pedido.getClienteNombre());
                row.createCell(2).setCellValue(pedido.getVendedorCodigo());
                row.createCell(3).setCellValue(pedido.getFecha().format(formatter));
                row.createCell(4).setCellValue(pedido.getEstado());
                
                // Calcular total
                double total = pedido.getProductos().stream()
                        .mapToDouble(p -> p.getPrecio().doubleValue() * p.getCantidad())
                        .sum();
                row.createCell(5).setCellValue(total);
                
                // Listar productos
                String productosStr = pedido.getProductos().stream()
                        .map(p -> p.getDetalle() + " (x" + p.getCantidad() + ")")
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");
                row.createCell(6).setCellValue(productosStr);
            }
            
            // Autoajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Convertir a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            
            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pedidos.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
                    
        } catch (Exception e) {
            throw new RuntimeException("Error al generar archivo Excel: " + e.getMessage());
        }
    }
}