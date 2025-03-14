import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RiverBasinManagement {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/river_basin_management";
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASS = "Akash@2005"; // replace with your MySQL password

    public static void main(String[] args) {
        JFrame frame = new JFrame("River Basin Management");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel label = new JLabel("Enter River Basin Name:");
        label.setBounds(50, 30, 200, 30);
        frame.add(label);

        JTextField basinNameField = new JTextField();
        basinNameField.setBounds(250, 30, 200, 30);
        frame.add(basinNameField);

        JTextArea outputArea = new JTextArea();
        outputArea.setBounds(50, 100, 500, 200);
        outputArea.setEditable(false);
        frame.add(outputArea);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(250, 70, 100, 30);
        frame.add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String basinName = basinNameField.getText();
                searchRiverBasin(basinName, outputArea);
            }
        });

        frame.setVisible(true);
    }

    private static void searchRiverBasin(String basinName, JTextArea outputArea) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM river_basin_management WHERE name = ?")) {

            pstmt.setString(1, basinName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double phValue = rs.getDouble("ph_level"); // Corrected column name
                double drainageDensity = rs.getDouble("drainage_density");
                String waterQuality = rs.getString("water_quality");
                double area = rs.getDouble("area");
                String tributaries = rs.getString("tributaries");
                String boundaries = rs.getString("boundary_coordinates");

                String result = String.format("River Basin: %s\npH Level: %.2f\nDrainage Density: %.2f\nWater Quality: %s\nArea: %.2f sq.km\nTributaries: %s\nBoundaries: %s",
                        name, phValue, drainageDensity, waterQuality, area, tributaries, boundaries);
                outputArea.setText(result);
            } else {
                outputArea.setText("No data found for the specified river basin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.setText("Error connecting to the database.");
        }
    }
}
