package jdbc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Jdbc_connect {

	public static void main(String[] args) throws ClassNotFoundException, NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Connection con = null;
        Statement stmt = null;
            
	        // SQL statements to create tables
	        String createCustomersTableSQL = "CREATE TABLE customers (" +
	                "cust_no VARCHAR2(10) PRIMARY KEY," +
	                "name VARCHAR2(20)," +
	                "phoneno VARCHAR2(15)," +
	                "city VARCHAR2(20)" +
	                ")";

	        String createAccountsTableSQL = "CREATE TABLE accounts (" +
	                "account_no VARCHAR2(15) PRIMARY KEY," +
	                "cust_no VARCHAR2(10) REFERENCES customers(cust_no)," +
	                "type VARCHAR2(30)," +
	                "balance NUMBER(10, 2)," +
	                "branch_code VARCHAR2(10)," +
	                "branch_name VARCHAR2(30)," +
	                "branch_city VARCHAR2(30)" +
	                ")";

	        try {
	        	 Class.forName("oracle.jdbc.driver.OracleDriver");

	             // Create the connection object
	             String conurl = "jdbc:oracle:thin:@172.17.144.110:1521:ora11g";
	             con = DriverManager.getConnection(conurl, "csite2141010075", "csite2141010075");
	             stmt = con.createStatement();
	        
	            {

	            // Create customers table
	            stmt.execute(createCustomersTableSQL);
	            System.out.println("Customers table created successfully.");

//	            // Create accounts table
	            stmt.execute(createAccountsTableSQL);
	            System.out.println("Accounts table created successfully.");
	            stmt = con.createStatement();
	            int choice_variable = 0;

	            do {
	                System.out.println("\n\n** Banking Management System **");
	                System.out.println("1. Show Customer Records");
	                System.out.println("2. Add Customer Record");
	                System.out.println("3. Delete Customer Record");
	                System.out.println("4. Update Customer Information");
	                System.out.println("5. Show Account Details of a Customer");
	                System.out.println("6. Show Loan Details of a Customer");
	                System.out.println("7. Deposit Money to an Account");
	                System.out.println("8. Withdraw Money from an Account");
	                System.out.println("9. Exit the Program");
	                System.out.println("Enter your choice(1-9):");

	                choice_variable = Integer.parseInt(br.readLine());

	                switch (choice_variable) {
	                    case 1:
	                        showCustomerRecords(stmt);
	                        break;
	                    case 2:
	                        addCustomerRecord(stmt, con);
	                        break;
	                    case 3:
	                        deleteCustomerRecord(stmt, br);
	                        break;
	                    case 4:
	                        updateCustomerInformation(stmt, br);
	                        break;
	                    case 5:
	                        showAccountDetails(stmt, br);
	                        break;
	                    case 6:
	                        showLoanDetails(stmt, br);
	                        break;
	                    case 7:
	                        depositMoney(stmt, br);
	                        break;
	                    case 8:
	                        withdrawMoney(stmt, br);
	                        break;
	                    case 9:
	                        System.out.println("Exiting program...");
	                        break;
	                    default:
	                        System.out.println("Invalid choice. Please enter a number between 1 and 9.");
	                }
	            } while (choice_variable != 9);}}
	        catch (SQLException e) {
	            System.out.println("Error occurred while creating tables: " + e.getMessage());
	        }
	}
	  public static void showCustomerRecords(Statement stmt) throws SQLException {
	        ResultSet rs = stmt.executeQuery("SELECT * FROM customers");
	        while (rs.next()) {
	            System.out.println("Customer Number: " + rs.getString("cust_no"));
	            System.out.println("Name: " + rs.getString("name"));
	            System.out.println("Phone Number: " + rs.getString("phoneno"));
	            System.out.println("City: " + rs.getString("city"));
	            System.out.println("------------------------");
	        }
	    }

	    public static void addCustomerRecord(Statement stmt, Connection con) throws IOException, SQLException {
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        System.out.println("Enter customer number:");
	        String custNo = br.readLine();
	        System.out.println("Enter name:");
	        String name = br.readLine();
	        System.out.println("Enter phone number:");
	        String phoneNo = br.readLine();
	        System.out.println("Enter city:");
	        String city = br.readLine();

	        String query = "INSERT INTO customers (cust_no, name, phoneno, city) VALUES (?, ?, ?, ?)";
	        PreparedStatement pstmt = con.prepareStatement(query);
	        pstmt.setString(1, custNo);
	        pstmt.setString(2, name);
	        pstmt.setString(3, phoneNo);
	        pstmt.setString(4, city);
	        pstmt.executeUpdate();
	        System.out.println("Customer record added successfully.");
	    }

	    public static void deleteCustomerRecord(Statement stmt, BufferedReader br) throws IOException, SQLException {
	        System.out.println("Enter customer number to delete:");
	        String custNo = br.readLine();
	        String query = "DELETE FROM customers WHERE cust_no = '" + custNo + "'";
	        int rowsAffected = stmt.executeUpdate(query);
	        if (rowsAffected > 0)
	            System.out.println("Customer record deleted successfully.");
	        else
	            System.out.println("Customer with given number not found.");
	    }

	    public static void updateCustomerInformation(Statement stmt, BufferedReader br) throws IOException, SQLException {
	        System.out.println("Enter customer number to update:");
	        String custNo = br.readLine();
	        System.out.println("Enter attribute to update (1: Name, 2: Phone Number, 3: City):");
	        int attributeChoice = Integer.parseInt(br.readLine());
	        String attributeName = "";
	        switch (attributeChoice) {
	            case 1:
	                attributeName = "name";
	                break;
	            case 2:
	                attributeName = "phoneno";
	                break;
	            case 3:
	                attributeName = "city";
	                break;
	            default:
	                System.out.println("Invalid attribute choice.");
	                return;
	        }
	        System.out.println("Enter new value:");
	        String newValue = br.readLine();

	        String query = "UPDATE customers SET " + attributeName + " = '" + newValue + "' WHERE cust_no = '" + custNo + "'";
	        int rowsAffected = stmt.executeUpdate(query);
	        if (rowsAffected > 0)
	            System.out.println("Customer information updated successfully.");
	        else
	            System.out.println("Customer with given number not found.");
	    }

	    public static void showAccountDetails(Statement stmt, BufferedReader br) throws IOException, SQLException {
	        System.out.println("Enter customer number:");
	        String custNo = br.readLine();
	        String query = "SELECT * FROM accounts WHERE cust_no = '" + custNo + "'";
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            System.out.println("Account Number: " + rs.getString("account_no"));
	            System.out.println("Type: " + rs.getString("type"));
	            System.out.println("Balance: " + rs.getDouble("balance"));
	            System.out.println("Branch Code: " + rs.getString("branch_code"));
	            System.out.println("Branch Name: " + rs.getString("branch_name"));
	            System.out.println("Branch City: " + rs.getString("branch_city"));
	            System.out.println("------------------------");
	        }
	    }

	    public static void showLoanDetails(Statement stmt, BufferedReader br) throws IOException, SQLException {
	        System.out.println("Enter customer number:");
	        String custNo = br.readLine();
	        String query = "SELECT * FROM loans WHERE cust_no = '" + custNo + "'";
	        ResultSet rs = stmt.executeQuery(query);
	        boolean found = false;
	        while (rs.next()) {
	            found = true;
	            System.out.println("Loan Number: " + rs.getString("loan_no"));
	            System.out.println("Loan Amount: " + rs.getDouble("loan_amount"));
	            System.out.println("Branch Code: " + rs.getString("branch_code"));
	            System.out.println("Branch Name: " + rs.getString("branch_name"));
	            System.out.println("Branch City: " + rs.getString("branch_city"));
	            System.out.println("------------------------");
	        }
	        if (!found)
	            System.out.println("No loan details found for the customer.");
	    }

	    public static void depositMoney(Statement stmt, BufferedReader br) throws IOException, SQLException {
	        System.out.println("Enter account number:");
	        String accountNo = br.readLine();
	        System.out.println("Enter amount to deposit:");
	        double amount = Double.parseDouble(br.readLine());

	        String updateQuery = "UPDATE accounts SET balance = balance + " + amount + " WHERE account_no = '" + accountNo + "'";
	        int rowsAffected = stmt.executeUpdate(updateQuery);
	        if (rowsAffected > 0)
	            System.out.println("Money deposited successfully.");
	        else
	            System.out.println("Account with given number not found.");
	    }

	    public static void withdrawMoney(Statement stmt, BufferedReader br) throws IOException, SQLException {
	        System.out.println("Enter account number:");
	        String accountNo = br.readLine();
	        System.out.println("Enter amount to withdraw:");
	        double amount = Double.parseDouble(br.readLine());

	        String checkQuery = "SELECT balance FROM accounts WHERE account_no = '" + accountNo + "'";
	        ResultSet rs = stmt.executeQuery(checkQuery);
	        if (rs.next()) {
	            double balance = rs.getDouble("balance");
	            if (balance >= amount) {
	                String updateQuery = "UPDATE accounts SET balance = balance - " + amount + " WHERE account_no = '" + accountNo + "'";
	                int rowsAffected = stmt.executeUpdate(updateQuery);
	                if (rowsAffected > 0)
	                    System.out.println("Money withdrawn successfully.");
	                else
	                    System.out.println("Failed to withdraw money.");
	            } else {
	                System.out.println("Insufficient balance.");
	            }
	        } else {
	            System.out.println("Account with given number not found.");
	                  }

}
}
