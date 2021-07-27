package com.mycompany.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DB {

    private static String dbDir = null;

    static {
        try {
            dbDir = "jdbc:sqlite:"+new Store().getDir()+"/db/";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDbDir() {
        return dbDir;
    }
    /**
     * Connect to a database
     *
     * @param dbName the database file name
     */
    private Connection connect(String dbName) throws ClassNotFoundException {
        // SQLite connection string
        Class.forName("org.sqlite.JDBC");

        String url = getDbDir() + dbName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    /**
     * Connect to a sample database
     *
     * @param dbName the database file name
     */
    public static void createNewDatabase(String dbName) {

        String url = getDbDir() + dbName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Create a new table in the test database
     *
     */
    public static void createNewTable(String dbName, String sqlStatement) {
        // SQLite connection string
        String url = getDbDir() + dbName;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sqlStatement);
        } catch (SQLException e) {
//            System.out.println("CREATE TABLE ERROR: "+e.getMessage());
        }
    }

    /**
     * Get the warehouse whose capacity greater than a specified capacity
     * @param name
     * @return
     */
    public byte[][] getNews(String name){
        String dbName = "news.db";
        String sql = "SELECT id, `name`,`template_a`,`template_b` "
                + "FROM fp WHERE name = ? LIMIT 1";

        try (Connection conn = this.connect(dbName);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,name);
            //
            ResultSet rs  = pstmt.executeQuery();

            byte[] templateA = rs.getBytes("template_a");
            byte[] templateB = rs.getBytes("template_b");

            return new byte[][]{templateA, templateB};
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return new byte[0][];
    }

    public void addNews(String section, String subsection, String title, String abstrac, String url, String byline, String item_type, String image_1, String caption_1, String copyright_1, String published_date){
        String dbName = "news.db";

        File db = new File(dbName);
        if (!db.exists()){
            createNewDatabase(dbName);
        }
        String sql = "CREATE TABLE `newyork` (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`section`\tTEXT NOT NULL,\n" +
                "\t`subsection`\tTEXT NOT NULL,\n" +
                "\t`title`\tTEXT NOT NULL,\n" +
                "\t`abstract`\tTEXT NOT NULL,\n" +
                "\t`url`\tTEXT,\n" +
                "\t`byline`\tTEXT,\n" +
                "\t`item_type`\tTEXT,\n" +
                "\t`image_1`\tTEXT,\n" +
                "\t`caption_1`\tTEXT,\n" +
                "\t`copyright_1`\tTEXT,\n" +
                "\t`published_date`\tTEXT NOT NULL\n" +
                ");";

        createNewTable(dbName, sql);

        String insert = "INSERT INTO `newyork`(`section`,`subsection`,`title`, `abstract`, `url`, `byline`, `item_type`, `image_1`, `caption_1`, `copyright_1`, `published_date`) VALUES (?,?,?,?,?,?,?,?,?,?,?);";

        try (Connection conn = this.connect(dbName);
             PreparedStatement pstmt = conn.prepareStatement(insert)) {
            pstmt.setString(1, section);
            pstmt.setString(2, subsection);
            pstmt.setString(3, title);
            pstmt.setString(4, abstrac);
            pstmt.setString(5, url);
            pstmt.setString(6, byline);
            pstmt.setString(7, item_type);
            pstmt.setString(8, image_1);
            pstmt.setString(9, caption_1);
            pstmt.setString(10, copyright_1);
            pstmt.setString(11, published_date);
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFixtures(int year, String league_name, String country_name, String current, String top_scorers, String injuries){
        String dbName = "fixtures.db";

        File db = new File(dbName);
        if (!db.exists()){
            createNewDatabase(dbName);
        }
        String sql = "CREATE TABLE `fixtures` (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`league_name`\tTEXT NOT NULL,\n" +
                "\t`year`\tTEXT NOT NULL,\n" +
                "\t`country_name`\tTEXT NOT NULL,\n" +
                "\t`current`\tTEXT NOT NULL,\n" +
                "\t`top_scorers`\tTEXT NOT NULL,\n" +
                "\t`injuries`\tTEXT\n" +
                ");";

        createNewTable(dbName, sql);

        String insert = "INSERT INTO `fixtures`(`year`,`league_name`,`country_name`,`current`, `top_scorers`, `injuries`) VALUES (?,?,?,?,?,?);";

        try (Connection conn = this.connect(dbName);
             PreparedStatement pstmt = conn.prepareStatement(insert)) {
            pstmt.setString(1, String.valueOf(year));
            pstmt.setString(2, league_name);
            pstmt.setString(3, country_name);
            pstmt.setString(4, current);
            pstmt.setString(5, top_scorers);
            pstmt.setString(6, injuries);
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
