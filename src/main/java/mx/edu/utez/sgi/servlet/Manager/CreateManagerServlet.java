package mx.edu.utez.sgi.servlet.Manager;

import mx.edu.utez.sgi.dao.ManagerDao;
import mx.edu.utez.sgi.entities.Manager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateManagerServlet", value = "/CreateManagerServlet")
public class CreateManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Boolean flag = (Boolean) request.getAttribute("flag");

        if (flag != null && flag) {
            request.setAttribute("successMessage", "Asignado de artículos creado exitosamente.");
        } else if (flag != null && !flag) {
            request.setAttribute("errorMessage", "Error al crear al asignado de artículos. Inténtalo de nuevo.");
        }

        request.getRequestDispatcher("/view/asignados.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset UTF-8");

        ManagerDao managerDao = new ManagerDao();

        String nombre1A = request.getParameter("nombre1A");
        String nombre2A = request.getParameter("nombre2A");
        String apellido1A = request.getParameter("apellido1A");
        String apellido2A = request.getParameter("apellido2A");
        long numEmpleado = Long.parseLong(request.getParameter("numEmpleado"));
        String fechaResguardo = request.getParameter("fechaResguardo");

        Manager manager = new Manager(true, nombre1A, nombre2A, apellido1A, apellido2A, numEmpleado, fechaResguardo);

        if(managerDao.createManager(manager)){
            request.setAttribute("flag", true);
        }else{
            request.setAttribute("flag", false);
        }


        doGet(request, response);
    }
}