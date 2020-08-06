import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();
        Course course = session.load(Course.class, 5);
        Teacher teacher = session.get(Teacher.class, 10);
        Student student = session.get(Student.class, 2);

        //ManyToOne
        System.out.println("\u001B[35m 1 На курсе \"" + course.getName() + "\" учится "
                + course.getStudentsCount() + " чел. Ведёт курс " + course.getTeacher().getName()
                + ".\u001B[30m" + " Course");
        //OneToMany
        System.out.println("\u001B[35m 2 Учитель " + teacher.getName() + " ведет курсы "
                + teacher.getCourse()
                .stream()
                .map(Course::getName)
                .collect(Collectors.toList()) + ".\u001B[30m" + " Teacher");
        //ManyToMany
        System.out.println("\u001B[35m 3 У студент " + student.getName() + " есть курсы " + student.getCourses()
                .stream()
                .map(Course::getName)
                .collect(Collectors.toList()) + ".\u001B[30m" + " Student");
        //ManyToMany
        System.out.println("\u001B[35m 4 На курсе \"" + course.getName() + "\" учатся студенты " + course.getStudents()
                .stream()
                .map(Student::getName)
                .collect(Collectors.toList()) + "\u001B[30m");

       /* System.out.println(course.getPurchaseList().getPrice());


        System.out.println(course.getStudents().stream().map(Student::get)
                .collect(Collectors.toList()));

                SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        List<Course> course1 = session.createQuery("FROM Courses where name = 'Frontend-разработчик' ", Course.class)
                .getResultList();
        course1.forEach(x -> System.out.println(x.getName() + " " + x.getStudentsCount()));

        List<Course> course2 = session.createQuery("FROM Courses", Course.class).list();
        course2.forEach(x -> System.out.println(x.getName() + " " + x.getStudentsCount()));*/
        session.close();
        sessionFactory.close();
    }
}
