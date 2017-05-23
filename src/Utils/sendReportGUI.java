package Utils;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.swing.*;

public class SendReportGUI extends JFrame {

    JTextField reportFolder = new HintTextField("Report Folder");
    JTextField recipientName = new HintTextField("To Whom?");
    JTextField subject = new HintTextField("Subject");
    JButton button = new JButton("SEND");

    public SendReportGUI() {
        super("Send The Report");
        setLayout(new FlowLayout());

        initTextField(reportFolder);
        initTextField(recipientName);
        initTextField(subject);
        String[] choices = Arrays.asList(getAllRecipients()).toArray(new String[getAllRecipients().length]);;

        final JComboBox<String> cb = new JComboBox<String>(choices);

        cb.setVisible(true);

        button.addActionListener(event -> {

                    ZipAndPutOnTheServer appZip = new ZipAndPutOnTheServer(reportFolder.getText());
                    String subjectString = subject.getText().replace(" ", "_");
                    String recipientEmail = getRecipient((String) cb.getSelectedItem());

                    System.out.println("Subject - " + subjectString + "\nReport - " + reportFolder.getText() + "\nRecipientEmail - " + recipientEmail);
                    if (!recipientEmail.equals("null") && !subjectString.equals("") && !reportFolder.getText().equals("")) {
                        appZip.generateFileList(new File(reportFolder.getText()));
                        appZip.zipIt(subjectString, reportFolder.getText());

                        try {
                            SendEmail.Send(recipientEmail, "", subjectString, "http://192.168.2.72:8181/logs/" + subjectString + ".zip");
                            JOptionPane.showMessageDialog(
                                    SendReportGUI.this,
                                    "This report:\n" + reportFolder.getText() + "\n" +
                                            "Was sent to:\n" + recipientEmail + "\n" +
                                            "With this subject:\n" + subjectString + "\n");
                            System.exit(0);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("We did not sent anything... You can try again");
                        JOptionPane.showMessageDialog(
                                SendReportGUI.this,
                                "We did not sent anything... You can try again");
                    }

                }
        );

        add(reportFolder);
        //   add(recipientName);
        add(cb);
        add(subject);

        add(button);

        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void initTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 12));
        textField.setForeground(Color.BLUE);
        textField.setBackground(Color.YELLOW);


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SendReportGUI());
    }

    public static String getRecipient(String recipientName) {
        File file = new File("src\\Utils\\emails.properties");
        Properties properties = new Properties();
        try {
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(properties.get(recipientName.toLowerCase()));
    }
    public static Object[] getAllRecipients() {
        File file = new File("src\\Utils\\emails.properties");
        Properties properties = new Properties();
        try {
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object[] arr = properties.keySet().toArray();
        return arr ;
    }
}

class HintTextField extends JTextField implements FocusListener {

    private final String hint;
    private boolean showingHint;

    public HintTextField(final String hint) {
        super(hint, 40);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText("");
            showingHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText(hint);
            showingHint = true;
        }
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }
}