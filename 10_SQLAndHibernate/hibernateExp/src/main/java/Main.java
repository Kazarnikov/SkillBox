import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();


        Session session = sessionFactory.openSession();
        Course course = session.load(Course.class, 5);
        Teachers teacher = session.get(Teachers.class, 1);
        Students student = session.get(Students.class, 2);

        System.out.println(course.getName() + " " + course.getStudentsCount());
        System.out.println(teacher.getName() + " " + teacher.getAge());
        System.out.println(student.getName() + " " + student.getRegistration_date());
        //        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        List<Course> course1 = session.createQuery("FROM Courses where name = 'Frontend-разработчик' ", Course.class)
//                .getResultList();
//        course1.forEach(x -> System.out.println(x.getName() + " " + x.getStudentsCount()));

//        List<Course> course2 = session.createQuery("FROM Courses", Course.class).list();
//        course2.forEach(x -> System.out.println(x.getName() + " " + x.getStudentsCount()));
        session.close();
        sessionFactory.close();

    }
}