import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
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
        Subscription subscription = session.get(Subscription.class, new Subscription.KeyId(1, 10));

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
        //ManyToMany + @OneToMany
        System.out.println("\u001B[35m 3 У студент " + student.getName() + " есть курсы " + student.getCourses()
                .stream()
                .map(Course::getName)
                .collect(Collectors.toList()) + " даты подписок " +
                student.getSubscriptions()
                        .stream()
                        .map(Subscription::getSubscriptionDate)
                        .collect(Collectors.toList()) + ".\u001B[30m" + " Student");
        //ManyToMany + @OneToMany
        System.out.println("\u001B[35m 4 На курсе \"" + course.getName() + "\" учатся студенты " + course.getStudents()
                .stream()
                .map(Student::getName)
                .collect(Collectors.toList()) + " даты подписок " +
                course.getSubscriptions()
                        .stream()
                        .map(Subscription::getSubscriptionDate)
                        .collect(Collectors.toList()) + "\u001B[30m");

        System.out.println(subscription.getCourseId().getName() + " " + subscription.getSubscriptionDate() + " " + subscription.getStudentId().getName());

        Transaction tx = session.beginTransaction();
        List<PurchaseList> purchaseLists = session.createQuery("FROM PurchaseList",PurchaseList.class).getResultList();
        List<Student> students = session.createQuery("FROM Students", Student.class).getResultList();
        List<Course> courses = session.createQuery("FROM Courses", Course.class).getResultList();
        for (PurchaseList val : purchaseLists) {
            for (Student s : students) {
                if (val.getStudentName().equals(s.getName())) {
                    for (Course c : courses) {
                        if (val.getCourseName().equals(c.getName())) {
                            session.save(new LinkedPurchaseList(s.getId(), c.getId()));
                        }
                    }
                }
            }
        }
        tx.commit();
        session.close();
        sessionFactory.close();

       /* System.out.println(course.getPurchaseList().getPrice());


        System.out.println(course.getStudents().stream().map(Student::get)
                .collect(Collectors.toList()));

                SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        List<Course> course1 = session.createQuery("FROM Courses where name = 'Frontend-разработчик' ", Course.class)
                .getResultList();
        course1.forEach(x -> System.out.println(x.getName() + " " + x.getStudentsCount()));

        List<Course> course2 = session.createQuery("FROM Courses", Course.class).list();
        course2.forEach(x -> System.out.println(x.getName() + " " + x.getStudentsCount()));*/

    }
}
