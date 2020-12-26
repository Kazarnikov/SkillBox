import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

public class Main {
    private static final String addStore = "ДОБАВИТЬ_МАГАЗИН";
    private static final String addGood = "ДОБАВИТЬ_ТОВАР";
    private static final String addGoodToStore = "ВЫСТАВИТЬ_ТОВАР";
    private static final String info = "СТАТИСТИКА_ТОВАРОВ";
    private static final int filter_price = 100;

    private static final String[] help = {
            "Пример: " + addStore + " Девяточка",
            "Пример: " + addGood + " Вафли 54",
            "Пример: " + addGoodToStore + " Вафли Девяточка",
            "Пример: " + info};

    private static final MongoClient client = MongoClients.create();
    private static final MongoDatabase base = client.getDatabase("test");
    private static final MongoCollection<Document> collectionStores = base.getCollection("stores");
    private static final MongoCollection<Document> collectionGoods = base.getCollection("goods");

    public static void main(String[] args) throws IOException {
        collectionGoods.drop();
        collectionStores.drop();

        help();
        BufferedReader stream = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String line = stream.readLine().strip();
            String[] element = line.split(" +", 3);
            switch (element[0]) {
                case (addStore) -> addStore(element);
                case (addGood) -> addGood(element);
                case (addGoodToStore) -> addGoodToStore(element);
                case (info) -> getInfo();
                default -> help();
            }
        }
    }

    private static void addStore(String[] strings) {
        if (strings.length == 2) {
            String nameStore = strings[1];
            if (getStore(nameStore) == null) {
                collectionStores.insertOne(new Document("name", nameStore));
                System.out.printf("%s store added\n", nameStore);
            } else System.out.printf("Name %s is taken\n", nameStore);
        } else System.out.println(help[0]);
    }

    private static void addGood(String[] strings) {
        if (strings.length == 3) {
            if (getGood(strings[1]) == null) {
                collectionGoods.insertOne(new Document(Map.of("name", strings[1],
                        "price", Integer.parseInt(strings[2]))));
                System.out.printf("%s item added\n", strings[1]);
            } else {
                System.out.printf("Name %s is taken\n", strings[1]);
            }
        } else {
            System.out.println(help[1]);
        }
    }

    private static void addGoodToStore(String[] strings) {
        if (strings.length == 3) {
            Document nameGood = getGood(strings[1]);
            Document nameStore = getStore(strings[2]);
            if (nameGood != null && nameStore != null) {
                collectionStores.updateOne(nameStore,
                        Updates.addToSet("goods", nameGood.get("name")));
                System.out.printf("Good added %s to %s store\n", nameGood.get("name"), nameStore.get("name"));
            } else System.out.println("Good or Store is missing");
        } else System.out.println(help[3]);
    }

    private static void getInfo() {
        AggregateIterable<Document> aggregate = collectionGoods.aggregate(Arrays.asList(
                Aggregates.lookup("stores", "name", "goods", "list_goods"),
                Aggregates.unwind("$list_goods"),
                Aggregates.group("$list_goods.name",
                        Accumulators.sum("count_goods", 1),
                        Accumulators.max("max_price", "$price"),
                        Accumulators.min("min_price", "$price"),
                        Accumulators.avg("avg_price", "$price")
                ))
        );
        printInfo(aggregate);
    }

    private static void printInfo(AggregateIterable<Document> aggregate) {
        for (Document s : aggregate) {
            System.out.printf("""
                            In %s:
                            \tcount goods:    %s
                            \tmaximum amount: %s
                            \tminimum amount: %s
                            \taverage amount: %s
                            """,
                    s.get("_id"),
                    s.get("count_goods"),
                    s.get("max_price"),
                    s.get("min_price"),
                    s.get("avg_price")
            );
        }
        System.out.printf("Number of goods cheaper than: %s\n",
                collectionGoods.countDocuments(Filters.lt("price", filter_price)));
    }

    private static Document getStore(String name) {
        return collectionStores.find(Filters.eq("name", name)).first();
    }

    private static Document getGood(String name) {
        return collectionGoods.find(Filters.eq("name", name)).first();
    }

    private static void help() {
        Arrays.stream(help).forEach(System.out::println);
    }
}