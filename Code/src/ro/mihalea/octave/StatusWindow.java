package ro.mihalea.octave;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;
import javax.mail.*;

/**
 * Created by Mircea on 29-Sep-14.
 */
public class StatusWindow {
    private JFrame frame = new JFrame();
    private final Dimension WINDOW_SIZE = new Dimension(250, 125);
    private final String TITLE = "Octave";

    private JLabel lb_status;
    private JButton bt_toggle, bt_config;

    private DataPack dataPack;
    private char[] password;

    private Timer timer;
    private boolean status = true;
    private File file;

    private long timePassed = 0;

    public StatusWindow(){
        setupUI();
        setupActions();
        setupTimer();
        initialise();
    }

    private void setupUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints c = new GridBagConstraints();


        lb_status = new JLabel("Stopped");
        bt_toggle = new JButton("N/A");
        bt_config = new JButton("Config");
        toggleButton();

        bt_toggle.setPreferredSize(new Dimension(100, 30));
        bt_config.setPreferredSize(new Dimension(100, 30));

        bt_toggle.setEnabled(false);

        c.anchor = GridBagConstraints.CENTER;
        c.weightx = c.weighty = 1d;

        c.gridx = c.gridy = 0;
        panel.add(bt_toggle, c);
        c.gridx = 1; c.gridy = 0;
        panel.add(bt_config, c);
        c.gridx = 0; c.gridy = 1; c.gridwidth = 2;
        panel.add(lb_status, c);

        frame.setContentPane(panel);
    }

    private void setupActions(){
        bt_toggle.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(status == false && (password == null || password.length <= 0)) {
                    JPanel panel = new JPanel();
                    JLabel label = new JLabel("Enter your password:");
                    JPasswordField pass = new JPasswordField(20);
                    panel.add(label);
                    panel.add(pass);
                    String[] options = new String[]{"OK", "Cancel"};
                    int option = JOptionPane.showOptionDialog(frame, panel, "Password", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                    if(option == 0)
                    {
                        password = pass.getPassword();
                        if(password.length <= 0)
                            return;
                        if (validCredentials() == false)
                            return;
                    } else
                        return;
                }
                toggleTimer();
            }
        });

        bt_config.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataPack = ConfigWindow.showDialog(frame);
                if(dataPack != null) {
                    bt_toggle.setEnabled(true);
                    file = new File(dataPack.folder);
                }
            }
        });
    }

    private void setupTimer(){
        ActionListener action = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                timePassed += 1000;
                updateStatus();

                if(timePassed >= dataPack.refreshRate) {
                    try {
                        timePassed = 0;
                        if (folderSize(file) > dataPack.size) {
                            sendMail(false);
                            toggleTimer();
                            lb_status.setText("Directory has reached the set limit");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        timer = new Timer(1000, action);
    }

    private void initialise(){
        frame.pack();
        frame.setIconImages(Main.icons);
        frame.setTitle(TITLE);
        frame.setSize(WINDOW_SIZE);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - WINDOW_SIZE.width / 2, screenSize.height / 2 - WINDOW_SIZE.height / 2);

        frame.setVisible(true);
    }

    private boolean validCredentials(){
        try {
            sendMail(true);
            System.out.println("Credentials validated successfully");
            return true;
        } catch (AuthenticationFailedException ex){
            System.out.println("Authentication failed");
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            System.out.println("Could not validate credentials");
            ex.printStackTrace();
            return false;
        }
    }

    private void updateStatus(){
        lb_status.setText("Updating in " + (dataPack.refreshRate - timePassed + 1000) / 1000 + " seconds");
    }

    private void toggleTimer(){
        if(timer != null) {
            if (timer.isRunning())
                timer.stop();
            else
                timer.start();
        }

        toggleButton();
    }

    private void toggleButton(){
        status = !status;
        if(status) {
            bt_toggle.setText("STOP");
            bt_toggle.setForeground(Color.RED);
            frame.setTitle(TITLE + " - Running");
        } else {
            bt_toggle.setText("Start");
            bt_toggle.setForeground(Color.BLACK);
            lb_status.setText("Stopped");
            frame.setTitle(TITLE);
            timePassed = 0;
        }
    }

    private long folderSize(File directory){
        long length = 0;
        for (File dir : file.listFiles()){
            if(dir.isFile())
                length += dir.length();
            else
                length += folderSize(dir);
        }
        return length;
    }

    private void sendMail(boolean isEmpty) throws Exception{
        Properties props = new Properties();
        if (dataPack.encryption == 1) //SSL
            props.setProperty("mail.smtp.startssl.enable", "true");
        else if(dataPack.encryption == 2) //TLS
            props.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props);
        Transport transport = session.getTransport("smtp");
        transport.connect(dataPack.host, dataPack.port, dataPack.email, new String(password));

        if(isEmpty == false) {
            String to = dataPack.email;
            String from = dataPack.email;
            String subject = "Octave: Size alert";
            Message message = new MimeMessage(session);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText("Your monitored directory has reached the set file size. \n" +
                "Email sent with Octave by Mihalea Mircea <mircea@mihalea.ro>");

            transport.sendMessage(message, message.getAllRecipients());
        }

        transport.close();
    }
}
