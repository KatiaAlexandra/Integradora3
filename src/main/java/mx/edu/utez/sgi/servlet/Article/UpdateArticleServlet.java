package mx.edu.utez.sgi.servlet.Article;


import mx.edu.utez.sgi.dao.ArticleDao;
import mx.edu.utez.sgi.entities.Article;
import mx.edu.utez.sgi.entities.Classroom;
import mx.edu.utez.sgi.entities.Manager;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateArticleServlet", value = "/UpdateArticleServlet")
public class UpdateArticleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Boolean flag = (Boolean) request.getAttribute("flag");

        if (flag != null && flag) {
            request.setAttribute("successMessage", "Artículo actualizado exitosamente.");
        } else if (flag != null && !flag) {
            request.setAttribute("errorMessage", "Error el actualizar el artículo. Inténtalo de nuevo.");
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

        // Recuperar parámetros del formulario con verificación de null y vacío
        String inventoryNumber = request.getParameter("u_inventory_number");

        String idParam = request.getParameter("u_id");
        if (idParam == null || idParam.isEmpty()) {
            // Manejar el error o redirigir a una página de error
            request.setAttribute("error", "El ID del artículo no puede ser nulo o vacío.");
            doGet(request, response);
            return;
        }
        long id = Long.parseLong(idParam);

        String articleName = request.getParameter("u_article_name");
        String brandModel = request.getParameter("u_brand_model");
        String serialNum = request.getParameter("u_serial_num");
        String specifications = request.getParameter("u_specifications");

        String managerIdParam = request.getParameter("u_manager");
        if (managerIdParam == null || managerIdParam.isEmpty()) {
            // Manejar el error o redirigir a una página de error
            request.setAttribute("error", "El ID del encargado no puede ser nulo o vacío.");
            doGet(request, response);
            return;
        }
        long managerId = Long.parseLong(managerIdParam);

        String classroomIdParam = request.getParameter("u_edificio");
        if (classroomIdParam == null || classroomIdParam.isEmpty()) {
            // Manejar el error o redirigir a una página de error
            request.setAttribute("error", "El ID del aula no puede ser nulo o vacío.");
            doGet(request, response);
            return;
        }
        long classroomId = Long.parseLong(classroomIdParam);

        Manager manager = new Manager();
        manager.setManager_ID(managerId);

        Classroom classroom = new Classroom();
        classroom.setClassroom_id(classroomId);

        Article article = new Article();
        article.setArticle_id(id);  // Añadir el ID para actualizar
        article.setInventory_number(inventoryNumber);
        article.setArticle_name(articleName);
        article.setBrand_model(brandModel);
        article.setSerial_num(serialNum);
        article.setSpecifications(specifications);
        article.setManagerObj(manager);
        article.setClassroomobj(classroom);

        ArticleDao articleDao = new ArticleDao();

        if(articleDao.updateArticle(article, currentUsername)){
            request.setAttribute("flag", true);
        }else{
            request.setAttribute("flag", false);
        }

        doGet(request, response);
    }
}