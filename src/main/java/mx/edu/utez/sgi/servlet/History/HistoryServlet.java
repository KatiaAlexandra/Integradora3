package mx.edu.utez.sgi.servlet.History;

import mx.edu.utez.sgi.dao.HistoryDao;
import mx.edu.utez.sgi.entities.History;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HistoryServlet", value = "/HistoryServlet")
public class HistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HistoryDao historyDao = new HistoryDao();
        List<History> historyList = historyDao.getAllHistory();

        request.setAttribute("historyList", historyList);
        request.getRequestDispatcher("/view/historial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}