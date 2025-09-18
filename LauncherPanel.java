package fr.kphoenix.minarium.launcher;

import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedProgressBar;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class LauncherPanel extends JPanel implements SwingerEventListener {
   private static Image background = Swinger.getResource("launcher_crack.png");
   private static final Saver SAVER = LauncherFrame.getSaver();
   private static final JTextField USERNAME_FIELD;
   private static final SColoredButton PLAY_BUTTON;
   private static final SColoredButton EXIT_BUTTON;
   private static final SColoredButton HIDE_BUTTON;
   private static final SColoredButton RAM_BUTTON;
   private static final SColoredButton DISCORD_BUTTON;
   private static final STexturedProgressBar progressBar;

   public LauncherPanel() {
      this.setBackground(Swinger.getTransparentInstance(Color.white, 0));
      this.setLayout((LayoutManager)null);
      USERNAME_FIELD.setForeground(Color.BLACK);
      USERNAME_FIELD.setFont(USERNAME_FIELD.getFont().deriveFont(20.0F));
      USERNAME_FIELD.setCaretColor(Color.BLACK);
      USERNAME_FIELD.setOpaque(false);
      USERNAME_FIELD.setBorder((Border)null);
      USERNAME_FIELD.setBounds(377, 409, 428, 40);
      this.add(USERNAME_FIELD);
      PLAY_BUTTON.setBounds(139, 241, 682, 140);
      PLAY_BUTTON.setOpaque(false);
      PLAY_BUTTON.setBorder((Border)null);
      PLAY_BUTTON.addEventListener(this);
      this.add(PLAY_BUTTON);
      EXIT_BUTTON.setBounds(913, 10, 50, 52);
      EXIT_BUTTON.addEventListener(this);
      this.add(EXIT_BUTTON);
      HIDE_BUTTON.setBounds(840, 27, 45, 25);
      HIDE_BUTTON.addEventListener(this);
      this.add(HIDE_BUTTON);
      RAM_BUTTON.setBounds(11, 548, 67, 68);
      RAM_BUTTON.addEventListener(this);
      this.add(RAM_BUTTON);
      DISCORD_BUTTON.setBounds(98, 551, 54, 55);
      DISCORD_BUTTON.addEventListener(this);
      this.add(DISCORD_BUTTON);
      progressBar.setBounds(0, 600, 975, 25);
      this.add(progressBar);
   }

   public void onEvent(SwingerEvent event) {
      if (event.getSource() == PLAY_BUTTON) {
         this.setFieldEnabled(false);
         if (USERNAME_FIELD.getText().replaceAll(" ", "").length() == 0) {
            LauncherFrame.getInstance();
            JOptionPane.showMessageDialog(LauncherFrame.getLauncherPanel(), "Veuillez entrer un pseudo valide.", "Erreur", 0);
            this.setFieldEnabled(true);
            return;
         }

         (new Thread(() -> {
            SAVER.set("Username", USERNAME_FIELD.getText());
            Launcher.auth(USERNAME_FIELD.getText());

            try {
               Launcher.updateMinecraft();
            } catch (Exception var1) {
               var1.printStackTrace();
            }

            LauncherFrame.getRamSelector().save();
            Launcher.launch();
         })).start();
      } else if (event.getSource() == EXIT_BUTTON) {
         System.exit(0);
      } else if (event.getSource() == HIDE_BUTTON) {
         LauncherFrame.getInstance().setState(1);
      } else if (event.getSource() == DISCORD_BUTTON) {
         try {
            Desktop.getDesktop().browse((new URL("https://discord.gg/3WtxpmFRFn")).toURI());
         } catch (IOException var3) {
            var3.printStackTrace();
         } catch (URISyntaxException var4) {
            var4.printStackTrace();
         }
      } else if (event.getSource() == RAM_BUTTON) {
         LauncherFrame.getRamSelector().display();
      }

   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Swinger.drawFullsizedImage(g, this, background);
   }

   public void setFieldEnabled(boolean enabled) {
      USERNAME_FIELD.setEnabled(enabled);
      PLAY_BUTTON.setEnabled(enabled);
      HIDE_BUTTON.setEnabled(enabled);
      EXIT_BUTTON.setEnabled(enabled);
      RAM_BUTTON.setEnabled(enabled);
      DISCORD_BUTTON.setEnabled(enabled);
   }

   public static STexturedProgressBar getProgressBar() {
      return progressBar;
   }

   static {
      USERNAME_FIELD = new JTextField(SAVER.get("Username"));
      PLAY_BUTTON = new SColoredButton(Swinger.getTransparentWhite(0), Swinger.getTransparentInstance(Color.gray, 50));
      EXIT_BUTTON = new SColoredButton(Swinger.getTransparentWhite(0), Swinger.getTransparentInstance(Color.gray, 50));
      HIDE_BUTTON = new SColoredButton(Swinger.getTransparentWhite(0), Swinger.getTransparentInstance(Color.gray, 50));
      RAM_BUTTON = new SColoredButton(Swinger.getTransparentWhite(0), Swinger.getTransparentInstance(Color.gray, 50));
      DISCORD_BUTTON = new SColoredButton(Swinger.getTransparentWhite(0), Swinger.getTransparentInstance(Color.gray, 50));
      progressBar = new STexturedProgressBar(Swinger.getResource("progress_bar.png"), Swinger.getResource("progress_bar_hovered.png"));
   }
}
