import java.util.HashMap;

public class CustomerStorage
{
    private HashMap<String, Customer> storage;

    private String rvEMail = "\\b[\\w\\d._-]+@[\\w]+\\.[\\w]{2,4}\\b";
    private String rvPhone = "^\\+7\\d{10}$";

    public CustomerStorage()
    {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) {
        String[] components = data.split("\\s+");
        String name = components[0] + " " + components[1];
        if (components[3].matches(rvPhone)) {
            if (components[2].matches(rvEMail)) {
                storage.put(name, new Customer(name, components[3], components[2]));
            } else {
                throwArgument("Wrong format e-mail. Correct format: vasily.petrov@gmail.com");
            }
        } else {
            throwArgument("Wrong format phone number. Correct format: +79215637722");
        }
    }


    public void listCustomers()
    {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name)
    {
        if (!storage.isEmpty()) {
            if (!storage.containsKey(name)) {
                System.out.println("Client not found");
                listCustomers();
            } else {
                storage.remove(name);
                System.out.println("Done");
            }
        } else {
            System.out.println("The list is empty");
        }
    }

    public int getCount()
    {
        return storage.size();
    }

    public void throwArgument(String nameArgument){
        throw new IllegalArgumentException(nameArgument);
    }
}