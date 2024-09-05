package project_alpha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import project_alpha.Gui0.TicketReservationSystem;
import project_alpha.Gui1.MuseumGUI1;
import project_alpha.Gui2.MuseumGUI2;

public class Gui3 {

    public static class MuseumGUI3 extends JFrame {

        public MuseumGUI3() {
            setTitle("Museum Administration");
            setSize(400, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel adminLabel = new JLabel("Admin");
            adminLabel.setHorizontalAlignment(SwingConstants.CENTER);
            adminLabel.setFont(new Font("Arial", Font.BOLD, 20));
            adminLabel.setForeground(Color.RED);

            JButton addTicketButton = new JButton("Sell Ticket");
            JButton manageItemsButton = new JButton("Manage Exhibitions");
            JButton reserveTicketButton = new JButton("Reserve Ticket");
            JButton createTicketButton = new JButton("Create Ticket");

            JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
            buttonPanel.add(addTicketButton);
            buttonPanel.add(manageItemsButton);
            buttonPanel.add(reserveTicketButton);
            buttonPanel.add(createTicketButton);

            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.add(buttonPanel);

            setLayout(new BorderLayout());
            add(adminLabel, BorderLayout.NORTH);
            add(centerPanel, BorderLayout.CENTER);

            addTicketButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showLoadingDialog(() -> {
                        MuseumGUI1 museumGUI1 = new MuseumGUI1();
                        museumGUI1.setVisible(true);
                    });
                }
            });

            manageItemsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showLoadingDialog(() -> {
                        MuseumGUI2 museumGUI2 = new MuseumGUI2();
                        museumGUI2.setVisible(true);
                    });
                }
            });

            reserveTicketButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showLoadingDialog(() -> {
                        TicketReservationSystem ticketReservationSystem = new TicketReservationSystem();
                        ticketReservationSystem.setVisible(true);
                    });
                }
            });

            createTicketButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showLoadingDialog(() -> {
                        Gui4.MuseumGUI4 museumGUI4 = new Gui4.MuseumGUI4();
                        museumGUI4.setVisible(true);
                    });
                }
            });
        }

        //Threads loading :D It worksssssssss !!!!!!!!!!!!!!!!!!!!!!!
        private void showLoadingDialog(Runnable onComplete) {
            JDialog loadingDialog = new JDialog(this, "Loading", true);
            JLabel loadingLabel = new JLabel("Loading");
            loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
            loadingDialog.add(loadingLabel);
            loadingDialog.setSize(200, 100);
            loadingDialog.setLocationRelativeTo(this);

            SwingWorker<Void, String> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    String[] dots = {"Loading", "Loading.", "Loading..", "Loading..."};
                    for (int i = 0; i < 20; i++) {
                        publish(dots[i % dots.length]);
                        Thread.sleep(250);
                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<String> chunks) {
                    loadingLabel.setText(chunks.get(chunks.size() - 1));
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();
                    onComplete.run();
                }
            };

            worker.execute();
            loadingDialog.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MuseumGUI3 museumGUI3 = new MuseumGUI3();
            museumGUI3.setVisible(true);
        });
    }
}
