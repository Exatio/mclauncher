package fr.kphoenix.minarium.launcher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VersionType;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

public class Launcher {
   public static final GameVersion MINARIUM_VERSION;
   public static final GameInfos MINARIUM_INFOS;
   public static final File MINARIUM_DIR;
   private static AuthInfos authInfos;

   public static void auth(String username) {
      authInfos = new AuthInfos(username, "no_access_token", "no_uuid");
   }

   public static void updateMinecraft() throws Exception {
      VanillaVersion version = (new VanillaVersion.VanillaVersionBuilder()).withName("1.16.5").withVersionType(VersionType.FORGE).build();
      List<Mod> mods = Mod.getModsFromJson("https://minarium.la-plateforme-gaming.fr/launcher/mods.json");
      File file = new File("minarium.log");
      file.createNewFile();
      ILogger logger = new Logger("[Minarium] - ", file.toPath(), true);
      UpdaterOptions options = (new UpdaterOptions.UpdaterOptionsBuilder()).withEnableCurseForgePlugin(true).build();
      AbstractForgeVersion forge = (new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW)).withForgeVersion("1.16.5-36.2.8").withMods(mods).withFileDeleter(new ModFileDeleter(true, new String[0])).build();
      FlowUpdater updater = (new FlowUpdater.FlowUpdaterBuilder()).withVanillaVersion(version).withLogger(logger).withProgressCallback(new IProgressCallback() {
         public void init(ILogger logger) {
         }

         public void step(Step step) {
         }

         public void update(long downloaded, long max) {
            LauncherPanel.getProgressBar().setValue((int)downloaded);
            LauncherPanel.getProgressBar().setMaximum((int)max);
         }

         public void onFileDownloaded(Path path) {
         }
      }).withForgeVersion(forge).withUpdaterOptions(options).build();
      updater.update(OS.getAppDir("minarium"));
   }

   public static void launch() {
      try {
         ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(MINARIUM_INFOS, GameFolder.FLOW_UPDATER, authInfos);
         profile.getVmArgs().addAll(Arrays.asList(LauncherFrame.getRamSelector().getRamArguments()));
         ExternalLauncher launcher = new ExternalLauncher(profile);
         LauncherFrame.getInstance().setVisible(false);
         Process p = launcher.launch();

         try {
            p.waitFor();
         } catch (InterruptedException var4) {
         }

         System.exit(0);
      } catch (LaunchException var5) {
         var5.printStackTrace();
         LauncherFrame.getInstance();
         JOptionPane.showMessageDialog(LauncherFrame.getLauncherPanel(), "Oops! Il y a eu un souci avec le lancement du jeu. Veuillez contacter un administrateur.", "Erreur", 0);
      }

   }

   static {
      MINARIUM_VERSION = new GameVersion("1.16.5", GameType.V1_13_HIGHER_FORGE.setNFVD(new NewForgeVersionDiscriminator("36.2.8", "1.16.5", "net.minecraftforge", "20210115.111550")));
      MINARIUM_INFOS = new GameInfos("Minarium", MINARIUM_VERSION, new GameTweak[0]);
      MINARIUM_DIR = MINARIUM_INFOS.getGameDir().toFile();
   }
}
