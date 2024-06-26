package net.zffu.buildtickets.locale;

import jdk.vm.ci.meta.Local;
import net.zffu.buildtickets.BuildTicketsPlugin;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

/**
 * Manages the languages files for translation support.
 */
public class LocaleManager {

    private BuildTicketsPlugin plugin;
    public static final Locale[] SUPPORTED_LOCALES = new Locale[] {Locale.ENGLISH};
    public static HashMap<Locale, HashMap<String, String>> locales = new HashMap<>();
    public static HashMap<UUID, Locale> playerLocales;

    public static Locale defaultLocale = null;

    public LocaleManager(BuildTicketsPlugin plugin) {
        this.plugin =  plugin;
        defaultLocale = Locale.forLanguageTag(plugin.getConfig().getString("default-language", "en"));
        if(plugin.getConfig().getBoolean("allow-players-to-choose-custom-language", false)) {
            playerLocales = new HashMap<>();
        }
    }

    /**
     * Loads a specific locale.
     * @param locale
     */
    public void loadLocale(Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("buildtickets", locale);
            Enumeration<String> keys = bundle.getKeys();

            HashMap<String, String> l = new HashMap<>();

            while(keys.hasMoreElements()) {
                String key = keys.nextElement();
                l.put(key, bundle.getString(key));
            }

            locales.put(locale, l);
        } catch (Exception e) {
            this.plugin.getLogger().warning("Could not load locale " + locale + e);
        }
    }

    /**
     * Gets the message from the locale.
     * @param id
     * @return
     */
    public static String getMessage(String id) {
        String t = locales.get(defaultLocale).get(id);
        if(t == null) return id;
        return t;
    }

    /**
     * Gets the message from the locale.
     * @param id
     * @param locale
     * @return
     */
    public static String getMessage(String id, Locale locale) {
        String t = locales.get(locale).get(id);
        if(t == null) return id;
        return t;
    }

    public static String getMessage(String id, HumanEntity entity) {
        if(playerLocales == null || !playerLocales.containsKey(entity.getUniqueId())) return getMessage(id);
        return getMessage(id, playerLocales.get(entity.getUniqueId()));
    }

    public static void setPlayerLocale(HumanEntity entity, Locale locale) {
        if(locale == defaultLocale) playerLocales.remove(entity.getUniqueId());
        playerLocales.put(entity.getUniqueId(), locale);
    }

    /**
     * Loads the locales
     */
    public void loadLocales() {
        for(Locale locale : SUPPORTED_LOCALES) {
            loadLocale(locale);
        }
    }
}
