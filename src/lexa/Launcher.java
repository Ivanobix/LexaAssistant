package lexa;

import lexa.listeners.GuildMessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Launcher extends ListenerAdapter {

    public static JDA jda;

    public static void main(String[] args) {
        jda = initJDA(args[0]);
        if (jda != null) {
            initSettings();
            initListeners();
        }
    }

    private static JDA initJDA(String token) {
        JDA jdaAux = null;
        try {
            jdaAux = JDABuilder.createLight(token).build();
        } catch (LoginException ignored) {
            //Ignore exception
        }
        return jdaAux;
    }

    private static void initSettings() {
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("Bug Searching"));
    }

    private static void initListeners() {
        jda.addEventListener(new GuildMessageListener());
    }
}