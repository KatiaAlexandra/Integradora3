package mx.edu.utez.sgi.servlet.Article;

import jakarta.servlet.http.HttpSession;
import mx.edu.utez.sgi.dao.ArticleDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import mx.edu.utez.sgi.entities.Article;
import mx.edu.utez.sgi.entities.Classroom;
import mx.edu.utez.sgi.entities.Manager;

@WebServlet(name = "CreateArticleServlet", value = "/CreateArticleServlet")
public class CreateArticleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Boolean flag = (Boolean) request.getAttribute("flag");

        if (flag != null && flag) {
            request.setAttribute("successMessage", "Artículo creado exitosamente.");
        } else if (flag != null && !flag) {
            request.setAttribute("errorMessage", "Error al crear el artículo. Inténtalo de nuevo.");
        }
        request.getRequestDispatcher("/view/articulos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Recuperar el nombre de usuario de la sesión
        HttpSession session = request.getSession(false);
        String currentUsername = (String) session.getAttribute("username");

        // Recuperar parámetros del formulario
        String inventoryNumber = request.getParameter("no_inventario");
        String articleName = request.getParameter("nombre_articulo");
        String brandModel = request.getParameter("marca_modelo");
        String serialNum = request.getParameter("serie");
        String specifications = request.getParameter("especificaciones");
        long managerId = Long.parseLong(request.getParameter("encargado"));
        long classroomId = Long.parseLong(request.getParameter("aula"));

        // Crear objetos Manager y Classroom
        Manager manager = new Manager();
        manager.setManager_ID(managerId);

        Classroom classroom = new Classroom();
        classroom.setClassroom_id(classroomId);

        // Crear objeto Article y asignar valores
        Article article = new Article();
        article.setInventory_number(inventoryNumber);
        article.setArticle_name(articleName);
        article.setBrand_model(brandModel);
        article.setSerial_num(serialNum);
        article.setSpecifications(specifications);
        article.setManagerObj(manager);
        article.setClassroomobj(classroom);

        // Llamar al DAO para guardar el objeto Article
        ArticleDao articleDao = new ArticleDao();

        if(articleDao.createArticle(article, currentUsername)){
            request.setAttribute("flag", true);
        }else{
            request.setAttribute("flag", false);
        }

        doGet(request, response);
    }
}