package com.control.stock.controller;

import com.control.stock.factory.ConnectionFactory;
import com.control.stock.modelo.Categoria;
import com.control.stock.persistencia.CategoriaDAO;
import java.util.ArrayList;
import java.util.List;

public class CategoriaController {
    private CategoriaDAO categoriaDAO;

    public CategoriaController() {
        var factory = new ConnectionFactory();
        this.categoriaDAO = new CategoriaDAO(factory.recuperaConexion());
    }

    public List<Categoria> listar() {
		return categoriaDAO.listar();
	}

    public List<?> cargaReporte() {
        // TODO
        return new ArrayList<>();
    }

}
