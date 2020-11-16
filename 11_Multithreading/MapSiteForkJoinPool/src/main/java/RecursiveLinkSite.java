import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class RecursiveLinkSite extends RecursiveTask<Set<String>> {

    private final String domain;
    private final String url;
    private final int nLevel;
    private int count;

    /**
     * @param url    - Исходный каталог
     * @param nLevel - Уровень вложенности сайта
     * @param domain - Домен сайта
     */
    public RecursiveLinkSite(String url, String domain, int nLevel) {
        this.url = url;
        this.domain = domain;
        this.nLevel = nLevel;
        count = 0;
    }
    /**
     * @param url    - Исходный каталог
     * @param nLevel - Уровень вложенности сайта
     * @param domain - Домен сайта
     * @param count - счетчик глубины
     */
    public RecursiveLinkSite(String url, String domain, int nLevel, int count) {
        this.url = url;
        this.domain = domain;
        this.nLevel = nLevel;
        this.count = count;
    }

    @Override
    protected Set<String> compute() {
        count++;
        Set<String> list = new HashSet<>();
        List<RecursiveLinkSite> tasks = new ArrayList<>();
        Set<String> links = getAllLink(url);

        if (count < nLevel) {
            if (links != null) {
                for (String link : links) {
                    RecursiveLinkSite task = new RecursiveLinkSite(link, domain, nLevel, count);
                    task.fork();
                    tasks.add(task);
                    list.add(link);
                }
            }
        }
        for (RecursiveLinkSite item : tasks) {
            list.addAll(item.join());
        }
        return list;
    }

    public Set<String> getAllLink(String urls) {
        Set<String> links = new HashSet<>();
        try {
            Thread.sleep(150);
            Document doc;
            doc = Jsoup.connect(urls)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla")
                    .get();
            Elements elements = doc.select("a[href]:not([href~=(?i)\\.pdf$])");
            for (Element element : elements) {
                String link = element.absUrl("href");
                if (link.contains(domain) && !link.equals(urls) && !link.contains("#") && !link.equals("https:" + domain)) {
                    links.add(link);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return links;
    }
}
