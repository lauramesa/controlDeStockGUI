package com.control.stock.persistencia;

import com.control.stock.factory.ConnectionFactory;
import com.control.stock.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    final private Connection CON;

    public ProductoDAO(Connection con) {
        this.CON = con;
    }

    public void guardarProducto(Producto producto){
        try {
            PreparedStatement statement = CON.prepareStatement(
                    "INSERT INTO PRODUCTO(nombre,descripcion,cantidad,categoria_id) VALUES(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            try (statement){
                statement.setString(1, producto.getNombre());
                statement.setString(2,producto.getDescripcion());
                statement.setInt(3, producto.getCantidad());
                statement.setInt(4, producto.getCategoriaid());
                statement.execute();
                final  ResultSet  resultSet = statement.getGeneratedKeys();

                try (resultSet) {
                    while (resultSet.next()){
                        producto.setId(resultSet.getInt(1));
                        System.out.println(String.format("dato ingresado %s", producto));
                    }
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ejecutarRegistro(Producto producto, PreparedStatement statement) throws SQLException {
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getCantidad());
        statement.setInt(4, producto.getCategoriaid());

        statement.execute();

        ResultSet resultSet = statement.getGeneratedKeys();

        try (resultSet){
            while (resultSet.next()){
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("dato ingresado %s", producto));
            }
        }
    }

    public List<Producto> listar(){
        List<Producto> resultado = new ArrayList<>();

        try {
            ConnectionFactory factory = new ConnectionFactory();
            Connection con = factory.recuperaConexion();

            PreparedStatement statement = con
                    .prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");
            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Producto fila = new Producto(resultSet.getInt("ID"),
                        resultSet.getString("NOMBRE"),
                        resultSet.getString("DESCRIPCION"),
                        resultSet.getInt("CANTIDAD"));

                resultado.add(fila);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // return new ArrayList<>();
        return resultado;
    }

    /** Busqueda de productos por categoria
     * @param categoriaid id que desea recuperar los productos
     * @return lista de objetos 'Producto' que pertenecen a la categoria especifica
     * @throws RuntimeException si ocurre algun error al acceder a la base de datos
     * */
    public List<Producto> listar(Integer categoriaid) {
        List<Producto> resultado = new ArrayList<>();
        try {
            ConnectionFactory factory = new ConnectionFactory();
            Connection con = factory.recuperaConexion();

            PreparedStatement statement = con
                    .prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO "+
                            "WHERE CATEGORIA_ID = ?");

            try (statement){
                statement.setInt(1, categoriaid);
                statement.execute();
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    resultado.add(new Producto(resultSet.getInt("ID"),
                            resultSet.getString("NOMBRE"),
                            resultSet.getString("DESCRIPCION"),
                            resultSet.getInt("CANTIDAD"))

                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // return new ArrayList<>();
        return resultado;
    }
}
