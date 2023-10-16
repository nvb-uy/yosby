package elocindev.yosby.forge.config;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.necronomicon.config.NecConfig;

public class YOSBYConfig {
    @NecConfig
    public static YOSBYConfig INSTANCE;

    public static String getFile() {
        return NecConfigAPI.getFile("necronomicon.json");
    }

    public String default_configs_folder = "yosby";
}