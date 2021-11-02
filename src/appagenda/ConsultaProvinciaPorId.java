
package appagenda;

import entidades.Provincia;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConsultaProvinciaPorId 
{
    public static void main(String[] args) 
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em = emf.createEntityManager();
        
        Provincia provinciaId2=em.find(Provincia.class,2);
        if (provinciaId2 != null)
        {
            System.out.print(provinciaId2.getId() + ":");
            System.out.println(provinciaId2.getNombre());
        } 
        else 
        {
            System.out.println("No hay ninguna provincia con ID=2");
        }
        
        em.close();
        emf.close();
    }
}
