import core.*;
import junit.framework.TestCase;
import java.util.*;

public class RouteCalculatorTest extends TestCase {

    List<Station> route;
    List<Station> route1;
    List<Station> route2;
    List<Station> route3;
    StationIndex stationIndex;
    RouteCalculator routeCalculator;

    @Override
    protected void setUp() throws Exception {

        /**  line1      line2       line3
                1         3           6
                2   <->   4           7
                3         5     <->   8
         */

        route = new ArrayList<>();
        route1 = new ArrayList<>();
        route2 = new ArrayList<>();
        route3 = new ArrayList<>();
        stationIndex = new StationIndex();
        routeCalculator = new RouteCalculator(stationIndex);

        Line line1 = new Line(1, "Первая");
        Line line2 = new Line(2, "Вторая");
        Line line3 = new Line(3, "Третья");

        Station station1 = new Station("Вишневая", line1);
        Station station2 = new Station("Яблочная", line1);
        Station station3 = new Station("Сливовая", line1);
        Station station4 = new Station("Морковная", line2);
        Station station5 = new Station("Луковая", line2);
        Station station6 = new Station("Помидорная", line2);
        Station station7 = new Station("Китовая", line3);
        Station station8 = new Station("Чебачная", line3);
        Station station9 = new Station("Щукинская", line3);

        line1.addStation(station1);
        line1.addStation(station2);
        line1.addStation(station3);
        line2.addStation(station4);
        line2.addStation(station5);
        line2.addStation(station6);
        line3.addStation(station7);
        line3.addStation(station8);
        line3.addStation(station9);

        route.add(station1);
        route.add(station2);
        route.add(station3);
        route.add(station4);
        route.add(station5);
        route.add(station6);
        route.add(station7);
        route.add(station8);
        route.add(station9);

        route1.add(station3);
        route1.add(station2);  //ожидаем, без перехода
        route1.add(station1);

        route2.add(station1);
        route2.add(station2);
        route2.add(station4);  //ожидаем, один переход
        route2.add(station5);
        route2.add(station6);

        route3.add(station1);
        route3.add(station2);
        route3.add(station4);  //ожидаем, два перехода
        route3.add(station5);
        route3.add(station8);
        route3.add(station9);

        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);

        stationIndex.stations.addAll(route);

        List<Station> connection1 = new ArrayList<>();
        connection1.add(station2);
        connection1.add(station4);
        stationIndex.addConnection(connection1);

        List<Station> connection2 = new ArrayList<>();
        connection2.add(station5);
        connection2.add(station8);
        stationIndex.addConnection(connection2);
    }

    public void testCalculateDuration()
    {
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 22.0;
        assertEquals(expected, actual);

    }

    public void testGetRouteOnTheLine()
    {
        List<Station> actual = routeCalculator.getShortestRoute(route.get(2), route.get(0));
        List<Station> expected = route1;
        assertEquals(expected, actual);
    }

    public void testGetRouteWithOneConnection()
    {
        List<Station> actual = routeCalculator.getShortestRoute(route.get(0), route.get(5));
        List<Station> expected = route2;
        assertEquals(expected, actual);
    }

    public void testGetRouteWithTwoConnections()
    {
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Вишневая"), stationIndex.getStation("Щукинская"));
        List<Station> expected = route3;
        assertEquals(expected, actual);
    }

    @Override
    protected void tearDown() throws Exception {

    }
}
