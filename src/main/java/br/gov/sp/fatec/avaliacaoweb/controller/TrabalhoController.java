package br.gov.sp.fatec.avaliacaoweb.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.sp.fatec.avaliacaoweb.dao.TrabalhoDao;
import br.gov.sp.fatec.avaliacaoweb.entity.Trabalho;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TrabalhoController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // Recupera o parâmetro id (de trabalho?id=<valor>)
        Long id = Long.valueOf(req.getParameter("id"));
        // Busca trabalho com o id
        TrabalhoDao trabalhoDao = new TrabalhoDao();
        Trabalho trabalho = trabalhoDao.buscar(id);
        // Usamos o Jackson para transformar o objeto em um JSON (String)
        ObjectMapper mapper = new ObjectMapper();
        String trabalhoJson = mapper.writeValueAsString(trabalho);
        // Formatamos a resposta
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        PrintWriter out = resp.getWriter();
        out.print(trabalhoJson);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // Recuperamos o corpo da requisição e transformamos o JSON em objeto
        ObjectMapper mapper = new ObjectMapper();
        Trabalho trabalho = mapper.readValue(req.getReader(), Trabalho.class);
        // Salvamos no Banco de Dados
        TrabalhoDao trabalhoDao = new TrabalhoDao();
        trabalhoDao.salvar(trabalho);
        // Retornamos o registro gerado
        String trabalhoJson = mapper.writeValueAsString(trabalho);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        // O código 201 requer que retornemos um header de Location
        resp.setStatus(201);
        String location = req.getServerName() + ":" + req.getServerPort() 
                + req.getContextPath() + "/trabalho?id=" + trabalho.getId();
        resp.setHeader("Location", location);
        PrintWriter out = resp.getWriter();
        out.print(trabalhoJson);
        out.flush();
    }
    
}