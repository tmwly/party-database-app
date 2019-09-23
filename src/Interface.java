import org.postgresql.util.PSQLException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Interface {

    private static Connection dbConn = null;
    private static PreparedStatement statement = null;

    public static void main(String[] args) {
        dbConn = DBConnector.connect();

        if (!(dbConn == null)) {

            Boolean waiting = true;
            Scanner input = new Scanner(System.in);

            String ui = "\nPlease enter one of the following commands:\nP - Party Report, M - Menu Report, I - Insert Party, Q - Quit";

            while (waiting) {
                System.out.println(ui);

                String s = input.next();
                s = s.toLowerCase();

                switch (s) {
                    case "q":
                        waiting = false;
                        break;
                    case "p":
                        inputP(input);
                        break;
                    case "m":
                        inputM(input);
                        break;
                    case "i":
                        inputI(input);
                        break;
                    default:
                        System.out.println("Invalid input: " + s);
                }

            }

            try {
                input.close();
                statement.close();
                dbConn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing connection");
            }
        }
    }

    private static void inputI(Scanner input) {
        Boolean repeat = true;

        // pid

        int pid = -1;
        System.out.println("Please enter the PID" );
        while (repeat) {
            try {
                pid = input.nextInt();
                repeat = false;
            } catch (Exception e) {
                input.nextLine();
                System.out.println("Please enter a valid PID" );
            }
        }

        repeat = true;
        System.out.println("Please enter the party name" );
        // needed to accept input
        input.nextLine();
        String partyName = input.nextLine();

        int mid = -1;
        System.out.println("Please enter an MID" );
        while (repeat) {
            try {
                mid = input.nextInt();
                repeat = false;
            } catch (Exception e) {
                input.nextLine();
                System.out.println("Please enter a valid MID" );
            }
        }

        repeat = true;
        int vid = -1;
        System.out.println("Please enter a VID" );
        while (repeat) {
            try {

                vid = input.nextInt();
                repeat = false;
            } catch (Exception e) {
                input.nextLine();
                System.out.println("Please enter a valid VID" );
            }
        }

        repeat = true;
        int eid = -1;
        System.out.println("Please enter an EID" );
        while (repeat) {
            try {
                eid = input.nextInt();
                repeat = false;
            } catch (Exception e) {
                input.nextLine();
                System.out.println("Please enter a valid EID" );
            }
        }

        repeat = true;
        int price = -1;
        System.out.println("Please enter the quoted price in the form £1234567.89" );
        input.nextLine();
        while (repeat) {
            try {
                String priceString = input.next();

                if(priceString.matches("^£[0-9]*\\.[0-9]{2}$")) {
                    priceString = priceString.replaceAll("[^0-9]", "");

                    price = Integer.parseInt(priceString);

                    repeat = false;
                } else {
                    throw new Exception();
                }


            } catch (Exception e) {
                input.nextLine();
                System.out.println("Please enter a valid price" );
            }
        }

        repeat = true;
        LocalDateTime dateTime = null;
        // needed for correct input
        input.nextLine();

        System.out.println("Please enter the date and time in the format dd/mm/yyyy hh:mm" );
        while (repeat) {
            try {
                String date = input.next();
                String time = input.next();
                int year = Integer.parseInt(date.substring(6, 10));
                int month = Integer.parseInt(date.substring(3, 5));
                int day = Integer.parseInt(date.substring(0, 2));
                int hour = Integer.parseInt(time.substring(0, 2));
                int minute = Integer.parseInt(time.substring(3, 5));
                dateTime = LocalDateTime.of(year, month, day, hour, minute);
                repeat = false;
            } catch (Exception e) {
                input.nextLine();
                System.out.println("Please enter a valid date and time" );
            }
        }

        repeat = true;
        int guests = -1;
        System.out.println("Please enter the number of guests" );
        while (repeat) {
            try {
                guests = input.nextInt();
                repeat = false;
            } catch (Exception e) {
                input.nextLine();
                System.out.println("Please enter a valid number of guests" );
            }
        }

        Timestamp timestamp = Timestamp.valueOf(dateTime);
        try {
            insert(pid, partyName, mid, vid, eid, price, timestamp, guests);
            System.out.println("Party with id: " + pid + " has been successfully inserted" );
        } catch (PSQLException e) {
            System.out.println("Failed to insert party due to the following database error:" );
            System.out.println(e.getServerErrorMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void inputM(Scanner input) {
        Boolean running = true;
        System.out.println("Please enter a valid MID, or 'b' to return to the previous menu" );

        while (running) {

            String mid = input.next();

            if (mid.equals("b" )) {
                running = false;
            } else {
                try {
                    int value = Integer.parseInt(mid);

                    String report = getMenuReport(value);
                    System.out.println("\n" + report);
                    running = false;

                } catch (NumberFormatException e) {
                    input.nextLine();
                    System.out.println("Please enter a valid MID" );
                } catch (SQLException e) {
                    input.nextLine();
                    System.out.println("Please enter a valid MID that both conforms with the database constraints and is contained in the database" );
                }
            }
        }
    }

    private static void inputP(Scanner input) {
        Boolean running = true;
        System.out.println("Please enter a valid PID, or 'b' to return to the previous menu" );

        while (running) {
            String pid = input.next();

            if (pid.equals("b" )) {
                running = false;
            } else {
                try {
                    int value = Integer.parseInt(pid);

                    String report = getPartyReport(value);
                    System.out.println("\n" + report);
                    running = false;

                } catch (NumberFormatException e) {
                    input.nextLine();
                    System.out.println("Please enter a valid PID" );
                } catch (SQLException e) {
                    input.nextLine();
                    System.out.println("Please enter a valid PID that both conforms with the database constraints and is contained in the database" );
                }
            }
        }
    }


    public static Boolean insert(int pid, String name, int mid, int vid, int eid, int price, Timestamp time, int guests) throws SQLException, PSQLException {

        statement = dbConn.prepareStatement("INSERT INTO Party" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?)" );

        statement.setInt(1, pid);
        statement.setString(2, name);
        statement.setInt(3, mid);
        statement.setInt(4, vid);
        statement.setInt(5, eid);
        statement.setInt(6, price);
        statement.setTimestamp(7, time);
        statement.setInt(8, guests);

        statement.executeUpdate();

        statement.close();

        return true;
    }

    public static String getMenuReport(int id) throws SQLException {

        statement = dbConn.prepareStatement("SELECT Menu.mid, Menu.description, Menu.costprice, COUNT(Party.mid), SUM(Party.numberofguests) FROM Menu, Party\n" +
                "WHERE Party.mid = Menu.mid AND Menu.mid = ?" +
                "\nGROUP BY Menu.mid\n" +
                "ORDER BY Menu.mid\n" );

        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        rs.next();

        String s = "Menu ID: " + rs.getInt("mid" )
                + "\nMenu Description: " + rs.getString("description" )
                + "\nMenu CostPrice: £" + rs.getInt("costprice" )
                + "\nNumber of parties using: " + rs.getInt("count" )
                + "\nTotal number of guests: " + rs.getInt("sum" );

        rs.close();
        statement.close();
        return s;
    }

    public static String getPartyReport(int id) throws SQLException {

        statement = dbConn.prepareStatement("SELECT Party.name, Party.pid, Party.numberofguests, Party.price, " +
                "Venue.name AS \"Venue Name\", Menu.Description AS \"Menu Description\", Entertainment.Description AS \"Entertainment Description\", (Entertainment.costprice + Venue.costprice + (Menu.costprice * Party.numberofguests)) AS \"Cost\", (Party.price - (Entertainment.costprice + Venue.costprice + (Menu.costprice * Party.numberofguests))) AS \"Profit\"\n" +
                "FROM Party, Venue, Menu, Entertainment\n" +
                "WHERE Party.mid = Menu.mid AND Party.vid = Venue.vid AND Party.eid = Entertainment.eid AND Party.pid = ?" );

        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        rs.next();

        String s = "Party Name: " + rs.getString("name" )
                + "\nParty ID: " + rs.getInt("pid" )
                + "\nVenue Name: " + rs.getString("Venue Name" )
                + "\nMenu Description: " + rs.getString("Menu Description" )
                + "\nEntertainment Description: " + rs.getString("Entertainment Description" )
                + "\nNumber of guests: " + rs.getInt("numberofguests" )
                + "\nPrice Charged: £" + rs.getInt("price" )
                + "\nTotal Cost: £" + rs.getInt("cost" )
                + "\nNet Profit: £" + rs.getString("profit" );

        rs.close();
        statement.close();

        return s;
    }
}
