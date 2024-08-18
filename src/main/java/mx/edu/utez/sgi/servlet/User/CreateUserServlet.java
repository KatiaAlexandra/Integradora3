package mx.edu.utez.sgi.servlet.User;

import mx.edu.utez.sgi.dao.UserDao;
import mx.edu.utez.sgi.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateUserServlet", value = "/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Boolean flag = (Boolean) request.getAttribute("flag");

        if (flag != null && flag) {
            request.setAttribute("successMessage", "Usuario creado exitosamente.");
        } else if (flag != null && !flag) {
            request.setAttribute("errorMessage", "Error al crear el usuario. Inténtalo de nuevo.");
        }
        request.getRequestDispatcher("/view/gestionUsuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String username=request.getParameter("nombreU");
        String email=request.getParameter("correo");
        String password=request.getParameter("contraseña");

        User user=new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        UserDao dao = new UserDao();

        if (dao.addUser(user)) {
            request.setAttribute("flag", true);
        } else {
            request.setAttribute("flag", false);
        }

        doGet(request, response);

    }
}