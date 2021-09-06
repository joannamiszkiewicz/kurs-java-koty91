package pl.kobietydokodu.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Repository;
import pl.kobietydokodu.domain.Kot;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Configuration
@ImportResource("classpath:beans.xml")
@Repository
public class JpaKotDAO implements KotDAOInterf{

    @PersistenceContext
    private EntityManager entityManagerFactory;

    @Override
    @Transactional
    public Kot getKotById(Integer id) {
        return entityManagerFactory.find(Kot.class, Long.valueOf(id));
    }

    @Transactional
    public List<Kot> getList() {
        Query query = entityManagerFactory.createQuery("from Kot k");
        List<Kot> koty;
        return koty = query.getResultList();
    }

    @Override
    @Transactional
    public boolean dodajKota(Kot kot) {
//        System.out.println(kot.przedstawSiePelne());
        boolean isCatNew = true;
//        Query query1 = entityManagerFactory.createQuery("select k from Kot k");
        String sel = "from Kot k where k.name = :koci_name and k.catOwner = :koci_catOwner";
        Query query1 = entityManagerFactory.createQuery(sel);
        query1.setParameter("koci_name", kot.getName());
        query1.setParameter("koci_catOwner", kot.getCatOwner());
        List<Kot> koty = query1.getResultList();
//        for(Kot k: koty) {
//            System.out.println(k.przedstawSie());
//            if ((k.getName().equals(kot.getName()))
//                    && (k.getCatOwner().equals(kot.getCatOwner()))) {
//                System.out.println("podany kot"+kot.przedstawSie());
//                System.out.println("kot z bazy"+k.przedstawSie());
        if (!koty.isEmpty()) {
                isCatNew = false;
            }
        Kot kotek = new Kot();
        if (isCatNew) {
            Query query2 = entityManagerFactory.createQuery("select max(k.id) from Kot k");
           Long maxId = (Long) query2.getSingleResult();
           kot.setId(maxId + 1 );
           entityManagerFactory.persist(kot);
//            kotek = entityManagerFactory.merge(kot);
        }
        return isCatNew;
    }
}
