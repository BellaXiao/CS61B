import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        /* Self: find start node s closest to start lon/lat, and end node t closest to end lon/lat.
        * find the SP between s and t, using A*, h(n) = GraphDB.distance(n, t)
        * could use a PQ, adding nodes to PQ with priority as d(s,v) + edge(v,w) + h(w)
        * passing self-defined comparator to the PQ constructor.
        * */
        long s = g.closest(stlon, stlat);
        long t = g.closest(destlon, destlat);
        HashMap<Long, Double> pathDistMap = new HashMap<>();
        HashMap<Long, Long> parentMap = new HashMap<>();
        HashSet<Long> markedSet = new HashSet<>();

        Comparator<Long> comparator = new AstarComparator(pathDistMap, t, g);
        PriorityQueue<Long> minPQ = new PriorityQueue<>(16,comparator);
        minPQ.add(s);
        pathDistMap.put(s, 0.0);
        parentMap.put(s, s);
        while (!minPQ.isEmpty()) {
            long v = minPQ.remove();
            if (markedSet.contains(v)) {
                continue;
            } else {
                markedSet.add(v);
            }
            if (v == t) {
                return genPathList(parentMap, s, v);
            }
            for (long w: g.adjacent(v)) {
                double newDist = pathDistMap.get(v) + g.distance(w, v);
                if (!pathDistMap.containsKey(w)) {
                    pathDistMap.put(w, newDist);
                    parentMap.put(w, v);
                } else {
                    double oriDist = pathDistMap.get(w);
                    if (oriDist > newDist) {
                        pathDistMap.put(w, newDist);
                        parentMap.put(w, v);
                    }
                }
                minPQ.add(w);
                //System.out.println(minPQ);
            }
        }
        return null; // FIXME
    }

    /* Self: build a A* comparator and pass to PQ constructor
     */
    private static class AstarComparator implements Comparator<Long> {

        HashMap<Long, Double> pathDistMap;
        long endNode;
        GraphDB g;

        // Self: maybe a constructor passing in pathDistMap and endnode
        AstarComparator(HashMap<Long, Double> map, long t, GraphDB g){
            pathDistMap = map;
            endNode = t;
            this.g = g;
        }

        @Override
        public int compare(Long node1ID, Long node2ID) {
            double AstarDistN1 = pathDistMap.get(node1ID) + g.distance(node1ID, endNode);
            double AstarDistN2 = pathDistMap.get(node2ID) + g.distance(node2ID, endNode);
            if (AstarDistN1 < AstarDistN2) {
                return -1;
            } else if (AstarDistN1 > AstarDistN2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /* Self: build a helper func to generate shortestPath List<Long> */
    public static List<Long> genPathList(HashMap<Long, Long> parentMap, long s, long t) {
        Stack<Long> buffer = new Stack<>();
        List<Long> pathList = new ArrayList<>();
        buffer.add(t);
        while (t != s) {
            buffer.add(parentMap.get(t));
            t = parentMap.get(t);
        }
        while (!buffer.isEmpty()) {
            pathList.add(buffer.pop());
        }
        return pathList;
    }





    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {


        /*
        Self: have a direction, distance += edge.distance, and a name,
        when the name of the edge between prev and cur nodes changes,
        we generate the navigationDirection object with direction,name, and distance.
        Then set distance to zero again, and recalculate direction and reset name.

        Need to add a findEdge method in Node class in GraphDB.
        Need a helper function to determine direction based on bearing Distance.
        */
        if (route.size() < 2) {
            return null;
        }
        List<NavigationDirection> routeDirectionRes = new ArrayList<>();

        long prev = route.get(0);
        long cur = route.get(1);
        GraphDB.Edge curEdge = g.getNode(prev).getEdge(cur);
        double curDistance = curEdge.distance;
        String curName = curEdge.getName();
        int curDirection = NavigationDirection.START;
        double prevEdgeDegree = g.bearing(prev, cur);

        for (int i = 1; i < route.size() - 1; i += 1) {
            prev = cur;
            cur = route.get(i + 1);
            curEdge = g.getNode(prev).getEdge(cur);
            if (!curName.equals(curEdge.name)) {
                NavigationDirection nd = new NavigationDirection();
                nd.distance = curDistance;
                nd.direction = curDirection;
                if (curName != null && curName != "") {
                    nd.way = curName;
                } else {
                    nd.way = NavigationDirection.UNKNOWN_ROAD;
                }

                routeDirectionRes.add(nd);

                // Update curDirection and curName
                curName = curEdge.name;
                double relativeBearing = g.bearing(prev, cur) - prevEdgeDegree;
                curDirection = getDirection(relativeBearing);
                curDistance = 0.0;
            }
            curDistance += curEdge.distance;
            prevEdgeDegree = g.bearing(prev, cur);
        }
        // add the last way
        NavigationDirection nd = new NavigationDirection();
        nd.distance = curDistance;
        nd.direction = curDirection;
        nd.way = curName;
        routeDirectionRes.add(nd);

        return routeDirectionRes; // FIXME


    }

    /*
    Self: helper function, return int direction based on two nodes ID and bearing function from GraphDB
     */
    /*private static int getDirection(long v, long w, GraphDB g, double prevEdgeDegree) {
        double curEdgeDegree = g.bearing(v, w);
        double bearing = curEdgeDegree - prevEdgeDegree;
        if (bearing > -15.0 && bearing < 15.0) {
            return NavigationDirection.STRAIGHT;
        } else if (bearing > -30.0 && bearing <= -15.0) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (bearing >= 15.0 && bearing < 30.0) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (bearing > -100.0 && bearing <= -30.0) {
            return NavigationDirection.LEFT;
        } else if (bearing >= 30.0 && bearing < 100.0) {
            return NavigationDirection.RIGHT;
        } else if (bearing <= -100.0) {
            return NavigationDirection.SHARP_LEFT;
        } else {
            return NavigationDirection.SHARP_RIGHT;
        }

    }*/
    private static int getDirection(double relativeBearing) {
        double absBearing = Math.abs(relativeBearing);
        if (absBearing > 180) {
            absBearing = 360 - absBearing;
            relativeBearing *= -1;
        }
        if (absBearing <= 15) {
            return NavigationDirection.STRAIGHT;
        }
        if (absBearing <= 30) {
            return relativeBearing < 0 ? NavigationDirection.SLIGHT_LEFT : NavigationDirection.SLIGHT_RIGHT;
        }
        if (absBearing <= 100) {
            return relativeBearing < 0 ? NavigationDirection.LEFT : NavigationDirection.RIGHT;
        }
        else {
            return relativeBearing < 0 ? NavigationDirection.SHARP_LEFT : NavigationDirection.SHARP_RIGHT;
        }
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
