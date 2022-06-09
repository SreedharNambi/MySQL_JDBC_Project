package mypackage.iplproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        List<Match> matches = getMatchesData();
        List<Delivery> deliveries = getDeliveriesData();

        List<String> queries = getQueries();

        findNumberOfMatchesPlayedPerYear(queries);
        findNumberOfMatchesWonByTeamsOverAllYears(queries);
        findNumberOfExtraRunsConcededByTeamIn2016(queries);
        findMostEconomicalBowlerIn2015(queries);
        findNumberOfMatchesWonByTeamInHomeGround(queries);
        getTotalWicketsTakenByABowler(matches,deliveries);


    }


    private static void findNumberOfMatchesPlayedPerYear(List<String> queries) throws ClassNotFoundException, SQLException {

        String query = queries.get(0);
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProjectData", "sammy", "password");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("No. of matches played per year over all years : ");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " - " + rs.getInt(2));
        }
        con.close();
    }

    private static void findNumberOfMatchesWonByTeamsOverAllYears(List<String> queries) throws SQLException, ClassNotFoundException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProjectData", "sammy", "password");
        Statement stmt = con.createStatement();
        String query = queries.get(1);
        ResultSet rs = stmt.executeQuery(query);

        System.out.println();
        System.out.println("Number of matches won by each team over all years : ");
        while (rs.next()) {
            System.out.println(rs.getString(1) + " - " + rs.getInt(2));
        }
        con.close();
    }


    private static void findNumberOfExtraRunsConcededByTeamIn2016(List<String> queries) throws ClassNotFoundException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProjectData", "sammy", "password");
        Statement stmt = con.createStatement();
        String query = queries.get(2);
        ResultSet rs = stmt.executeQuery(query);

        System.out.println();
        System.out.println("Extra Runs Conceded by each team in 2016 :");
        while (rs.next()) {
            System.out.println(rs.getString(1) + " - " + rs.getInt(2));
        }
        con.close();
    }

    private static void findMostEconomicalBowlerIn2015(List<String> queries) throws ClassNotFoundException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProjectData", "sammy", "password");
        Statement stmt = con.createStatement();
        String query =queries.get(3);
        ResultSet rs = stmt.executeQuery(query);

        System.out.println();
        System.out.print("Most Economical Bowler of 2015 is : ");
        rs.next();
        System.out.println(rs.getString(1));
        con.close();
    }

    private static void findNumberOfMatchesWonByTeamInHomeGround(List<String> queries) throws ClassNotFoundException, SQLException {
        //Team I have chosen is SunRisers Hyderabad and their homeground is Hyderabad
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProjectData", "sammy", "password");
        Statement stmt = con.createStatement();
        String query = queries.get(4);
        ResultSet rs = stmt.executeQuery(query);

        System.out.println();
        System.out.print("No. of Matches Won by Sunrisers Hyderabad in Hyderabad : ");
        rs.next();
        System.out.println(rs.getInt(2));
        con.close();
    }


    private static List<String> getQueries() throws IOException {
        List<String> queries = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("./Queries.sql"));
        String line = "";
        while ((line = br.readLine()) != null) {
            queries.add(line);
        }
        return queries;
    }


    private static void getTotalWicketsTakenByABowler(List<Match> matches, List<Delivery> deliveries) {
        HashMap<String, Integer> totalWicketsTaken = new HashMap<>();

        ArrayList<String> matchIdOf2015 = new ArrayList<>();
        String reqyear = "2015";
        String reqBowler="Z Khan";
        for (Match match : matches) {
            if (reqyear.equals(match.getSeason())) {
                matchIdOf2015.add(match.getMatchId());
            }
        }
        for (Delivery delivery : deliveries) {
            if ((matchIdOf2015.contains(delivery.getDeliveryMatchId())) && (!Objects.equals(delivery.getWicket(), null))) {
                if(totalWicketsTaken.containsKey(delivery.getBowler())){
                    int count=totalWicketsTaken.get(delivery.getBowler());
                    totalWicketsTaken.put(delivery.getBowler(),count+1);
                }
                else{
                    totalWicketsTaken.put(delivery.getBowler(),1);
                }
            }

        }

        System.out.println("Wickets taken by reqBowler(Z Khan) "+totalWicketsTaken.get(reqBowler));




    }

    private static List<Match> getMatchesData() throws IOException {
        List<Match> matches = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("./matches.csv"));
        String line=br.readLine();
        while((line= br.readLine())!=null){
            String [] records=line.split(",");
            Match match = new Match();
            match.setMatchId(records[0]);
            match.setSeason(records[1]);
            match.setCity(records[2]);
            match.setWinningTeam(records[10]);

            matches.add(match);
        }

        return matches;
        }


        private static List<Delivery> getDeliveriesData() throws IOException{
        List<Delivery> deliveries=new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("./deliveries.csv"));
        String line= br.readLine();
        int noOfColumns = line.split(",").length;
        while((line=br.readLine())!=null){
            String [] lineData=line.split(",");
            String records[] = new String[noOfColumns];
            for(int i=0;i<lineData.length;i++){
                records[i] = lineData[i];
            }

            Delivery delivery=new Delivery();

            delivery.setDeliveryMatchId(records[0]);
            delivery.setBowler(records[8]);
            delivery.setWicket(records[18]);

            deliveries.add(delivery);


            }
        return deliveries;
    }
}


