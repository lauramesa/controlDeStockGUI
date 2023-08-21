package com.control.stock.controller;

import com.control.stock.factory.ConnectionFactory;
import com.control.stock.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
		Connection con = factory.recuperaConexion();

		PreparedStatement statement = con
				.prepareStatement("UPDATE PRODUCTO SET "
						+ " NOMBRE = ?, "
						+ " DESCRIPCION = ?,"
						+ " CANTIDAD = ?"
						+ " WHERE ID = ?");

		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);
		statement.setInt(4, id);
		statement.execute();

		int updateCount = statement.getUpdateCount();

		con.close();

		return updateCount;
	}

	public int eliminar(Integer id) {
		try {
			Connection  con = new ConnectionFactory().recuperaConexion();

			PreparedStatement statement = con
					.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");
			statement.setInt(1, id);
			statement.execute();

			int updateCount = statement.getUpdateCount();

			return updateCount;

		} catch (SQLException e){
			throw new RuntimeException(e);
		}
	}

	public List<Map<String, String>> listar() {

		List<Map<String, String>> resultado = new ArrayList<>();

		try {
			ConnectionFactory factory = new ConnectionFactory();
			Connection con = factory.recuperaConexion();

			PreparedStatement statement = con
					.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");
			statement.execute();

			ResultSet resultSet = statement.getResultSet();



			while (resultSet.next()) {
				Map<String, String> fila = new HashMap<>();
				fila.put("ID", String.valueOf(resultSet.getInt("ID")));
				fila.put("NOMBRE", resultSet.getString("NOMBRE"));
				fila.put("DESCRIPCION", resultSet.getString("DESCRIPCION"));
				fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));

				resultado.add(fila);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		// return new ArrayList<>();
		return resultado;
	}

    public void guardar(Producto producto) throws SQLException{
		try {
			ConnectionFactory factory = new ConnectionFactory();
			Connection con = factory.recuperaConexion();
			//se saca la responsabilidad del jdbc, para concluir la transaccion
			con.setAutoCommit(false);
			PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTO(nombre,descripcion,cantidad)" +
					"VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);

			try (statement){
				ejecutarRegistro(producto, statement);
				con.commit();
			}catch (Exception e){
				con.rollback();
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void ejecutarRegistro(Producto producto, PreparedStatement statement) throws SQLException {
		statement.setString(1, producto.getNombre());
		statement.setString(2, producto.getDescripcion());
		statement.setInt(3, producto.getCantidad());

		statement.execute();

		ResultSet resultSet = statement.getGeneratedKeys();

		try (resultSet){
			while (resultSet.next()){
				producto.setId(resultSet.getInt(1));
				System.out.println(String.format("dato ingresado %s", producto));
			}
		}
	}

}
