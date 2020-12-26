import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    // Self: create helper class Node (id / lon / lat / edgeList (store edges) / name)
    // Self: Node class method: addEdge(node2)
    static class Node {
        long id;
        double lat, lon;
        String name;
        ArrayList<Edge> edgeList;

        Node(long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.name = null;
            edgeList = new ArrayList<>();
        }

        public void addEdge(long otherID, double distance) {
            edgeList.add(new Edge(id, otherID, distance));
        }

        public Edge getEdge(long otherID) {
            for (Edge e: edgeList) {
                if (e.node2ID == otherID) {
                    return e;
                }
            }
            return null;
        }


        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        // Used for getLocations in MapServer
        public double getLat() {
            return this.lat;
        }

        public double getLon() {
            return this.lon;
        }

        public long getID() {
            return this.id;
        }
    }

    // Self: create helper class Edge (node1, node2, distance, name)
    static class Edge {
        long node1ID, node2ID;
        double distance;
        String name = null;

        Edge(long id1, long id2, double dist) {
            node1ID = id1;
            node2ID = id2;
            distance = dist;
        }

        public void setName(String name) {
            this.name = name;
            // considering also insert this name into the StringTrie
            // but what if the node is cleaned later for no connection in the graph?
            // try building trie in the clean() method
        }

        public String getName() {
            return this.name;
        }
    }

    // Self: create an instance map: key = node id, value = Node
    private HashMap<Long, Node> nodesMap = new HashMap<>();
    // Self: Create an instance StringTire to store all the name Strings of nodes in map.
    // built in clean() method
    private StringTrie locationNamesTrie = new StringTrie();
    // Self: Create a map mapping cleaned name to original Name
    // built in clean() method
    private HashMap<String, ArrayList<String>> cleanedNameMap = new HashMap<>();
    // Self: Create a map mapping original name to node id
    // built in clean() method
    private HashMap<String, Long> nameToIDMap = new HashMap<>();



    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }


    /**
     * Self: add a addNode method: adding node n into nodesMap, key is its id.
     */
    public void addNode(Node n) {
        nodesMap.put(n.id, n);
    }

    /**
     * Self: add a getNode method: return the node n with id v.
     */
    public Node getNode(long v) {
        return nodesMap.get(v);
    }



    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TD: Your code here.
        // Self: loop the map, if any Node has empty edgeList, then delete it from map
        // Self: maybe a helper function to removeNode().
        ArrayList<Long> cleanID = new ArrayList<>();
        for (long id: nodesMap.keySet()) {
            if (nodesMap.get(id).edgeList.isEmpty()) {
                cleanID.add(id);
            }
        }
        // try add all nodes name, including those being cleaned later
        for (long id: vertices()) {
            String originalName = getNode(id).getName();
            if (originalName == null) {
                continue;
            }
            nameToIDMap.put(originalName, id);

            String cleanedName = cleanString(originalName);
            locationNamesTrie.insert(cleanedName);
            ArrayList<String> originalNames = new ArrayList<>();
            if (cleanedNameMap.containsKey(cleanedName)) {
                originalNames = cleanedNameMap.get(cleanedName);
            }
            originalNames.add(originalName);
            cleanedNameMap.put(cleanedName, originalNames);
        }
        /* try not remove those unconnected nodes for golden points
        for (long id: cleanID) {
            nodesMap.remove(id);
        }*/
        // considering building the trie and cleanedNameMap after nodes are cleaned up
        // by this we make sure the names of those nodes being cleaned up
        // are not added into trie or map below
        // can be used later for getLocations and get getLocationsByPrefix
        /*for (long id: vertices()) {
            String originalName = getNode(id).getName();
            if (originalName == null) {
                continue;
            }
            String cleanedName = cleanString(originalName);
            locationNamesTrie.insert(cleanedName);
            cleanedNameMap.put(cleanedName, originalName);
        }*/
    }

    /**
     * Self: creates get attibutes method related to trie and map
     */
    public StringTrie getLocationNamesTrie() {
        return locationNamesTrie;
    }
    public Map<String, ArrayList<String>> getCleanedNameMap() {
        return cleanedNameMap;
    }


    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        // Self: clean the map first. (No need to clean: it's already in constructor)
        // Self: then put all keys of the map into ArrayList
        return nodesMap.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        // Self: loop all edges in edgeList of V, return the id of the other node.
        HashSet<Long> results = new HashSet<>();
        for (Edge e: nodesMap.get(v).edgeList) {
            if (e.node1ID != v) {
                results.add(e.node1ID);
            } else if (e.node2ID != v) {
                results.add(e.node2ID);
            }
        }

        return results;

    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        // Self: this could be linear time (from lecture video)
        // Self: loop through vertexes(), then calculate distance(lon, lat, node.lon, node.lat)
        // for each vertex, find the smallest one, return it's id.
        long closestID = -1;
        double minDist = Double.POSITIVE_INFINITY;
        for (long v: vertices()) {
            double d = distance(lon, lat, lon(v), lat(v));
            if (d < minDist) {
                closestID = v;
                minDist = d;
            }
        }
        return closestID;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        // Self: go to the map, based on the id, return the lon of the node.
        return nodesMap.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        // Self: go to the map, based on the id, return the lat of the node.
        return nodesMap.get(v).lat;

    }





    /**
     * create a self trie class to help with getLocationsByPrefix method in MapServer
     */
    public static class StringTrie {
        private class Node {
            boolean isLeaf;
            Map<Character, Node> children;

            Node() {
                isLeaf = false;
                children = new HashMap<>();
            }
        }

        private Node root;
        StringTrie() {
            root = new Node();
        }

        public void insert(String key) {
            Node cur = root;
            for (char c: key.toCharArray()) {
                if (!cur.children.containsKey(c)) {
                    cur.children.put(c, new Node());
                }

                cur = cur.children.get(c);
            }
            cur.isLeaf = true;
        }

        public List<String> keysWithPrefix(String prefix) {
            List<String> resList = new ArrayList<>();
            String cache = prefix;
            Node cur = root;
            for (char c: prefix.toCharArray()) {
                if (!cur.children.containsKey(c)) {
                    return null;
                }
                cur = cur.children.get(c);
            }
            // need a helper function to do it recursively
            getKeys(cache, cur, resList);

            return resList;
        }

        private void getKeys(String cache, Node cur, List<String> resList) {
            if (cur.isLeaf) {
                resList.add(cache);
            }

            for (char c: cur.children.keySet()) {
                Node n = cur.children.get(c);
                getKeys(cache + c, n, resList);
            }
        }
    }

    /**
     * In order to make runtime of getLocationsByPrefix and getLocations in MapServer to be O(k)
     * Need to build the Trie and the nameMap in the GraphDB class clean() method first
     */
    public List<String> getLocationsByPrefix(String prefix) {
        ArrayList<String> resList = new ArrayList();
        List<String> resCleaned = locationNamesTrie.keysWithPrefix(cleanString(prefix));
        if (resCleaned != null) {
            for (String cleanedName: resCleaned) {
                for (String originalName: cleanedNameMap.get(cleanedName)) {
                    resList.add(originalName);
                }
            }
        }
        return resList;
    }




    /** No need
     * Helper function for getLocations method which is also used in MapServer
     * given original Name of the id, generate all Nodes id having that original Name
     */
    /*public List<Long> getIDfromName(String name) {
        List<Long> resIDList = new ArrayList<>();
        for (long id: vertices()) {
            if (getNode(id).getName() == name) {
                resIDList.add(id);
            }
        }
        return resIDList;
    }*/

    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> resList = new ArrayList<>();
        for (String name: getLocationsByPrefix(locationName)) {
            long id = nameToIDMap.get(name);
            Map<String, Object> nodeMap = new HashMap<>();
            GraphDB.Node node = getNode(id);
            nodeMap.put("lat", node.getLat());
            nodeMap.put("lon", node.getLon());
            nodeMap.put("name", node.getName());
            nodeMap.put("id", node.getID());
            resList.add(nodeMap);
        }
        return resList;
    }


}
