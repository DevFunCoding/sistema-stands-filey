package com.filey.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class FileyView extends JFrame {
    private final DefaultTableModel modeloTabla;
    private final JTable tablaStands;

    private final JButton btnReservar = new JButton("Reservar Stand");
    private final JButton btnPagar = new JButton("Registrar Pago");
    private final JButton btnNotificaciones = new JButton("Procesar Alertas");
    private final JButton btnEditoriales = new JButton("Ver Editoriales");
    private final JButton btnActualizar = new JButton("Actualizar");

    public FileyView() {
        setTitle("Sistema Administrativo FILEY ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 520);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(new Color(245, 247, 250));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(24, 49, 83));
        panelHeader.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel lblTitulo = new JLabel("PANEL DE CONTROL DE STANDS — FILEY");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Salomon Jesus Balam Chuc");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(180, 200, 225));

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(lblSubtitulo, BorderLayout.EAST);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        String[] columnas = {"ID Stand", "Superficie", "Costo Total", "Estado", "Editorial Asignada", "Monto Pagado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaStands = new JTable(modeloTabla);
        tablaStands.setRowHeight(32);
        tablaStands.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaStands.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaStands.setSelectionBackground(new Color(220, 235, 252));
        tablaStands.setSelectionForeground(Color.BLACK);
        tablaStands.setGridColor(new Color(225, 230, 235));
        tablaStands.setShowVerticalLines(false);

        tablaStands.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaStands.getTableHeader().setBackground(new Color(232, 237, 243));
        tablaStands.getTableHeader().setForeground(new Color(30, 41, 59));
        tablaStands.getTableHeader().setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaStands.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaStands.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaStands.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaStands.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        tablaStands.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                String estado = value != null ? value.toString() : "";
                if (!isSelected) {
                    switch (estado) {
                        case "DISPONIBLE":
                            c.setForeground(new Color(22, 130, 60));
                            break;
                        case "RESERVADO":
                            c.setForeground(new Color(195, 110, 10));
                            break;
                        case "OCUPADO":
                            c.setForeground(new Color(25, 95, 180));
                            break;
                        case "BLOQUEADO":
                            c.setForeground(new Color(190, 40, 40));
                            break;
                        default:
                            c.setForeground(Color.DARK_GRAY);
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaStands);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 218, 226)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panelBotones.setBackground(new Color(245, 247, 250));

        estilizarBoton(btnReservar, new Color(25, 135, 84));
        estilizarBoton(btnPagar, new Color(13, 110, 253));
        estilizarBoton(btnNotificaciones, new Color(217, 119, 6));
        estilizarBoton(btnEditoriales, new Color(71, 85, 105));
        estilizarBoton(btnActualizar, new Color(51, 65, 85));

        panelBotones.add(btnReservar);
        panelBotones.add(btnPagar);
        panelBotones.add(btnNotificaciones);
        panelBotones.add(btnEditoriales);
        panelBotones.add(btnActualizar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        setContentPane(panelPrincipal);
    }

    private void estilizarBoton(JButton btn, Color fondo) {
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public void agregarFilaStand(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    public String getIdStandSeleccionado() {
        int fila = tablaStands.getSelectedRow();
        if (fila == -1) return null;
        return modeloTabla.getValueAt(fila, 0).toString();
    }

    public String[] pedirDatosEditorial(String idStand) {
        JTextField txtNombre = new JTextField();
        JTextField txtEmail = new JTextField();
        Object[] campos = {
            "Stand seleccionado: " + idStand,
            "Nombre de la Editorial:", txtNombre,
            "Email de Contacto:", txtEmail
        };
        int opcion = JOptionPane.showConfirmDialog(this, campos, "Nueva Reservación", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            return new String[]{txtNombre.getText().trim(), txtEmail.getText().trim()};
        }
        return null;
    }

    public String pedirMontoPago(String idStand) {
        return JOptionPane.showInputDialog(this, "Ingrese el monto a abonar para el stand " + idStand + ":", "Registrar Pago", JOptionPane.QUESTION_MESSAGE);
    }

    public void mostrarInfo(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public void addActualizarListener(ActionListener l) { btnActualizar.addActionListener(l); }
    public void addReservarListener(ActionListener l) { btnReservar.addActionListener(l); }
    public void addPagarListener(ActionListener l) { btnPagar.addActionListener(l); }
    public void addNotificacionesListener(ActionListener l) { btnNotificaciones.addActionListener(l); }
    public void addEditorialesListener(ActionListener l) { btnEditoriales.addActionListener(l); }
}