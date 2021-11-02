
package appagenda;

import entidades.Provincia;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EliminarProvinciaPorID 
{
    public static void main(String[] args) 
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em = emf.createEntityManager();
        
        Provincia provinciaId15 = em.find(Provincia.class, 15);
        em.getTransaction().begin();
        if (provinciaId15 != null)
        {
            em.remove(provinciaId15);
        }
        else
        {
            System.out.println("No hay ninguna provincia con ID=15");
        }
        em.getTransaction().commit();
    }  
}
