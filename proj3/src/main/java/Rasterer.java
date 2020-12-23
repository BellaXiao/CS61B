import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    int TILE_SIZE, maxDepth;
    double ROOT_ULLAT, ROOT_ULLON, ROOT_LRLAT, ROOT_LRLON;
    double lonDPPError;

    public Rasterer() {
        // YOUR CODE HERE
        TILE_SIZE = MapServer.TILE_SIZE;
        ROOT_LRLAT = MapServer.ROOT_LRLAT;
        ROOT_LRLON = MapServer.ROOT_LRLON;
        ROOT_ULLAT = MapServer.ROOT_ULLAT;
        ROOT_ULLON = MapServer.ROOT_ULLON;
        maxDepth = 7; // d0 - d7 for images
        lonDPPError = 1E-15; // accept when lonDPPdD - clientLonDPP < lonDPPError (for TestTwelve)
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        /*System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
                           + "your browser."); */
        // read in all the useful param in params map
        // (a1,b1) is the upperleft corner, and (a2, b2) is the lowerright corner.
        double a1 = params.get("ullon");
        double b1 = params.get("ullat");
        double a2 = params.get("lrlon");
        double b2 = params.get("lrlat");
        double w = params.get("w"); // seems like h is useless if only use LonDPP

        /* validate and adjust params */
        // if params not valid for calculation, set query_success as False and return
        // use helper func to determine whether query box is completely out of map
        if (a1 > a2 || b1 < b2 || queryBoxNonOverlap(a1, b1, a2, b2)) {
            results.put("query_success", false);
            return results;
        }

        // based on client LonDPP to find the depth, get clientDPP before adjust range
        double clientLonDPP = (a2 - a1) / w;
        // if params valid not out of range, adjust it within proper range
        // use helper function to adjust within range
        a1 = adjustWithinRange(a1, ROOT_ULLON, ROOT_LRLON);
        a2 = adjustWithinRange(a2, ROOT_ULLON, ROOT_LRLON);
        b1 = adjustWithinRange(b1, ROOT_LRLAT, ROOT_ULLAT);
        b2 = adjustWithinRange(b2, ROOT_LRLAT, ROOT_ULLAT);

        /* Do the calculation for usable params now */
        int D = 0;
        double lonDPPD0 = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
        while (true) {
            if (lonDPPD0 / Math.pow(2, D) - clientLonDPP <= lonDPPError) {
                break;
            }
            D += 1;
        }
        /*
        System.out.println(lonDPPD0 / Math.pow(2, D));
        System.out.println(lonDPPD0 / Math.pow(2, D - 1));
        System.out.println(clientLonDPP);
        System.out.println(D); */
        D = Math.min(D, maxDepth);

        // get render_grid by calculating dD_Xa_Ya to dD_Xb_Yb
        double lonWidD = (ROOT_LRLON - ROOT_ULLON) / Math.pow(2, D);
        double latWidD = (ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2, D);

        int Xa = (int) ((a1 - ROOT_ULLON) / lonWidD);
        int Xb = (int) ((a2 - ROOT_ULLON) / lonWidD);
        if ((a2 - ROOT_ULLON) % lonWidD == 0) {
            Xb -= 1; // adjust for the right side
        }
        int Ya = (int) ((ROOT_ULLAT - b1) / latWidD);
        int Yb = (int) ((ROOT_ULLAT - b2) / latWidD);
        if ((ROOT_ULLAT - b2) % latWidD == 0) {
            Yb -= 1; // adjust for the right side
        }
        String[][] render_grid = new String[Yb - Ya + 1][Xb - Xa + 1];
        for (int y = Ya; y <= Yb; y += 1) {
            for (int x = Xa; x <= Xb; x += 1) {
                render_grid[y - Ya][x - Xa] = String.format("d%d_x%d_y%d.png", D, x, y);
            }
        }

        // get other params based on Xa, Xb, Ya, Yb
        double raster_ul_lon = ROOT_ULLON + Xa * lonWidD;
        double raster_lr_lon = ROOT_ULLON + (Xb + 1) * lonWidD;
        double raster_ul_lat = ROOT_ULLAT - Ya * latWidD;
        double raster_lr_lat = ROOT_ULLAT - (Yb + 1) * latWidD;

        /* put all param value pairs into results map */
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("render_grid", render_grid);
        results.put("depth", D);
        results.put("query_success", true);
        //System.out.println(results);
        return results;
    }


    // helper func to determine whether query box is completely out of map
    private boolean queryBoxNonOverlap(double a1, double b1, double a2, double b2) {
        return (a1 > ROOT_LRLON || a2 < ROOT_ULLON || b1 < ROOT_LRLAT || b2 > ROOT_ULLAT);
    }

    // helper function to adjust within range
    private double adjustWithinRange(double num, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    "minimum of the range shouldn't be larger than maximum of the range.");
        }
        if (num < min) {
            num = min;
        } else if (num > max) {
            num = max;
        }
        return num;
    }

    public static void main(String[] args) {
        Rasterer r = new Rasterer();
        HashMap<String, Double> params = new HashMap<>();
        params.put("lrlon",-122.2104604264636);
        params.put("ullon",-122.30410170759153);
        params.put("ullat",37.870213571328854);
        params.put("lrlat",37.8318576119893);
        params.put("w", 1091.0);
        params.put("h", 566.0);

        r.getMapRaster(params);
    }
}
