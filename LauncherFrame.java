package fr.kphoenix.minarium.launcher;

import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.swing.JFrame;

public class LauncherFrame extends JFrame {
   private static LauncherFrame instance;
   private static LauncherPanel launcherPanel;
   private static Saver saver;
   private static RamSelector ramSelector;

   public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
      Swinger.setSystemLookNFeel();
      Swinger.setResourcePath("/");
      if (!Launcher.MINARIUM_DIR.exists()) {
         Launcher.MINARIUM_DIR.mkdirs();
      }

      instance = new LauncherFrame();
   }

   public LauncherFrame() {
      File saverFile = new File(Launcher.MINARIUM_DIR.getAbsolutePath() + File.separator + "launcher.properties");
      if (!saverFile.exists()) {
         try {
            saverFile.createNewFile();
         } catch (Exception var5) {
         }
      }

      saver = new Saver(saverFile.toPath());
      File ramFile = new File(Launcher.MINARIUM_DIR.getAbsolutePath() + File.separator + "ram.properties");
      if (!ramFile.exists()) {
         try {
            ramFile.createNewFile();
         } catch (Exception var4) {
         }
      }

      ramSelector = new RamSelector(ramFile.toPath());
      this.setTitle("Minarium");
      this.setSize(975, 625);
      this.setDefaultCloseOperation(3);
      this.setLocationRelativeTo((Component)null);
      this.setUndecorated(true);
      this.setBackground(Swinger.getTransparentInstance(Color.white, 0));
      this.setIconImage(Swinger.getResource("icon.jpg"));
      launcherPanel = new LauncherPanel();
      this.setContentPane(launcherPanel);
      WindowMover mover = new WindowMover(this);
      this.addMouseListener(mover);
      this.addMouseMotionListener(mover);
      this.setVisible(true);
   }

   public static LauncherFrame getInstance() {
      return instance;
   }

   public static LauncherPanel getLauncherPanel() {
      return launcherPanel;
   }

   public static Saver getSaver() {
      return saver;
   }

   public static RamSelector getRamSelector() {
      return ramSelector;
   }
}
