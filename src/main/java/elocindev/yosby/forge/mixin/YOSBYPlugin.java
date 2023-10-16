package elocindev.yosby.forge.mixin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import net.minecraftforge.fml.loading.FMLPaths;

public class YOSBYPlugin implements IMixinConfigPlugin {

    public static final Logger LOGGER = LogManager.getLogger("yosby");
    public static final File RUN_DIR = FMLPaths.GAMEDIR.get().toFile();
    public static final File CONFIG_DIR = FMLPaths.CONFIGDIR.get().toFile();

    @Override
    public void onLoad(String mixinPackage) {
        try {
            NecConfigAPI.registerConfig(YOSBYPlugin.class);
        } catch (Exception e) {
            LOGGER.error("Failed to register YOSBY config.", e);            
        }

        try {
            File yosby = new File(CONFIG_DIR, "yosby");
            if (!yosby.exists() && !yosby.mkdirs()) {
                throw new IllegalStateException("Could not create directory: " + yosby.getAbsolutePath());
            }
            new File(yosby, "options.txt").createNewFile();
            File config = new File(yosby, "config");
            if (!config.exists() && !config.mkdirs()) {
                throw new IllegalStateException("Could not create directory: " + config.getAbsolutePath());
            }
            Files.walk(yosby.toPath()).forEach(path -> {
                File file = path.normalize().toAbsolutePath().normalize().toFile();
                if (!file.isFile()) return;
                try {
                    try {
                        Path configRelative = config.toPath().toAbsolutePath().normalize().relativize(file.toPath().toAbsolutePath().normalize());
                        if (configRelative.startsWith("yosby"))
                            throw new IllegalStateException("Illegal default config file: " + file);
                        applyDefaultOptions(new File(CONFIG_DIR, configRelative.normalize().toString()), file);
                    } catch (IllegalArgumentException e) {
                        System.out.println(yosby.toPath().toAbsolutePath().normalize());
                        System.out.println(file.toPath().toAbsolutePath().normalize());
                        System.out.println(yosby.toPath().toAbsolutePath().normalize().relativize(file.toPath().toAbsolutePath().normalize()));
                        applyDefaultOptions(new File(RUN_DIR, yosby.toPath().toAbsolutePath().normalize().relativize(file.toPath().toAbsolutePath().normalize()).normalize().toString()), file);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Failed to apply default options.", e);
        }
    }

    private void applyDefaultOptions(File file, File defaultFile) throws IOException {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new IllegalStateException("Could not create directory: " + file.getParentFile().getAbsolutePath());
        }
        if (!defaultFile.getParentFile().exists() && !defaultFile.getParentFile().mkdirs()) {
            throw new IllegalStateException("Could not create directory: " + defaultFile.getParentFile().getAbsolutePath());
        }
        if (!defaultFile.exists()) {
            defaultFile.createNewFile();
            return;
        }
        if (file.exists()) return;
        LOGGER.info("Applying default options for " + File.separator + RUN_DIR.toPath().toAbsolutePath().normalize().relativize(file.toPath().toAbsolutePath().normalize()).normalize().toString() + " from " + File.separator +
                    RUN_DIR.toPath().toAbsolutePath().normalize().relativize(defaultFile.toPath().toAbsolutePath().normalize()).normalize().toString());
        Files.copy(defaultFile.toPath(), file.toPath());
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
    
}
