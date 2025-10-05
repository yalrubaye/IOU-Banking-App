import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class accountGUI implements ActionListener, KeyListener {
    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private JLabel transactionLabel;
    private JTextField transactionField;
    private JLabel depositLabel;
    private JTextField depositField;
    private JButton depositButton;
    private JButton iouButton;
    private JButton backButton;
    String transaction;
    String recipient;
    double balance;
    double deposit;
    double ioupayment;
    boolean isValidAmount;
    int user_id;
    String user;
    public accountGUI(String username, int userId) throws SQLException {
    	try (Connection connection = bbaDatabase.getConnection()) {
            //first, get the borrower_id using the borrower name (assuming the borrower name is unique)
    		String balanceQuery = "SELECT balance FROM users WHERE username = ?";
    		balance = -1; //default value if balance is not found

    		try (PreparedStatement stmt = connection.prepareStatement(balanceQuery)) {
    		    stmt.setString(1, username); //set the borrower name to get the user_id
    		    try (ResultSet resultSet = stmt.executeQuery()) {
    		        if (resultSet.next()) {
    		            balance = resultSet.getDouble("balance");
    		        }
    		    }
    		}
            if (balance == -1) {
                System.out.println("Null balance found.");
                return;  //return if borrower doesn't exist
            }
    	}
    	this.user_id = userId;
    	this.user = username;
        frame = new JFrame();
        panel = new JPanel();
        //set up BoxLayout for vertical alignment in the main panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 100, 50));        
        //title label at the top
        label = new JLabel("Transaction Menu");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);      
        //transaction label and field
        transactionLabel = new JLabel("Today is a great day for banking " + username + " how can we help?");
        transactionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        transactionField = new JTextField(15);
        transactionField.setAlignmentX(JTextField.CENTER_ALIGNMENT);     
        //deposit label and field
        depositLabel = new JLabel("Your current balacne is: " + balance + "$");
        depositLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        depositField = new JTextField(15);
        depositField.setAlignmentX(JTextField.CENTER_ALIGNMENT);       
        //buttons for deposit, withdrawal, and back
        depositButton = new JButton("Deposit");
        iouButton = new JButton("Pay an IOU");
        backButton = new JButton("Back");        
        //panel to hold deposit and withdrawal buttons side by side
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));       
        //align and add the buttons to the horizontal panel
        depositButton.setPreferredSize(new Dimension(200, 50));
        iouButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(depositButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Spacing between buttons
        buttonPanel.add(iouButton);        
        //center-align the horizontal panel and back button
        buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        backButton.setAlignmentX(JButton.CENTER_ALIGNMENT);        
        //add ActionListeners and KeyListeners to buttons and fields
        depositButton.addActionListener(this);
        iouButton.addActionListener(this);
        backButton.addActionListener(this);
        transactionField.addKeyListener(this);  //add KeyListener to transactionField
        depositField.addKeyListener(this);      //add KeyListener to depositField if needed       
        //add components to the main panel
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));
        panel.add(transactionLabel);
        panel.add(transactionField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(depositLabel);
        panel.add(depositField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel); // add the button panel with deposit and withdrawal
        panel.add(Box.createVerticalStrut(5));
        panel.add(backButton);        
        //set up the frame
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle("Transaction Menu");
        frame.pack();
        frame.setVisible(true);
        transactionField.setVisible(false);
        depositField.setVisible(false);
        backButton.setVisible(false);               
        //mouseListener to remove focus from text fields on panel click
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.requestFocusInWindow();
            }
        });
    }
    //function to return GUI to original state 
    private void resetToDefaultState() {
        transactionLabel.setText("Today is a great day for banking " + user + " How can we help?");
        transactionField.setVisible(false);
        depositLabel.setVisible(true);
        depositLabel.setText("Your current balacne is: " + balance + "$");
        depositField.setVisible(false);
        depositButton.setText("Deposit");
        iouButton.setText("Pay an IOU");
        backButton.setVisible(false);
        depositButton.setVisible(true);
        iouButton.setVisible(true);
        depositButton.setEnabled(true);
        transactionField.setText("");
        depositField.setText("");
    }
    // function to update the users balance
    public void updateBalance(int user_id, double balance) {
        String sql = "UPDATE users SET balance = ? WHERE user_id = ?";
        try (Connection conn = bbaDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the balance and user_id in the SQL statement
            pstmt.setDouble(1, balance); // Set the new balance
            pstmt.setInt(2, user_id);   

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Balance updated successfully.");
            } else {
                System.out.println("No user found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //implement the ActionListener method for button actions
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == depositButton) {
            if (depositButton.getText().equals("Deposit")) {
                //update the label and button text for deposit mode
                transactionLabel.setText("How much would you like to deposit?");
                transactionField.setVisible(true);  // Show the transaction field for user input
                depositLabel.setVisible(false);     // Hide the deposit label
                depositButton.setText("Confirm");   // Change button text to Confirm
                depositButton.setEnabled(false);
                iouButton.setText("Back");   // Change Withdraw button to act as Back
                backButton.setVisible(false);       // Hide the original Back button          
            } else if (depositButton.getText().equals("Confirm")) {
            	//capture the deposit amount and display confirmation
            	if (transactionLabel.getText().equals("How much would you like to deposit?")) {
            		try {
            			//parse the text to a double and round to two decimal places
            			double depositAmount = Double.parseDouble(transactionField.getText());
            			depositAmount = Math.round(depositAmount * 100.0) / 100.0;
            			balance = balance + depositAmount;
            			updateBalance(user_id, balance);
            			//display the confirmation message
            			transactionLabel.setText("Thank you, your deposit of $" + depositAmount + " has been confirmed.");
            			//hide input fields and buttons for the confirmation screen
            			transactionField.setVisible(false);
            			depositButton.setVisible(false);
            			iouButton.setVisible(false);
            			backButton.setVisible(true); //show the back button to return to the menu
            		} catch (NumberFormatException ex) {
                    //handle the case where the input is not a valid number
                    transactionLabel.setText("Invalid input. Please enter a valid number.");
            		}
            	}
            	else if(transactionField.getText().equals("How much would you like to transfer?")){
            		//TRASACTION TO DATABASE CHECK FUNCTION GOES HERE
            	}
            }       
        } else if (e.getSource() == iouButton) {
        	if (iouButton.getText().equals("Pay an IOU")) {
                //update for IOU payment mode
                transactionLabel.setText("How much would you like to transfer?");
                transactionField.setVisible(true); //show the transaction field for amount entry                
                depositLabel.setText("Who do you want to transfer money to?");
                depositLabel.setVisible(true); //show the deposit label as recipient prompt               
                depositField.setVisible(true); //show deposit field to enter recipient's name              
                depositButton.setText("Confirm"); //set Deposit button text to "Confirm"
                depositButton.setEnabled(false);
                iouButton.setText("Back");   //set Withdrawal button text to "Back"
                backButton.setVisible(false);       //hide the original Back button
            } else if (iouButton.getText().equals("Back")) {
                //return to the original state
                resetToDefaultState();
            }
        } else if (e.getSource() == backButton) {
        	resetToDefaultState();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        //check if the depositButton should be enabled when in deposit mode
        if (depositButton.getText().equals("Confirm")) {
            transaction = transactionField.getText().trim();
            recipient = depositField.getText().trim();
            if (transactionLabel.getText().equals("How much would you like to deposit?")) {
                //check if the input is a valid number with up to two decimal places
                if (transaction.matches("^\\d+(\\.\\d{1,2})?$")) {
                    depositButton.setEnabled(true);  //enable button if valid
                } else {
                    depositButton.setEnabled(false); //disable button if invalid
                }
            } else if (transactionLabel.getText().equals("How much would you like to transfer?")) {
                //ensure both fields (amount and recipient) are filled in
                if (!transaction.isEmpty() && !recipient.isEmpty() &&
                        transaction.matches("^\\d+(\\.\\d{1,2})?$")) {
                    depositButton.setEnabled(true);  //enable if both fields are filled and valid
                } else {
                    depositButton.setEnabled(false);
                }
            }
        }
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
