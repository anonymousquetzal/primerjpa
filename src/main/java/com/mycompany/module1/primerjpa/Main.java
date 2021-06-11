/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import modelo.Equipo;
//import edu.telus.primerjpa.modelo.EquipoFinanciero;
//import edu.telus.primerjpa.modelo.Equipo_;
//import edu.telus.primerjpa.modelo.Pais;
//import edu.telus.primerjpa.modelo.wrapper.EquipoWrapper1;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Equipo_;
import modelo.wrapper.EquipoWrapper1;


/**
 *
 * @author Leticia Boch
 */
public class Main {
    
    public static void crearEquipo(EntityManager entityManager) {
        try {
            entityManager.getTransaction().begin();

            Equipo equipo = new Equipo();
            equipo.setNombre("Newcastle United");
            equipo.setDireccion("Newcastle, Inglaterra");
            entityManager.persist(equipo);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            System.err.println(ex.getMessage());
        }
    }

    
     // SQL:   select * from equipo
    public static void buscarEquiposPorCriteria(EntityManager entityManager) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        System.out.println("Query con JPA Criterias");
        // select * 
        CriteriaQuery<Equipo> query = builder.createQuery(Equipo.class);
        query.from(Equipo.class);

        List<Equipo> equipoList = entityManager.createQuery(query).getResultList();

        equipoList.stream().forEach(System.out::println);
    }
    
    public static void buscaEquiposPorId(EntityManager entityManager, Integer id) {
        // SQL:   select * from equipo where id = 5
        Equipo equipo = entityManager.find(Equipo.class, 5);

        System.out.println(equipo);
    }
    
    public static void buscarEquiposPorExpresion(EntityManager entityManager, String expresion) {

        //select * from equipo e where e.nombre like ('%City%') 
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Equipo> query = builder.createQuery(Equipo.class);

        Root<Equipo> root = query.from(Equipo.class);

        query.where(
                builder.like(root.get(Equipo_.nombre), expresion)
                //builder.like(root.get("nombre"), "%City%")//without % is verbatim
        );

        entityManager.createQuery(query).getResultList().forEach(System.out::println);

    }
    
    public static void updateEquipo(EntityManager entityManager) {
        try {
            entityManager.getTransaction().begin();

            Equipo equipo = entityManager.find(Equipo.class, 6);
            equipo.setDireccion("Newcastle, Inglaterra, UK");

            entityManager.merge(equipo);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            System.err.println(ex.getMessage());
        }

    }
    
    
    public static void removeEquipo(EntityManager entityManager) {
        try {
            entityManager.getTransaction().begin();

            Equipo equipo = entityManager.find(Equipo.class, 6);
            entityManager.merge(equipo);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            System.err.println(ex.getMessage());
        }

    }
    
    public static void buscarEquipoPorId(EntityManager entityManager, Long id) {

        //select * from equipo e where e.id = 5
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Equipo> query = builder.createQuery(Equipo.class);

        Root<Equipo> root = query.from(Equipo.class);

        query.where(
                //builder.equal(root.get(Equipo_.id), 4L) 52 mm
                builder.equal(root.get(Equipo_.id), id)
        //builder.like(root.get(Equipo_.nombre), "%City%")
        );

        Equipo equipo = null;

        try {
            entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException nre) {
            System.err.println(nre.getMessage());
        } catch (NonUniqueResultException nure) {
            System.err.println("NonUniqueResult:  " + nure.getMessage());
        }

        System.out.println(equipo);
    } 
    
    public void tupleMethod(EntityManager entityManager){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);

        Root<Equipo> root = query.from(Equipo.class);

        query.multiselect(
                root.get(Equipo_.nombre).alias(Equipo_.NOMBRE),
                root.get(Equipo_.direccion).alias(Equipo_.DIRECCION),
                root.get(Equipo_.creadoEl).alias(Equipo_.CREADO_EL)
        );

        List<Tuple> resultList = entityManager.createQuery(query).getResultList();
        resultList.forEach(tuple -> {
            var nombre = tuple.get(Equipo_.NOMBRE, String.class);
            var direccion = tuple.get(Equipo_.DIRECCION, String.class);
            var creadoEl = tuple.get(Equipo_.CREADO_EL, LocalDateTime.class);
            
            System.out.print("Nombre " + nombre);
            System.out.print("\tDirrecion " + direccion);
            System.out.println("\tcreadoEl " + creadoEl);
        
        });

        entityManager.close();

    }
    
        public static void buscarEquipoWrapper1ConTuple(EntityManager entityManager) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);

        Root<Equipo> root = query.from(Equipo.class);

        query.multiselect(
                root.get(Equipo_.nombre).alias(Equipo_.NOMBRE),
                root.get(Equipo_.direccion).alias(Equipo_.DIRECCION),
                root.get(Equipo_.creadoEl).alias(Equipo_.CREADO_EL)
        );

        List<Tuple> resultList = entityManager.createQuery(query).getResultList();

        List<EquipoWrapper1> equipoWrapper1List = resultList.stream()
                .map(tuple -> {

                    var nombre = tuple.get(Equipo_.NOMBRE, String.class);
                    var direccion = tuple.get(Equipo_.DIRECCION, String.class);
                    var creadoEl = tuple.get(Equipo_.CREADO_EL, LocalDateTime.class);

                    EquipoWrapper1 equipoWrapper1 = new EquipoWrapper1();
                    equipoWrapper1.setNombre(nombre);
                    equipoWrapper1.setDireccion(direccion);
                    equipoWrapper1.setCreadoEl(creadoEl);

                    return equipoWrapper1;
                }).collect(Collectors.toList());

        equipoWrapper1List.forEach(System.out::println);
    }
        
         public static void buscarEquipoWrapper1(EntityManager entityManager) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<EquipoWrapper1> query = builder.createQuery(EquipoWrapper1.class);

        Root<Equipo> root = query.from(Equipo.class);

        query.multiselect(
                root.get(Equipo_.nombre),
                root.get(Equipo_.direccion),
                root.get(Equipo_.creadoEl)
        );

        List<EquipoWrapper1> resultList = entityManager.createQuery(query).getResultList();
        resultList.forEach(System.out::println);

    }

    public static void main(String[] args) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("com.mycompany.module1_PrimerJPA_jar_1.0-SNAPSHOTPU")
                .createEntityManager();
        
        //select * from equipo e where e.id = 5
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EquipoWrapper1> query = builder.createQuery(EquipoWrapper1.class);

        Root<Equipo> root = query.from(Equipo.class);

        query.multiselect(
                root.get(Equipo_.nombre),
                root.get(Equipo_.direccion),
                root.get(Equipo_.creadoEl)
        );

        List<EquipoWrapper1> resultList = entityManager.createQuery(query).getResultList();
        resultList.forEach(System.out::println);

        entityManager.close();

    }
    
}
