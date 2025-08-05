import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IouGUI implements ActionListener, KeyListener {
    // Variables for the GUI
    private JLabel label;
    private JLabel user;
    private JButton requestButton;
    private JButton manageButton;
    private JFrame frame;
    private JPanel panel;
    private JTextField amountField;
    private JTextField creditorField;
    private JTextField dueDateField;
    private JLabel amountLabel;
    private JLabel creditorLabel;
    private JLabel dueDateLabel;
    private double amount;
    private String creditor;
    private String borrower;
    private String dueDate;
    
    // Constructor for the GUI
    public IouGUI(String username) {
        this.borrower = username;
        frame = new JFrame();
        panel = new JPanel();  
        // boxLayout for vertical alignment
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 100, 50)); // Padding around the panel        
        // label at the top
        label = new JLabel("IOU Manager");
        user = new JLabel("Welcome " + username + " how can we help you?");
        dueDateLabel = new JLabel("When do you think you can pay it back?");
        label.setFont(new Font("Arial", Font.BOLD, 24)); //set font size to 24
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT); //center the label
        user.setFont(new Font("Arial", Font.BOLD, 18)); //set font size to 24
        user.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        dueDateLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);     
        //buttons and their labels
        requestButton = new JButton("Create a new IOU");
        manageButton = new JButton("Manage Existing IOU");
        //resize buttons
        requestButton.setPreferredSize(new Dimension(400, 50));
        manageButton.setPreferredSize(new Dimension(400, 50));
        //align the buttons
        requestButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        manageButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        //action listeners check for buttons being pressed
        requestButton.addActionListener(this);
        manageButton.addActionListener(this);
        //moved this code to the constructor to prevent formating errors between transitions
        amountLabel = new JLabel("Amount:");
        creditorLabel = new JLabel("Creditor's Name:");
        amountField = new JTextField(15);
        creditorField = new JTextField(15);
        dueDateField = new JTextField(15);
        //added KeyListeners to enable button only if text fields are filled
        amountField.addKeyListener(this);
        creditorField.addKeyListener(this);
        //align components
        amountLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        amountField.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        creditorLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        creditorField.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        //components are added to the window panel
        panel.add(label); // Add the label first
        panel.add(Box.createVerticalStrut(40));
        panel.add(user);
        panel.add(Box.createVerticalStrut(20)); // Vertical space between label and buttons
        panel.add(amountLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(amountField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(creditorLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(creditorField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(dueDateLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(dueDateField);
        panel.add(Box.createVerticalStrut(10));
        //hide components initially
        amountLabel.setVisible(false);
        amountField.setVisible(false);
        creditorLabel.setVisible(false);
        creditorField.setVisible(false);
        dueDateLabel.setVisible(false);
        dueDateField.setVisible(false);
        //buttons are visible
        panel.add(requestButton); // Add request button
        panel.add(Box.createVerticalStrut(15)); // Vertical space between buttons
        panel.add(manageButton); // Add manage button
        //set up the windows frame
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle("Basic Banking");
        frame.pack();
        frame.setVisible(true);
        //mouseListener to transfer focus away from a text box on panel click
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.requestFocusInWindow();
            }
        });
    }
    //function to return the original layout typically used by the back button
    public void back() {
		label.setText("IOU Manager");
     	requestButton.setText("Create a new IOU");
        requestButton.setVisible(true);
        manageButton.setText("Manage Existing IOU");
        manageButton.setVisible(true);
	    amountLabel.setVisible(false);
	    amountField.setVisible(false);
	    creditorLabel.setVisible(false);
	    creditorField.setVisible(false);
	    dueDateLabel.setVisible(false);
	    dueDateField.setVisible(false);
	    amountField.setText("");
	    creditorField.setText("");
	    dueDateField.setText(""); 
    }   
    //this listens for a click on a button
    public void actionPerformed(ActionEvent e) {
    	//this handles the top button
        if (e.getSource() == requestButton) {
        	//depending on the buttons text it will do certain things in the GUI
            if (requestButton.getText().equals("Create a new IOU")) {
                label.setText("Request Money");
                requestButton.setText("Send Request");
                manageButton.setText("Back"); 
                //add components in the correct order (below buttons)
                amountLabel.setVisible(true);
                amountField.setVisible(true);
                creditorLabel.setVisible(true);
                creditorField.setVisible(true);
                dueDateLabel.setVisible(true);
                dueDateField.setVisible(true);
                //add text fields and labels for "Amount" and "Creditor" if they are not there
                if (amountField == null && creditorField == null) {
                	requestButton.setEnabled(false); //here we have disabled the request button
                    //refresh layout
                    panel.revalidate();
                    panel.repaint();
                }
            } else if (requestButton.getText().equals("Send Request")) {
                //save values when "Send Request" is pressed
                amount = Double.parseDouble(amountField.getText().trim());
                creditor = creditorField.getText().trim(); 
                dueDate = dueDateField.getText().trim();
                //update label and buttons for confirmation
                label.setText("Confirm IOU:");
                requestButton.setText("Confirm");
                manageButton.setVisible(false); //hide the manage button
                //display the amount and creditor for confirmation
                amountLabel.setText("Amount: $" + amount);
                creditorLabel.setText("Creditor: " + creditor);
                //remove text fields since we're showing confirmed values
                amountLabel.setVisible(true);
                creditorLabel.setVisible(true);
			    amountField.setVisible(false);
			    creditorField.setVisible(false);
			    dueDateLabel.setVisible(false);
			    dueDateField.setVisible(false);
                amountField.setText("");
                creditorField.setText("");
                dueDateField.setText("");
            } else if (requestButton.getText().equals("Confirm")) {
            	//call the createIou class
                createIou createIou = new createIou();
                createIou.addIou(amount, creditor, borrower, dueDate);
                //final confirmation scenario            	
            	amountLabel.setVisible(true);
                creditorLabel.setVisible(true);
                label.setText("Thank you!");
                amountLabel.setText("Request Sent");
                creditorLabel.setText(creditor + " has been informed.");              
                requestButton.setText("Return to IOU Manager");
                manageButton.setVisible(false);
            }else if (requestButton.getText().equals("Return to IOU Manager")) {
                //go back to the initial state
            	back();
            }       
        }
        //manage button functionality
        	if (e.getSource() == manageButton) {
        		if (manageButton.getText().equals("Manage Existing IOU")) {
        			label.setText("Current IOU(s)");
        			if (amount != 0.0 && creditor != null) {        			
        				requestButton.setText(creditor + " $" + amount);
        	            manageButton.setText("Back");
        			}
        			else {
        				user.setText("No current IOU(s) found.");
        				requestButton.setVisible(false);
        				manageButton.setText("Back");
        			}
        		}
        		else if (manageButton.getText().equals("Back")) {
        			back(borrower);
                }        		    	
        }
    }  
    public void back(String borrower) {
    	back();
	    user.setText("Welcome " + borrower + " how can we help you?");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //enable "Send Request" only when both fields are filled
        if (!amountField.getText().trim().isEmpty() && !creditorField.getText().trim().isEmpty()) {
            requestButton.setEnabled(true);
        } else {
            requestButton.setEnabled(false);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        //not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //not used
    }
}
