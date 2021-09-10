package pl.kobietydokodu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import pl.kobietydokodu.domain.Kot;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Map;

@Configuration
@ImportResource("classpath:beans.xml")
public class JdbcKotDAO implements KotDAOInterf {

    @Autowired
    private DataSource dataSource;

    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Kot getKotById(Integer jid) {
Kot kot = new Kot();
        try
    {
        Connection myConObject = dataSource.getConnection();

        if (myConObject != null) {
            String sqlSelect = "SELECT * FROM koty WHERE id = ?";
            PreparedStatement myStatObject = myConObject.prepareStatement(sqlSelect);
            myStatObject.setInt(1, jid);
            ResultSet myResultObject = myStatObject.executeQuery();
            if (myResultObject.next()) {
                kot.setName(myResultObject.getString("name"));
                kot.setDateOfBirth(myResultObject.getDate("dateOfBirth"));
                kot.setWeight(myResultObject.getFloat("weight"));
                kot.setCatOwner(myResultObject.getNString("catOwner"));
            }
            myConObject.close();
        }
    }
        catch(SQLException e) {
            System.out.println("Opps, error in connecting or executing query");
            e.printStackTrace();
        }
return kot;
    }

    @Override
    public boolean dodajKota(Kot kot) {
        boolean isCatNew = true;
        try {
            Connection myConObject = dataSource.getConnection();
            if (myConObject != null) {
                String sqlCheck = "SELECT id FROM koty where name = ? and catOwner = ?";
                PreparedStatement myStatObjec1 = myConObject.prepareStatement(sqlCheck);
                myStatObjec1.setString(1, kot.getName());
                myStatObjec1.setString(2, kot.getCatOwner());
                ResultSet myResultObject1 = myStatObjec1.executeQuery();
                if (myResultObject1.next()) {
                    System.out.println("Kot już jest w Bazie");
                    isCatNew = false;
                } else {
                    String sqlSelect = "SELECT MAX(id) FROM koty";
                    Statement myStatObject2 = myConObject.createStatement();
                    ResultSet myResultObject2 = myStatObject2.executeQuery(sqlSelect);
                    if (myResultObject2.next()) {
                        int maxid = myResultObject2.getInt(1);
                        System.out.println("zwrócony maxid = " + maxid);
                        String sqlInsert = "INSERT INTO koty (id, name, dateOfBirth, weight, catOwner) "
                                + "VALUES (?, ?, ?, ?, ?)";
                        try {
                            Date date = Date.valueOf(sdf2.format(kot.getDateOfBirth()));
                            PreparedStatement myStatObject3 = myConObject.prepareStatement(sqlInsert);
                            myStatObject3.setInt(1, maxid + 1);
                            myStatObject3.setString(2, kot.getName());
                            myStatObject3.setDate(3, date);
                            myStatObject3.setFloat(4, kot.getWeight());
                            myStatObject3.setString(5, kot.getCatOwner());
                            int rowsNr = myStatObject3.executeUpdate();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
                myConObject.close();
            }
        }
        catch(SQLException e) {
                e.printStackTrace();
            }
        return isCatNew;
    }
    public Map<Integer, Kot> getList(Map<Integer, Kot> kotyHM) {
        try{
            Connection myConObject = dataSource.getConnection();
            if (myConObject != null) {
                String sqlSelect = "SELECT * FROM koty";
                Statement myStatObject = myConObject.createStatement();
                ResultSet myResultObject = myStatObject.executeQuery(sqlSelect);
                while (myResultObject.next()) {
                    int id = myResultObject.getInt(1);
                    Kot kot = new Kot();
                    kot.setName(myResultObject.getString("name"));
                    kot.setDateOfBirth(myResultObject.getDate("dateOfBirth"));
                    kot.setWeight(myResultObject.getFloat("weight"));
                    kot.setCatOwner(myResultObject.getString("catOwner"));
                    kotyHM.put(id, kot);
                }
                myConObject.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return kotyHM;
    }

    public void usunKota(int id) {
        try {
            Connection myConOdject = dataSource.getConnection();
            String sqlDelete = "Delete from koty where id = ?";
            PreparedStatement myStatement = myConOdject.prepareStatement(sqlDelete);
            myStatement.setInt(1, id);
            int rowsNr = myStatement.executeUpdate();
            if (rowsNr > 0) {
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
