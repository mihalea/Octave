package ro.mihalea.octave;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by Mircea on 29-Sep-14.
 */
public class ConfigWindow {
    private JDialog dialog = new JDialog();
    private final Dimension WINDOW_SIZE = new Dimension(320, 325);
    private final String TITLE = "Octave - Config";

    private JLabel lb_folder, lb_email, lb_host, lb_port, lb_encryption, lb_refreshRate, lb_size;
    private JTextField tf_folder, tf_email, tf_host, tf_port, tf_refreshRate, tf_size;
    private JComboBox<String> cb_encryption, cb_size;
    private JButton bt_ok, bt_cancel, bt_choose;

    private Color errorColor = new Color(240, 128, 128);

    private DataPack dataPack;

    private ConfigWindow(JFrame parent){
        dialog = new JDialog(parent);
        setupUI();
        setupActions();
        initialise();
    }

    private void setupUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints c = new GridBagConstraints();

        lb_folder = new JLabel("Folder:");
        lb_email = new JLabel("Email:");
        lb_host = new JLabel("Host:");
        lb_port = new JLabel("Port:");
        lb_encryption = new JLabel("Encryption:");
        lb_refreshRate = new JLabel("Refresh Rate:");
        lb_size = new JLabel("Size:");

        tf_folder = new JTextField();
        tf_email = new JTextField();
        tf_host = new JTextField();
        tf_port = new JTextField();
        tf_refreshRate = new JTextField();
        tf_size = new JTextField();

        cb_encryption = new JComboBox<String>(new String[]{"None", "SSL", "TLS"});
        cb_size = new JComboBox<String>(new String[]{"Gigabytes", "Megabytes", "Kilobytes", "Bytes"});

        bt_ok = new JButton("OK");
        bt_cancel = new JButton("Cancel");
        bt_choose = new JButton("Choose");


        tf_folder.setPreferredSize(new Dimension(100, 25));
        tf_email.setPreferredSize(new Dimension(200, 25));
        tf_host.setPreferredSize(new Dimension(200, 25));
        tf_port.setPreferredSize(new Dimension(200, 25));
        tf_refreshRate.setPreferredSize(new Dimension(200, 25));
        tf_size.setPreferredSize(new Dimension(100, 25));
        cb_encryption.setPreferredSize(new Dimension(200, 25));
        bt_choose.setPreferredSize(new Dimension(90, 25));
        cb_size.setPreferredSize(new Dimension(90, 25));

        bt_ok.setPreferredSize(new Dimension(75, 30));
        bt_cancel.setPreferredSize(new Dimension(75, 30));


        c.weightx = 0.1d;
        c.weighty = 1d;
        c.insets = new Insets(0, 0, 0, 5);

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0; c.gridy = 0;
        panel.add(lb_folder, c);
        c.gridx = 0; c.gridy = 1;
        panel.add(lb_email, c);
        c.gridx = 0; c.gridy = 2;
        panel.add(lb_host, c);
        c.gridx = 0; c.gridy = 3;
        panel.add(lb_port, c);
        c.gridx = 0; c.gridy = 4;
        panel.add(lb_encryption, c);
        c.gridx = 0; c.gridy = 5;
        panel.add(lb_refreshRate, c);
        c.gridx = 0; c.gridy = 6;
        panel.add(lb_size, c);


