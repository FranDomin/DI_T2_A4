
package appagenda;

import entidades.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AppAgenda
{
    public static void main(String[] args)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em = emf.createEntityManager();
        
        Provincia provinciaCadiz = new Provincia(56,"Cadiz");
        Provincia provinciaSevilla = new Provincia();
        provinciaSevilla.setNombre("Sevilla");
        
        //em.getTransaction().begin();
        //em.persist(provinciaSevilla);
        //em.persist(provinciaCadiz);
        //em.getTransaction().commit();
        
        em.close();
        emf.close();
        try
        {
            DriverManager.getConnection("jdbc:derby:BDAgenda;shutdown=true");
        } 
        catch (SQLException ex){}


    }
}