/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gestionbodega;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class GestionBodega {
    private static class Producto {
        private String nombre;
        private String categoria;
        private int cantidad;
        private double precio;
        private static int idCounter = 1; // Contador para asignar IDs únicos
        private int id;

        public Producto(String nombre, String categoria, int cantidad, double precio) {
            this.id = idCounter++; // Asigna un ID único a cada producto
            this.nombre = nombre;
            this.categoria = categoria;
            this.cantidad = cantidad;
            this.precio = precio;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getCategoria() {
            return categoria;
        }

        public int getCantidad() {
            return cantidad;
        }

        public double getPrecio() {
            return precio;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Nombre: " + nombre + ", Categoría: " + categoria + ", Cantidad: " + cantidad + ", Precio: " + precio;
        }
    }

    private static class Bodega {
        private List<Producto> productos;

        public Bodega() {
            productos = new ArrayList<>();
            cargarDatos();
        }

        private void cargarDatos() {
            try (BufferedReader br = new BufferedReader(new FileReader("productos.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    productos.add(new Producto(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3])));
                }
            } catch (IOException e) {
                System.out.println("No se pudo cargar datos previos.");
            }
        }

        public void guardarDatos() {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("productos.txt"))) {
                for (Producto p : productos) {
                    bw.write(p.getNombre() + "," + p.getCategoria() + "," + p.getCantidad() + "," + p.getPrecio());
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error al guardar los datos.");
            }
        }

        public void agregarProducto(Producto producto) {
            productos.add(producto);
            guardarDatos();
        }

        public void modificarProducto(int index, Producto producto) {
            productos.set(index, producto);
            guardarDatos();
        }

        public void eliminarProducto(String nombre, String categoria) {
            boolean encontrado = false;
            for (Producto p : productos) {
                if (p.getNombre().equalsIgnoreCase(nombre) && p.getCategoria().equalsIgnoreCase(categoria)) {
                    productos.remove(p);
                    guardarDatos();
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                JOptionPane.showMessageDialog(null, "Producto no encontrado.");
            }
        }

        public List<Producto> listarProductos() {
            return productos;
        }

        public List<Producto> ordenarPorNombre() {
            productos.sort(Comparator.comparing(Producto::getNombre));
            return productos;
        }

        public List<Producto> ordenarPorPrecio() {
            productos.sort(Comparator.comparingDouble(Producto::getPrecio));
            return productos;
        }
    }

    private static class Autenticacion {
        private String usuario = "admin";
        private String clave = "1234";

        public boolean autenticar() {
            String usuarioIngresado = JOptionPane.showInputDialog("Usuario:");
            String claveIngresada = JOptionPane.showInputDialog("Clave:");
            return usuarioIngresado.equals(usuario) && claveIngresada.equals(clave);
        }
    }

    private static class InterfazBodega {
        private JFrame frame;
        private Bodega bodega;
        private JTextArea productosArea;
        private JPanel panelBotones;

        public InterfazBodega() {
            bodega = new Bodega();
            frame = new JFrame("Gestión Bodega");
            frame.setSize(600, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Crear el área de texto para mostrar los productos
            productosArea = new JTextArea();
            productosArea.setEditable(false);
            frame.add(new JScrollPane(productosArea), BorderLayout.CENTER);

            // Crear el panel de botones
            panelBotones = new JPanel();
            panelBotones.setLayout(new GridLayout(6, 1, 10, 10)); // 6 botones

            // Botones para las acciones
            JButton btnListar = new JButton("Listar Productos");
            JButton btnAgregar = new JButton("Agregar Producto");
            JButton btnModificar = new JButton("Modificar Producto");
            JButton btnEliminar = new JButton("Eliminar Producto");
            JButton btnOrdenarNombre = new JButton("Ordenar por Nombre");
            JButton btnOrdenarPrecio = new JButton("Ordenar por Precio");
            JButton btnSalir = new JButton("Salir");

            btnListar.addActionListener(e -> mostrarProductos());
            btnAgregar.addActionListener(e -> agregarProducto());
            btnModificar.addActionListener(e -> modificarProducto());
            btnEliminar.addActionListener(e -> eliminarProducto());
            btnOrdenarNombre.addActionListener(e -> ordenarProductosPorNombre());
            btnOrdenarPrecio.addActionListener(e -> ordenarProductosPorPrecio());
            btnSalir.addActionListener(e -> salir());

            panelBotones.add(btnListar);
            panelBotones.add(btnAgregar);
            panelBotones.add(btnModificar);
            panelBotones.add(btnEliminar);
            panelBotones.add(btnOrdenarNombre);
            panelBotones.add(btnOrdenarPrecio);
            panelBotones.add(btnSalir);

            frame.add(panelBotones, BorderLayout.WEST); // Los botones estarán en el lado izquierdo

            actualizarListaProductos();
        }

        private void mostrarProductos() {
            actualizarListaProductos();
        }

        private void actualizarListaProductos() {
            List<Producto> productos = bodega.listarProductos();
            StringBuilder sb = new StringBuilder();
            for (Producto p : productos) {
                sb.append(p.toString()).append("\n");
            }
            productosArea.setText(sb.toString());
        }

        private void agregarProducto() {
            String nombre = JOptionPane.showInputDialog("Nombre del Producto:");
            String categoria = JOptionPane.showInputDialog("Categoría del Producto:");
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Cantidad del Producto:"));
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio del Producto:"));

            Producto nuevoProducto = new Producto(nombre, categoria, cantidad, precio);
            bodega.agregarProducto(nuevoProducto);
            JOptionPane.showMessageDialog(frame, "Producto agregado exitosamente.");
            actualizarListaProductos();
        }

        private void modificarProducto() {
            int index = Integer.parseInt(JOptionPane.showInputDialog("Índice del Producto a Modificar:"));
            String nombre = JOptionPane.showInputDialog("Nuevo Nombre del Producto:");
            String categoria = JOptionPane.showInputDialog("Nueva Categoría del Producto:");
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Nueva Cantidad del Producto:"));
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Nuevo Precio del Producto:"));

            Producto productoModificado = new Producto(nombre, categoria, cantidad, precio);
            bodega.modificarProducto(index, productoModificado);
            JOptionPane.showMessageDialog(frame, "Producto modificado exitosamente.");
            actualizarListaProductos();
        }

        private void eliminarProducto() {
            String nombre = JOptionPane.showInputDialog("Nombre del Producto a Eliminar:");
            String categoria = JOptionPane.showInputDialog("Categoría del Producto a Eliminar:");
            bodega.eliminarProducto(nombre, categoria);
            actualizarListaProductos();
        }

        private void ordenarProductosPorNombre() {
            bodega.ordenarPorNombre();
            JOptionPane.showMessageDialog(frame, "Productos ordenados por nombre.");
            actualizarListaProductos();
        }

        private void ordenarProductosPorPrecio() {
            bodega.ordenarPorPrecio();
            JOptionPane.showMessageDialog(frame, "Productos ordenados por precio.");
            actualizarListaProductos();
        }

        private void salir() {
            int respuesta = JOptionPane.showConfirmDialog(frame, "¿Desea guardar los cambios antes de salir?", "Confirmar salida", JOptionPane.YES_NO_CANCEL_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                bodega.guardarDatos();
                System.exit(0);
            } else if (respuesta == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        }

        public void mostrar() {
            frame.setVisible(true);
        }
    }

    public static void main(String[] args) {
        Autenticacion autenticacion = new Autenticacion();
        if (autenticacion.autenticar()) {
            InterfazBodega interfaz = new InterfazBodega();
            interfaz.mostrar();
        } else {
            JOptionPane.showMessageDialog(null, "Autenticación fallida.");
        }
    }
}