        c.weightx = 0.9d;
        c.weighty = 1d;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);

        c.gridx = 1; c.gridy = 0;
        panel.add(tf_folder, c);
        c.gridx = 1; c.gridy = 1; c.gridwidth = 2;
        panel.add(tf_email, c);
        c.gridx = 1; c.gridy = 2;
        panel.add(tf_host, c);
        c.gridx = 1; c.gridy = 3;
        panel.add(tf_port, c);
        c.gridx = 1; c.gridy = 4;
        panel.add(cb_encryption, c);
        c.gridx = 1; c.gridy = 5;
        panel.add(tf_refreshRate, c);
        c.gridx = 1; c.gridy = 6; c.gridwidth = 1;
        panel.add(tf_size, c);


        c.gridx = 2; c.gridy = 0; c.gridwidth = 1;
        panel.add(bt_choose, c);
        c.gridx = 2; c.gridy = 6;
        panel.add(cb_size, c);


        JPanel flowPanel = new JPanel();
        flowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        flowPanel.add(bt_ok);
        flowPanel.add(bt_cancel);

        c.gridx = 0; c.gridy = 7;
        c.gridwidth = 3; c.weightx = 1d;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(flowPanel, c);

        dialog.setContentPane(panel);
    }

    private void setupActions(){
        bt_ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String folder   = tf_folder.getText();
                    String email    = tf_email.getText();
                    String host     = tf_host.getText();
                    String port     = tf_port.getText();
                    String refresh  = tf_refreshRate.getText();
                    String size     = tf_size.getText();

                    int nPort = 587, encryption = cb_encryption.getSelectedIndex();
                    long nSize = 1000, nRefresh = 300;

                    boolean isValidated = true;
                    if(new File(folder).isDirectory()){
                        tf_folder.setBackground(Color.WHITE);
                    } else {
                        tf_folder.setBackground(errorColor); //Light coral(red)
                        isValidated = false;
                    }

                    if(email.contains("@"))
                        tf_email.setBackground(Color.WHITE);
                    else {
                        tf_email.setBackground(errorColor);
                        isValidated = false;
                    }

                    if(host.contains("."))
                        tf_host.setBackground(Color.WHITE);
                    else {
                        tf_host.setBackground(errorColor);
                        isValidated = false;
                    }

                    try {
                        nPort = Integer.parseInt(port);
                        tf_port.setBackground(Color.WHITE);
                    } catch (Exception ex){
                        tf_port.setBackground(errorColor);
                        isValidated = false;
                    }

                    try {
                        nRefresh = Integer.parseInt(refresh);
                        nRefresh *= 1000;
                        tf_refreshRate.setBackground(Color.WHITE);
                    } catch (Exception ex) {
                        tf_refreshRate.setBackground(errorColor);
                        isValidated = false;
                    }

                    try {
                        nSize = Long.parseLong(size);
                        tf_size.setBackground(Color.WHITE);

                        switch (cb_size.getSelectedIndex()){
                            case 0: //GIGA
                                nSize = nSize * 1024;
                            case 1: //MEGA
                                nSize = nSize * 1024;
                            case 2: //KILO
                                nSize = nSize * 1024;
                            default:
                                break;
                        }
                    } catch (Exception ex) {
                        tf_port.setBackground(errorColor);
                        isValidated = false;
                    }


                    if(isValidated == false){
                        System.out.println("Config NOT validated");
                        return;
                    }
                    System.out.println("Config validated");

                    dataPack = new DataPack(folder, email, host, nPort, encryption, nRefresh, nSize);
                    ConfigWindow.this.dialog.dispose();
                } catch (Exception ex){

                }
            }
        });

        bt_cancel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataPack = null;
                ConfigWindow.this.dialog.dispose();
            }
        });

        bt_choose.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                fileChooser.showOpenDialog(dialog);
                File file = fileChooser.getSelectedFile();
                if(file == null)
                    return;

                String path = fileChooser.getSelectedFile().getAbsolutePath();
                tf_folder.setText(path);
            }
        });
    }

    private void initialise(){
        dialog.pack();
        dialog.setIconImages(Main.icons);
        dialog.setModal(true);
        dialog.setTitle(TITLE);
        dialog.setSize(WINDOW_SIZE);
        dialog.setMinimumSize(WINDOW_SIZE);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(screenSize.width / 2 - WINDOW_SIZE.width / 2, screenSize.height / 2 - WINDOW_SIZE.height / 2);

        dialog.setVisible(true);
    }

    public static DataPack showDialog(JFrame parent){
        ConfigWindow window = new ConfigWindow(parent);
        return window.dataPack;
    }
}
