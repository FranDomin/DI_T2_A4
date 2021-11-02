
package appagenda;

import entidades.Provincia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


public class ConsultaProvinciasPorNombre 
{
    public static void main(String[] args) 
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em = emf.createEntityManager();
        
        Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
        queryProvinciaCadiz.setParameter("nombre", "Cadiz");
        List<Provincia> listProvinciasCadiz =queryProvinciaCadiz.getResultList();
        for(Provincia provinciaCadiz:listProvinciasCadiz)
        {
            System.out.println(provinciaCadiz.getId()+":");
            System.out.println(provinciaCadiz.getNombre());
        }
        
        em.close();
        emf.close();
    }
    
}
