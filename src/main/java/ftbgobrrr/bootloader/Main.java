package ftbgobrrr.bootloader;

import launchit.Launchit;
import launchit.LaunchitConfig;
import launchit.launcher.LauncherFile;
import launchit.launcher.events.ILauncherHandler;
import launchit.utils.FilesUtils;
import launchit.utils.OperatingSystem;
import launchit.utils.UrlUtils;

import javax.swing.*;

import ftbgobrrr.bootloader.components.ProgressBar;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main extends JFrame implements ILauncherHandler {

    private final ProgressBar progress;
    private final JLabel label;
    private Launchit it;

    public Main() {
        this.setIconImage(new ImageIcon(getClass().getResource("/images/icon.png")).getImage());
        this.setTitle("BTB go brrrrr - Bootloader");
        this.setSize(440, 200);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setBackground(new Color(1.0f,1.0f,1.0f,1));
        Font dumbledor = setupFont();

        Image img = new ImageIcon(getClass().getResource("/images/bg.png")).getImage();

        JPanel bgPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                super.paintComponent(g);
            }
        };

        bgPanel.setSize(this.getSize());
        bgPanel.setLayout(null);
        bgPanel.setOpaque(false);

        label = new JLabel("Verification des mises a jour");
        label.setBounds(15, this.getHeight() - 30 - 30, this.getWidth() - 15 - 15, 20);
        label.setFont(dumbledor.deriveFont(25f));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(JLabel.CENTER);
        bgPanel.add(label);

        progress = new ProgressBar();
        progress.setBounds(30, this.getHeight() - 30, this.getWidth() - 60, 4);
        progress.setBackground(new Color(52, 52, 52, 255));
        progress.setForeground(new Color(231, 67, 70));
        bgPanel.add(progress);

        this.add(bgPanel);
        this.setVisible(true);
        verify();
    }

    public Font setupFont() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font dumbledor;
        try {
            dumbledor = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/marcellus.ttf")).deriveFont(22f);
            ge.registerFont(dumbledor);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("arial", Font.PLAIN, 12);
        }
        return dumbledor;
    }

    public void verify() {
        try {
            it = new LaunchitConfig()
                    .setManifestUrl("http://localhost:3000/manifest")
                    .setInstallFolder(FilesUtils.getInstallDir(".ftbgobrrrrr"))
                    .create();

            boolean isNet = UrlUtils.netIsAvailable(it);
            if (!isNet) {
                if (it.getLauncherManager().getLauncherFile().exists()) {
                    launch();
                    return;
                }
                JOptionPane.showMessageDialog(null, "Aucune connection internet", "FTB GO BRRRRR Bootloader Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                return;
            }

            it.getLauncherManager().setiLauncherHandler(this);
            it.getLauncherManager().checkForUpdate(LauncherFile.Type.LAUNCHER, it.getLauncherManager().getLauncherFile());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void launch() {
        try {
            File thisFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            java.util.List<String> arguments = new ArrayList<>();
            arguments.add(OperatingSystem.getCurrentPlatform().getJavaDir());
            arguments.add("-cp");
            arguments.add(it.getLauncherManager().getLauncherFile().getCanonicalPath());
            arguments.add("ftbgobrrr.launcher.Launcher");
            arguments.add(thisFile.getAbsolutePath());
            ProcessBuilder processBuilder = new ProcessBuilder();
            String.join(" ", arguments);
            processBuilder.command(arguments);
            processBuilder.redirectErrorStream(true);
            processBuilder.start();
            this.dispose();
            System.exit(0);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new Main();
    }

    @Override
    public void endChecking(LauncherFile file, boolean needUpdate) {
        if (needUpdate) {
            it.getLauncherManager().update(file, it.getLauncherManager().getLauncherFile());
        } else launch();
    }

    @Override
    public void startUpdate(LauncherFile file) {
        label.setText("Telechargement de la mise a jour");
    }

    @Override
    public void updateProgress(LauncherFile file, int current, int total) {
        progress.setValue(((double)current * 100) / (double)total);
    }

    @Override
    public void updateFinished(LauncherFile file, boolean error) {
        if (!error)
            launch();
    }
}
