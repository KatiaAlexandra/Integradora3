package mx.edu.utez.sgi.dao;

import mx.edu.utez.sgi.entities.Article;
import mx.edu.utez.sgi.entities.Classroom;
import mx.edu.utez.sgi.entities.Manager;
import mx.edu.utez.sgi.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDao {
    private final DBConnection DB_CONNECTION = new DBConnection();
    private final ManagerDao Manager_Dao= new ManagerDao();
    private final ClassroomDao Classroom_Dao= new ClassroomDao();
    private ResultSet rs;
    private PreparedStatement pstm;
    private Connection con;
    private final String[] QUERIES = {
            "select article_id, Inventory_Number, Article_Name, Brand_Model, Serial_Num, Specifications,Classroom_Name, First_name, First_Lastname , Building_Name from Articles join Article_Manager on Manager_ID= Manager join Classrooms on Classroom_ID=Classroom join Buildings on Building_ID = Building;",
            "SELECT * FROM Articles WHERE article_id= ?;",
            "INSERT INTO Articles(inventory_number, article_name, brand_model, serial_num, specifications, manager,classroom) VALUES(?, ?, ?, ?, ?, ?, ?);",
            "UPDATE Articles SET inventory_number = ?, article_name= ?, brand_model= ?, serial_num= ?, specifications= ?, manager= ?, classroom= ? WHERE article_id = ?;",
            "DELETE FROM Articles WHERE article_id = ?;",
    };

    public List<Article> findArticles() {
        List<Article> list = new ArrayList<>();
        try {
            con = DB_CONNECTION.getConnection();
            pstm= con.prepareStatement(QUERIES[0]);
            rs = pstm.executeQuery();
            while(rs.next()){
                Article article = new Article(
                        rs.getLong("article_id"),
                        rs.getString("inventory_number"),
                        rs.getString("article_name"),
                        rs.getString("brand_model"),
                        rs.getString("serial_num"),
                        rs.getString("specifications"),
                        rs.getString("first_name"),
                        rs.getString("classroom_name"),
                        rs.getString("building_name")
                );
                list.add(article);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return list;
    }

    public Article findArticleById(long article_id) {
        Article found = null;
        try {
            con = DB_CONNECTION.getConnection();
            pstm= con.prepareStatement(QUERIES[1]);
            pstm.setLong(1, article_id);
            rs = pstm.executeQuery();
            if(rs.next()){
                found = new Article(
                        rs.getLong("article_id"),
                        rs.getString("inventory_number"),
                        rs.getString("article_name"),
                        rs.getString("brand_model"),
                        rs.getString("serial_num"),
                        rs.getString("specifications"),
                        Manager_Dao.findManagerById(rs.getLong("manager")),
                        Classroom_Dao.findClassroomById(rs.getLong("classroom"))
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return found;
    }

    public boolean createArticle (Article article, String currentUsername){
        try{
            con = DB_CONNECTION.getConnection();
            con.setAutoCommit(false);

            pstm = con.prepareStatement(QUERIES[2]);
            pstm.setString(1, article.getInventory_number());
            pstm.setString(2, article.getArticle_name());
            pstm.setString(3, article.getBrand_model());
            pstm.setString(4, article.getSerial_num());
            pstm.setString(5, article.getSpecifications());
            pstm.setLong(6, article.getManagerObj().getManager_ID()); // Cambia String por objeto Manager
            pstm.setLong(7, article.getClassroomobj().getClassroom_id()); // Cambia String por objeto Classroom
            pstm.executeUpdate();

            // Llamar al procedimiento para insertar en la tabla History
            String sqlInsertHistory = "{CALL InsertHistory(?, ?, 'INSERT', ?)}";
            pstm = con.prepareStatement(sqlInsertHistory);
            pstm.setString(1, article.getInventory_number());
            pstm.setString(2, article.getArticle_name());
            pstm.setString(3, currentUsername);
            pstm.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e){
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
    }

    public boolean updateArticle (Article article, String currentUsername){
            try{
                con = DB_CONNECTION.getConnection();
                con.setAutoCommit(false);

                pstm = con.prepareStatement(QUERIES[3]);
                pstm.setString(1, article.getInventory_number());
                pstm.setString(2, article.getArticle_name());
                pstm.setString(3, article.getBrand_model());
                pstm.setString(4, article.getSerial_num());
                pstm.setString(5, article.getSpecifications());
                pstm.setLong(6, article.getManagerObj().getManager_ID()); // Cambia String por objeto Manager
                pstm.setLong(7, article.getClassroomobj().getClassroom_id()); // Cambia String por objeto Classroom
                pstm.setLong(8, article.getArticle_id());
                pstm.executeUpdate();

                // Llamar al procedimiento para actualizar en la tabla History
                String sqlInsertHistory = "{CALL InsertHistory(?, ?, 'UPDATE', ?)}";
                pstm = con.prepareStatement(sqlInsertHistory);
                pstm.setString(1, article.getInventory_number());
                pstm.setString(2, article.getArticle_name());
                pstm.setString(3, currentUsername);
                pstm.executeUpdate();

                con.commit();
                return true;
            } catch (SQLException e){
                if (con != null) {
                    try {
                        con.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
                return false;
            } finally {
                closeConnection();
            }
        }


    public boolean deleteArticle (long article_id, String currentUsername){
        Article found = findArticleById(article_id);
        if (found != null) {
            try{
                con = DB_CONNECTION.getConnection();
                con.setAutoCommit(false);

                pstm = con.prepareStatement(QUERIES[4]);
                pstm.setLong(1, article_id);
                int result = pstm.executeUpdate();

                // Llamar al procedimiento para eliminar en la tabla History
                String sqlInsertHistory = "{CALL InsertHistory(?, ?, 'DELETE', ?)}";
                pstm = con.prepareStatement(sqlInsertHistory);
                pstm.setString(1, found.getInventory_number());
                pstm.setString(2, found.getArticle_name());
                pstm.setString(3, currentUsername);
                pstm.executeUpdate();

                con.commit();
                return result > 0;
            } catch (SQLException e){
                if (con != null) {
                    try {
                        con.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
                return false;
            } finally {
                closeConnection();
            }
        } else {
            return false;
        }
    }

    public void closeConnection(){
        try{
            if(con != null){
                con.close();
            }
            if(pstm!= null){
                pstm.close();
            }
            if(rs!=null){
                rs.close();
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArticleDao dao = new ArticleDao();
        Manager manager = new Manager();
        manager.setManager_ID(1L);

        Classroom classroom = new Classroom();
        classroom.setClassroom_id(1);

        Article article = new Article();
        article.setArticle_id(1598);
        article.setInventory_number("13456");
        article.setArticle_name("Sillas");
        article.setManagerObj(manager);
        article.setClassroomobj(classroom);

        dao.updateArticle(article, "Daniel");
    }

}