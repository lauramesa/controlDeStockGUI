package com.control.stock.controller;

import com.control.stock.factory.ConnectionFactory;
import com.control.stock.modelo.Categoria;
import com.control.stock.modelo.Producto;
import com.control.stock.persistencia.ProductoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	private ProductoDAO productoDAO;

	public ProductoController() {
		this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperaConexion());
	}
	public ProductoController(ProductoDAO productoDAO) {
		this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperaConexion());
	}


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

	public List<Producto> listar() {
		return productoDAO.listar();
	}

	public List<Producto> listar(Categoria categoria) {
		return productoDAO.listar(categoria.getId());
	}

    public void guardar(Producto producto, Integer categoriaid) {
		//ProductoDAO persistenciaproducto = new ProductoDAO(new ConnectionFactory().recuperaConexion());
		producto.setCategoriaid(categoriaid);
		productoDAO.guardarProducto(producto);
	}
}
