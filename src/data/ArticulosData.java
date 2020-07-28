package data;

import entities.Articulos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import igu.util.ErrorLogger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.sqlite.SQLiteConfig;

/**
 *
 * @author Asullom
 */
public class ArticulosData {

    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;
    static ErrorLogger log = new ErrorLogger(ArticulosData.class.getName());
    static Date dt = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat(SQLiteConfig.DEFAULT_DATE_STRING_FORMAT);
    

    public static int create(Articulos d) {
        int rsId = 0; 
        String[] returns = {"id"};
        String sql = "INSERT INTO articulos(nombre, codigo, cantidad_producto, tipo_producto, precio_unidario, fecha_ingreso, descripcion) "
                + "VALUES(?,?,?,?,?,?,?)";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql, returns);
            ps.setString(++i, d.getNombre());
            ps.setString(++i, d.getCodigo());
            ps.setDouble(++i, d.getCantidad_producto());
            ps.setString(++i, d.getTipo_producto());
            ps.setDouble(++i, d.getPrecio_unidario());
            ps.setString(++i, sdf.format(d.getFecha_ingreso()));
            ps.setString(++i, d.getDescripcion());
            rsId = ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rsId = rs.getInt(1); // select tk, max(id)  from clientes
                    //System.out.println("rs.getInt(rsId): " + rsId);
                }
                rs.close();
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "create", ex);
        }
        return rsId;
    }

    public static int update(Articulos d) {
        System.out.println("actualizar d.getId(): " + d.getId());
        int comit = 0;
        String sql = "UPDATE articulos SET "
                + "nombre=?, "
                + "codigo=? "
                + "cantidad_producto=? "
                + "tipo_producto=?"
                + "precio_unidario=?"
                + "fecha_ingreso=?"
                + "descripcion=?"

                + "WHERE id=?";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(++i, d.getNombre());
            ps.setString(++i, d.getCodigo());
            ps.setDouble(++i, d.getCantidad_producto());
            ps.setString(++i, d.getTipo_producto());
            ps.setDouble(++i, d.getPrecio_unidario());
            ps.setString(++i, sdf.format(d.getFecha_ingreso()));
            ps.setString(++i, d.getDescripcion());
            ps.setInt(++i, d.getId());
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "update", ex);
        }
        return comit;
    }

    public static int delete(int id) throws Exception {
        int comit = 0;
        String sql = "DELETE FROM articulos WHERE id = ?";
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "delete", ex);
            // System.err.println("NO del " + ex.toString());
            throw new Exception("Detalle:" + ex.getMessage());
        }
        return comit;
    }

    public static List<Articulos> list(String filter) {
        String filtert = null;
        if (filter == null) {
            filtert = "";
        } else {
            filtert = filter;
        }
        System.out.println("list.filtert:" + filtert);

        List<Articulos> ls = new ArrayList();
        String sql = "";
        if (filtert.equals("")) {
            sql = "SELECT * FROM articulos ORDER BY id";
        } else {
            sql = "SELECT * FROM articulos WHERE (id LIKE'" + filter + "%' OR "
                    + "nombre LIKE'" + filter + "%' OR codigo LIKE'" + filter + "%' OR "+ filter +"%' OR cantidad_producto LIKE'"+ filter +"%' OR tipo_producto LIKE'"+ filter +"%' OR precio_unidario LIKE'"+ filter +"%' OR fecha_ingreso LIKE'"+ filter +"%' OR descripcion LIKE'"
                    + "id LIKE'" + filter + "%') "
                    + "ORDER BY nombre";
        }
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Articulos d = new Articulos();
                d.setId(rs.getInt("id"));
                d.setNombre(rs.getString("nombre"));
                d.setCodigo(rs.getString("codigo"));
                d.setCantidad_producto(rs.getDouble("cantidad_producto"));
                d.setTipo_producto(rs.getString("tipo_producto"));
                d.setPrecio_unidario(rs.getDouble("precio_unidario"));
                d.setFecha_ingreso(rs.getDate("fecha_ingreso"));
                d.setDescripcion(rs.getString("descripcion"));
                ls.add(d);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "list", ex);
        }
        return ls;
    }

    public static Articulos getByPId(int id) {
        Articulos d = new Articulos();

            String sql = "SELECT * FROM articulos WHERE id = ? ";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(++i, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                d.setId(rs.getInt("id"));
                d.setNombre(rs.getString("nombre"));
                d.setCodigo(rs.getString("codigo"));
                d.setCantidad_producto(rs.getDouble("cantidad_producto"));
                d.setTipo_producto(rs.getString("tipo_producto"));
                d.setPrecio_unidario(rs.getDouble("precio_unidario"));
                d.setFecha_ingreso(rs.getDate("fecha_ingreso"));
                d.setDescripcion(rs.getString("descripcion"));
                
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "getByPId", ex);
        }
        return d;
    }
    /*
    public static void iniciarTransaccion() {
        try {
            cn.setAutoCommit(false);
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "iniciarTransaccion", ex);
        }
    }

    public static void finalizarTransaccion() {
        try {
            cn.commit();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "finalizarTransaccion", ex);
        }
    }

    public static void cancelarTransaccion() {
        try {
            cn.rollback();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "cancelarTransaccion", ex);
        }
    }
     */
}
