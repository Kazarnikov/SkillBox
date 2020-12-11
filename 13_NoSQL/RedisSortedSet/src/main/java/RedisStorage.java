import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;

import java.util.Date;
import java.util.Random;

import static java.lang.System.out;

public class RedisStorage {

    // Объект для работы с Redis
    private RedissonClient redisson;

    // Объект для работы с ключами
    private RKeys rKeys;

    // Объект для работы с Sorted Set'ом
    private RScoredSortedSet<String> allUsers;

    private static final double CHANCE = 0.1; // шанс 10%

    private final static String KEY = "USERS";

    private double getTs() {
        return new Date().getTime() / 1000;
    }

    void init() {
        try {
            /** default config connect redis://127.0.0.1:6379 */
            redisson = Redisson.create();
            out.println("Подключение к Redis выполнено");
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        //rKeys.flushdb();
        allUsers = redisson.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    public void listPrint() throws InterruptedException {
        Random random = new Random();

        for (String user : allUsers) {
            if (Math.random() < CHANCE) {
                int i = random.nextInt(allUsers.size());
                user = allUsers.stream().skip(i).findFirst().get();
                out.printf("> Пользователь %s оплатил платную услугу\n", user);
            }
            out.println("— На главной странице показываем пользователя " + user);
        }
    }

    void addUsersRedis(int user_id) {
        //ZADD USERS
        allUsers.add(getTs(), String.valueOf(user_id));
    }

    void shutdown() {
        redisson.shutdown();
    }
}
