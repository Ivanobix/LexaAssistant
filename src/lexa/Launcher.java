package lexa;

import lexa.listeners.GuildMessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Launcher extends ListenerAdapter {

    private static JDA jda;

    public static void main(String[] args) {
        if (initJDA(args[0])) {
            initSettings();
            initListeners();
        }
    }

    private static boolean initJDA(String token) {
        boolean initResult = false;
        try {
            jda = JDABuilder.createLight(token).build();
            initResult = true;
        } catch (LoginException ignored) {
            //Ignore exception
        }
        return initResult;
    }

    private static void initSettings() {
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("Bug Searching"));
    }

    private static void initListeners() {
        jda.addEventListener(new GuildMessageListener());
    }

    public static JDA getJda() {
        return jda;
    }

}