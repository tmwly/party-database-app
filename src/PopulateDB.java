import java.sql.*;
import java.util.Random;

public class PopulateDB {


    private static Connection dbConn = null;
    private static Statement statement = null;


    public static void main(String[] args) {
        // check whether connected
        dbConn = DBConnector.connect();

        if(!(dbConn == null)) {

            // if the database has been cleared, populate with new values
            if(clear()) {
                populate();
            }

            // close db
            try{
                statement.close();
                dbConn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public static Boolean populate() {
        try {
            populateVenue();
            populateMenu();
            populateEnt();
            populateParty();

            System.out.println("Successfully populated database.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void populateParty() throws SQLException {
        String createParty = "CREATE TABLE Party (" +
                " pid INTEGER," +
                " name TEXT NOT NULL," +
                " mid INTEGER," +
                " vid INTEGER," +
                " eid INTEGER," +
                " price NUMERIC NOT NULL," +
                " timing TIMESTAMP  NOT NULL," +
                " numberofguests INT NOT NULL," +
                " FOREIGN KEY (mid) REFERENCES Menu(mid)," +
                " FOREIGN KEY (vid) REFERENCES Venue(vid)," +
                " FOREIGN KEY (eid) REFERENCES Entertainment(eid)," +
                " CONSTRAINT PartyKey PRIMARY KEY (pid)," +
                " CONSTRAINT MinGuests CHECK (numberofguests > 0)" +
                ")";
        statement.executeUpdate(createParty);

        for(int i =0; i < 1001; i++) {
            String name = "'Party " + i + "'";

            int mid = new Random().nextInt(150);
            int vid = new Random().nextInt(150);
            int eid = new Random().nextInt(150);

            int guestNo = new Random().nextInt(50) + 1;

            int price = (new Random().nextInt(40) * guestNo) + 1000;

            // generate a random timestamp
            long offset = Timestamp.valueOf("2018-01-01 00:00:00").getTime();
            long end = Timestamp.valueOf("2030-01-01 00:00:00").getTime();
            long diff = end - offset + 1;
            Timestamp timing = new Timestamp(offset + (long)(Math.random() * diff));

            String values =  i
                    + ", " + name
                    + ", " + mid
                    + ", " + vid
                    + ", " + eid
                    + ", " + price
                    + ", '" + timing
                    + "', " + guestNo;

            createParty = "INSERT INTO Party"
                    + " VALUES(" + values + ")";

            statement.executeUpdate(createParty);
        }
    }

    public static void populateEnt() throws SQLException {
        String createEnt = "CREATE TABLE Entertainment (" +
                " eid INTEGER," +
                " description TEXT  NOT NULL," +
                " costprice NUMERIC NOT NULL ," +
                " CONSTRAINT EntertainmentKey PRIMARY KEY (eid)," +
                " CONSTRAINT MinEntertainmentCost CHECK (costprice > 0)" +
                ")";
        statement.executeUpdate(createEnt);

        String[] adj = {"Funky", "Jazzy", "Fast", "Messy", "Tasty", "Mini", "Tiny", "Cool", "Crazy", "Frantic"};
        String[] noun = {"Clown", "Band", "Dance", "", "Drunk", "Chinese", "Italian", "Russian", "Space"};
        String[] activity = {"Hockey", "Football", "Golf", "Connect 4", "Twister", "Painting", "Drawing", "Climbing", "Cooking"};

        for(int i = 0; i <= 150; i++) {

            String nameAdj = adj[new Random().nextInt(adj.length)];
            String nameNoun = noun[new Random().nextInt(noun.length)];
            String namePlace = activity[new Random().nextInt(activity.length)];
            String name = nameAdj + " " + nameNoun + " " + namePlace;

            int random = new Random().nextInt(150) + 1;

            createEnt = "INSERT INTO Entertainment"
                    + " VALUES(" + i + ", " + "'" + name + "', " + random + ")";

            statement.executeUpdate(createEnt);
        }
    }

    public static void populateMenu() throws SQLException {
        statement = dbConn.createStatement();

        String createMenu = "CREATE TABLE Menu (" +
                " mid INTEGER," +
                " description TEXT NOT NULL," +
                " costprice NUMERIC NOT NULL," +
                " CONSTRAINT MenuKey PRIMARY KEY (mid)," +
                " CONSTRAINT MinMenuCost CHECK (costprice > 0)" +
                ")";
        statement.executeUpdate(createMenu);

        String[] adj = {"Sweet", "Salty", "Smelly", "Bumpy", "Tasty", "Massive", "Tiny", "Cool", "Crazy", "Frantic"};
        String[] noun = {"Chinese", "Fried", "Battered", "Roasted", "Sauteed", "Toasted", "Italian", "and salty", "smashed", ""};
        String[] food = {"Beans", "Pork", "Potato", "Avocado", "Noodles", "Fish", "Sushi", "Lunch", "Chicken"};

        for(int i = 0; i <= 150; i++) {

            String nameAdj = adj[new Random().nextInt(adj.length)];
            String nameNoun= noun[new Random().nextInt(noun.length)];
            String namePlace = food[new Random().nextInt(food.length)];
            String name =  nameAdj + " " + nameNoun + " " + namePlace;

            int random = new Random().nextInt(30) + 1;

            createMenu = "INSERT INTO Menu"
                    + " VALUES(" + i + ", " + "'"  + name + "', " + random + ")";

            statement.executeUpdate(createMenu);
        }
    }

    public static void populateVenue() throws SQLException {
        statement = dbConn.createStatement();

        String createVenue = "CREATE TABLE Venue (" +
                " vid INTEGER," +
                " name TEXT NOT NULL," +
                " costprice NUMERIC NOT NULL," +
                " CONSTRAINT VenueKey PRIMARY KEY (vid)," +
                " CONSTRAINT MinVenueCost CHECK (costprice > 0)" +
                ")";

        statement.executeUpdate(createVenue);

        String[] adj = {"Sweet", "Salty", "Smelly", "Bumpy", "Tasty", "Massive", "Tiny", "Cool", "Crazy", "Frantic"};
        String[] noun = {"Dave''s", "Sam''s", "Tom''s", "Marth''s", "Meg''s", "Rosie''s", "Dom''s", "Fred''s", "Andrew''s", "Rob''s"};
        String[] place = {"Steakhouse", "Bouncy Castle", "Pirate Ship", "Bar", "House", "Attic", "Pool", "Clubhouse", "Crib"};

        for(int i = 0; i <= 150; i++) {

            String nameAdj = adj[new Random().nextInt(adj.length)];
            String nameNoun= noun[new Random().nextInt(noun.length)];
            String namePlace = place[new Random().nextInt(place.length)];
            String name =  nameAdj + " " + nameNoun + " " + namePlace;

            int random = new Random().nextInt(200) + 1;

            createVenue = "INSERT INTO Venue"
                    + " VALUES(" + i + ", " + "'"  + name + "', " + random + ")";

            statement.executeUpdate(createVenue);
        }
    }

    public static Boolean clear() {
        try{
            statement = dbConn.createStatement();

            String clearDB = "DROP TABLE IF EXISTS Party, Venue, Menu, Entertainment";

            statement.executeUpdate(clearDB);
            return true;
        } catch (SQLException e){
            System.out.println("Error clearing database.");
            e.printStackTrace();
            return false;
        }
    }
}

