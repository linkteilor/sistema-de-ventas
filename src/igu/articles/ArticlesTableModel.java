package igu.articles;

import data.ArticulosData;
import entities.Articulos;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Asullom
 */
public class ArticlesTableModel extends AbstractTableModel {

    private List<Articulos> lis = new ArrayList();
    private String[] columns = {"#", "nombre", "codigo", "cantidad_producto","tipo_producto","precio_unidario","fecha_ingreso","descripcion"};
    private Class[] columnsType = {Integer.class, String.class, String.class, Double.class, String.class,Double.class, Date.class, String.class };

    public ArticlesTableModel() {
        lis = ArticulosData.list("");
    }

    public ArticlesTableModel(String filter) {
        lis = ArticulosData.list(filter);
    }

    @Override
    public Object getValueAt(int row, int column) {
        Articulos d = (Articulos) lis.get(row);
        switch (column) {
            case 0:
                return row + 1;
            case 1:
                return d.getNombre();
            case 2:
                return d.getCodigo();
            case 3:
                return d.getCantidad_producto();
            case 4:
                return d.getTipo_producto();
            case 5:
                return d.getPrecio_unidario();
            case 6:
                return d.getFecha_ingreso();
            case 7:
                return d.getDescripcion();                      
            default:
                return null;
        }
    }
/*
    @Override
    public void setValueAt(Object valor, int row, int column) {
        Articulos d = (Articulos) lis.get(row);
        switch (column) {
            
           // case 0:
           //     int id = 0;
           //     try {
            //        if (valor instanceof Number) {
           //             id = Integer.parseInt("" + valor);
           //         }
           //     } catch (NumberFormatException nfe) {
            //        System.err.println("" + nfe);
             //   }
            //    d.setId(id);
             //   break;
             
            case 1:
                d.setNombres("" + valor);
                break;
            case 2:
                d.setInfoadic("" + valor);
                break;

        }
        this.fireTableRowsUpdated(row, row);
        //fireTableCellUpdated(row, row);
    }
*/
    @Override
    public boolean isCellEditable(int row, int column) {
        //Articulos c = (Articulos) lis.get(row);
        if (column >= 0 && column != 0) {
            //return true;
        }
        return false;//bloquear edicion
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class getColumnClass(int column) {
        return columnsType[column];
    }

    @Override
    public int getRowCount() {
        return lis.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    public void addRow(Articulos d) { // con db no se usa
        this.lis.add(d);
        //this.fireTableDataChanged();
        this.fireTableRowsInserted(lis.size(), lis.size());
    }

    public void removeRow(int linha) { // con db no se usa
        this.lis.remove(linha);
        this.fireTableRowsDeleted(linha, linha);
    }

    public Object getRow(int row) { // usado para paintForm
        this.fireTableRowsUpdated(row, row);
        return lis.get(row);
    }

}
