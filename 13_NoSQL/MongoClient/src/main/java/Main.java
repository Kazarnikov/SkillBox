import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.bson.BsonDocument;
import org.bson.Document;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class Main {
    private static String fileCSV = "src/main/resources/mongo.csv";

    public static void main(String[] args) {
        // "127.0.0.1" , 27017
        try (MongoClient mongoClient = MongoClients.create()) {
            MongoDatabase database = mongoClient.getDatabase("test");
            database.drop();
            MongoCollection<Document> studentsCollection = database.getCollection("students");
            List<Document> students = parserCSV(fileCSV);
            studentsCollection.insertMany(students);

            System.out.println("Общее количество студентов в базе: "
                    + studentsCollection.countDocuments());

            System.out.println("Количество студентов старше 40 лет: "
                    + studentsCollection
                    .countDocuments(gt("age", 40)));

            System.out.println("Имя самого молодого студента: "
                    + Objects.requireNonNull(studentsCollection
                    .find()
                    .sort(ascending("age"))
                    .first())
                    .get("name"));

            System.out.println("Список курсов самого старого студента: "
                    + Objects.requireNonNull(studentsCollection
                    .find()
                    .sort(descending("age"))
                    .first())
                    .get("courses"));

//            studentsCollection.find().forEach((Consumer<Document>) e -> System.out.println(e.toJson()));
//
//            BsonDocument q = BsonDocument.parse("{age: -1}");
//            studentsCollection.find().sort(q).forEach((Consumer<Document>) System.out::println);

        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Document> parserCSV(String name) throws IOException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader(name));
        String[] nextLine;
        List<Document> list = new ArrayList<>();
        while ((nextLine = reader.readNext()) != null) {
            list.add(new Document(Map.of(
                    "name", nextLine[0],
                    "age", Integer.parseInt(nextLine[1]),
                    "courses", new ArrayList<>(Arrays.asList(nextLine[2].split(","))))));
        }
        return list;
    }
}
