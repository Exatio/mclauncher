package fr.kphoenix.minarium.bootstrap;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.SplashScreen;
import fr.theshark34.openlauncherlib.util.explorer.ExploredDirectory;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.swinger.Swinger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MinariumBootstrap {

    private static final File MN_DIR = new File(GameDirGenerator.createGameDir("Minarium", false).toFile(), "launcher");
    private static final CrashReporter MN_REPORTER = new CrashReporter("Minarium", new File(MN_DIR, "crashs").toPath());
    private SplashScreen splash;
    private Thread barThread;

    public static void main(String[] args)
    {
        new MinariumBootstrap().start();
    }

    private void start()
    {
        Swinger.setResourcePath("/");

        displaySplash();
        try
        {
            download();
        }
        catch (Exception e)
        {
            MN_REPORTER.catchError(e, "Impossible de télécharger le launcher Minarium:");
            this.barThread.interrupt();
        }
        try
        {
            launchLauncher();
        }
        catch (Exception e)
        {
            MN_REPORTER.catchError(e, "Impossible de lancer le launcher Minarium:");
        }
    }

    private void displaySplash()
    {
        BufferedImage logo = Swinger.getResource("icon.jpg");
        this.splash = new SplashScreen("Minarium", logo);
        this.splash.setIconImage(logo);
        this.splash.setBackground(Swinger.TRANSPARENT);
        this.splash.setLayout(null);
        this.splash.setVisible(true);
    }

    private void download() throws IOException {
        URL launcherURL = new URL("https://minarium.la-plateforme-gaming.fr/launcher/crack/launcher.jar");
        if(!MN_DIR.exists()) {
            Files.createDirectories(MN_DIR.toPath());
        }
        Files.deleteIfExists(new File(MN_DIR, "launcher.jar").toPath());
        Files.copy(launcherURL.openStream(), new File(MN_DIR, "launcher.jar").toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
    }

    private void launchLauncher() throws LaunchException
    {
        ClasspathConstructor constructor = new ClasspathConstructor();
        ExploredDirectory gameDir = Explorer.dir(MN_DIR.toPath());
        constructor.add(gameDir.get("launcher.jar"));

        ExternalLaunchProfile profile = new ExternalLaunchProfile("fr.kphoenix.minarium.launcher.LauncherFrame", constructor.make());
        ExternalLauncher launcher = new ExternalLauncher(profile);
        Process b = launcher.launch();
        this.splash.setVisible(false);
        try
        {
            b.waitFor();
        }
        catch (InterruptedException localInterruptedException) {}
        System.exit(0);
    }

}