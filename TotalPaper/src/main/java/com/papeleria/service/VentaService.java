package com.papeleria.service;

import com.papeleria.model.Pedido;
import com.papeleria.model.Producto;
import com.papeleria.dto.PedidoDTO;
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

@Service
public class VentaService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Pedido registrarPedido(Pedido pedido, List<PedidoDTO.ProductoPedidoDTO> productosDTO) {
        if (pedido.getClienteNombre() == null || pedido.getClienteNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es requerido");
        }
        
        if (productosDTO == null || productosDTO.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto");
        }

        for (PedidoDTO.ProductoPedidoDTO productoDTO : productosDTO) {
            Producto producto = productoRepository.findByCodigo(productoDTO.getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoDTO.getCodigo()));
            
            if (!producto.isActivo()) {
                throw new IllegalArgumentException("Producto no disponible: " + productoDTO.getCodigo());
            }
            
            Pedido.ProductoPedido productoPedido = new Pedido.ProductoPedido();
            productoPedido.setCodigo(producto.getCodigo());
            productoPedido.setDetalle(producto.getDetalle());
            productoPedido.setPrecio(producto.getPrecio());
            productoPedido.setCantidad(productoDTO.getCantidad());
            
            pedido.getProductos().add(productoPedido);
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
            
            // Estilo para la cabecera
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderTop(BorderStyle.MEDIUM);
            headerStyle.setBorderLeft(BorderStyle.MEDIUM);
            headerStyle.setBorderRight(BorderStyle.MEDIUM);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            
            // Estilo para las celdas
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            
            // Crear fila de cabecera
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Cliente", "Vendedor", "Fecha", "Estado", "Total (S/)", "Productos"};
            
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
                
                row.createCell(0).setCellValue(pedido.getId().substring(0, 8) + "...");
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
                        .map(p -> p.getDetalle() + " (x" + p.getCantidad() + " - S/ " + p.getPrecio() + ")")
                        .reduce((a, b) -> a + "\n" + b)
                        .orElse("");
                row.createCell(6).setCellValue(productosStr);
                
                // Aplicar estilo a todas las celdas
                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(cellStyle);
                }
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
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pedidos_papeleria.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
                    
        } catch (Exception e) {
            throw new RuntimeException("Error al generar archivo Excel: " + e.getMessage());
        }
    }
}