import java.util.stream.IntStream;

public class RedisTest {

    //количество пользователей
    private static final int USERS = 20;

    private static final int SLEEP = 1000; // 1 секунда

    public static void main(String[] args) throws InterruptedException {

        RedisStorage redis = new RedisStorage();
        redis.init();
        IntStream.rangeClosed(1, USERS).forEach(redis::addUsersRedis);

        while (true) {
            redis.listPrint();
            Thread.sleep(SLEEP);
        }
        // redis.shutdown();
    }
}
