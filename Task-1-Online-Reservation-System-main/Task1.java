import java.sql.*;
import java.util.Scanner;
import java.util.Random;

public class Task1 {
    private static final int min = 1000;
    private static final int max = 9999;

    public static class User {
        private String username;
        private String password;

        Scanner sc = new Scanner(System.in);

        public User() {
        }

        public String getUsername() {
            System.out.println("Enter Username: ");
            username = sc.nextLine();
            return username;
        }

        public String getPassword() {
            System.out.println("Enter Password: ");
            password = sc.nextLine();
            return password;
        }
    }

    public static class PnrRecord {
        private int pnrNumber;
        private String passengerName;
        private String trainNumber;
        private String classType;
        private String journeyDate;
        private String from;
        private String to;

        Scanner sc = new Scanner(System.in);

        public int getPnrNumber() {
            Random random = new Random();
            pnrNumber = random.nextInt((max - min) + 1) + min;
            return pnrNumber;
        }

        public String getPassengerName() {
            System.out.println("Enter the passenger name: ");
            passengerName = sc.nextLine();
            return passengerName;
        }

        public String getTrainNumber() {
            System.out.println("Enter the train number: ");
            trainNumber = sc.nextLine();
            return trainNumber;
        }

        public String getClassType() {
            System.out.println("Enter the class type: ");
            classType = sc.nextLine();
            return classType;
        }

        public String getJourneyDate() {
            System.out.println("Enter the Journey date as 'YYYY-MM-DD' format: ");
            journeyDate = sc.nextLine();
            return journeyDate;
        }

        public String getFrom() {
            System.out.println("Enter the starting place: ");
            from = sc.nextLine();
            return from;
        }

        public String getTo() {
            System.out.println("Enter the destination place: ");
            to = sc.nextLine();
            return to;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User u1 = new User();
        String username = u1.getUsername();
        String password = u1.getPassword();

        String url = "jdbc:mysql://localhost:3306/reservation";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println("User Connection Granted.\n");
                while (true) {
                    String insertQuery = "INSERT INTO reservations VALUES (?, ?, ?, ?, ?, ?, ?)";
                    String deleteQuery = "DELETE FROM reservations WHERE pnr_number = ?";
                    String showQuery = "SELECT * FROM reservations";

                    System.out.println("Enter the choice: ");
                    System.out.println("1. Insert Record.");
                    System.out.println("2. Delete Record.");
                    System.out.println("3. Show All Records.");
                    System.out.println("4. Exit.");
                    int choice = sc.nextInt();
                    sc.nextLine();  // Consume the newline

                    if (choice == 1) {
                        PnrRecord p1 = new PnrRecord();
                        int pnrNumber = p1.getPnrNumber();
                        String passengerName = p1.getPassengerName();
                        String trainNumber = p1.getTrainNumber();
                        String classType = p1.getClassType();
                        String journeyDate = p1.getJourneyDate();
                        String from = p1.getFrom();
                        String to = p1.getTo();

                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                            preparedStatement.setInt(1, pnrNumber);
                            preparedStatement.setString(2, passengerName);
                            preparedStatement.setString(3, trainNumber);
                            preparedStatement.setString(4, classType);
                            preparedStatement.setString(5, journeyDate);
                            preparedStatement.setString(6, from);
                            preparedStatement.setString(7, to);

                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Record added successfully.");
                            } else {
                                System.out.println("No records were added.");
                            }
                        } catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }

                    } else if (choice == 2) {
                        System.out.println("Enter the PNR number to delete the record: ");
                        int pnrNumber = sc.nextInt();
                        sc.nextLine(); 
                        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                            preparedStatement.setInt(1, pnrNumber);
                            int rowsAffected = preparedStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("Record deleted successfully.");
                            } else {
                                System.out.println("No records were deleted.");
                            }
                        } catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }
                    } else if (choice == 3) {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(showQuery);
                             ResultSet resultSet = preparedStatement.executeQuery()) {
                            System.out.println("\nAll records printing.\n");
                            while (resultSet.next()) {
                                String pnrNumber = resultSet.getString("pnr_number");
                                String passengerName = resultSet.getString("passenger_name");
                                String trainNumber = resultSet.getString("train_number");
                                String classType = resultSet.getString("class_type");
                                String journeyDate = resultSet.getString("journey_date");
                                String fromLocation = resultSet.getString("from_location");
                                String toLocation = resultSet.getString("to_location");

                                System.out.println("PNR Number: " + pnrNumber);
                                System.out.println("Passenger Name: " + passengerName);
                                System.out.println("Train Number: " + trainNumber);
                                System.out.println("Class Type: " + classType);
                                System.out.println("Journey Date: " + journeyDate);
                                System.out.println("From Location: " + fromLocation);
                                System.out.println("To Location: " + toLocation);
                                System.out.println("--------------");
                            }
                        } catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }
                    } else if (choice == 4) {
                        System.out.println("Exiting the program.");
                        break;
                    } else {
                        System.out.println("Invalid Choice Entered.");
                    }
                }

            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading JDBC driver: " + e.getMessage());
        }

        sc.close();
    }
}
