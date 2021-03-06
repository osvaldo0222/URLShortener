package root.Services;

import root.URLShortener.Browser;
import root.URLShortener.IpDevice;
import root.URLShortener.TempVisits;
import root.URLShortener.Visit;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class TempVisitsServices extends GenericCRUD<TempVisits> {
    public static TempVisitsServices instance;
    public TempVisitsServices() {super(TempVisits.class); }

    public static TempVisitsServices getInstance(){
        if (instance == null){
            instance = new TempVisitsServices();
        }
        return instance;
    }

    public List<TempVisits> visitsByDates(String hash, LocalDate date){
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("SELECT c FROM TempVisits c WHERE c.url.hash = :hash AND c.date = :date ORDER BY c.date ASC" );
        query.setParameter("hash",hash);
        query.setParameter("date",date);
        List<TempVisits> list = query.getResultList();
        entityManager.close();
        return list;
    }

    public boolean sanitizeDate(String date){
        try {
            Integer.parseInt(date.substring(6,10));
            Integer.parseInt(date.substring(0,2));
            Integer.parseInt(date.substring(3,5));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public int getByOs ( String hash, String os){
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("SELECT COUNT (c.id) FROM TempVisits c WHERE c.url.hash = :hash AND c.Os = :os" );
        query.setParameter("hash",hash);
        query.setParameter("os",os);
        Integer result = Integer.parseInt(query.getSingleResult().toString());
        entityManager.close();
        return result;
    }

    public List<TempVisits> getIPS(String hash){
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("SELECT c FROM TempVisits c WHERE c.url.hash = :hash " );
        query.setParameter("hash",hash);
        List<TempVisits> list = query.getResultList();
        entityManager.close();
        return list;

    }

    public List<Browser> listBrowser(String hash){
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("SELECT new root.URLShortener.Browser(c.browser, count(*)) FROM TempVisits c WHERE c.url.hash =:hash GROUP BY c.browser");
        query.setParameter("hash", hash);
        List<Browser> list = query.getResultList();
        entityManager.close();
        return list;
    }


}
