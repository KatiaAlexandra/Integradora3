package mx.edu.utez.sgi.servlet.Article;

import jakarta.servlet.http.HttpSession;
import mx.edu.utez.sgi.dao.ArticleDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteArticleServlet", value = "/DeleteArticleServlet")
public class DeleteArticleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/articulos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Recuperar el nombre de usuario de la sesión
        HttpSession session = request.getSession(false);
        String currentUsername = (String) session.getAttribute("username");

        ArticleDao articleDao = new ArticleDao();
        long id = Long.parseLong(request.getParameter("d_id"));
        boolean isDeleted = articleDao.deleteArticle(id, currentUsername);
        request.setAttribute("success", isDeleted);

        doGet(request, response);
    }
}