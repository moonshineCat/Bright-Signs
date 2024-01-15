package com.github.moonshinecat.brightsigns;

import com.github.moonshinecat.brightsigns.command.BrightSignsCommand;
import com.github.moonshinecat.brightsigns.config.BrightSignsConfig;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "brightsigns", useMetadata = true)
public class BrightSigns {
    public static final String MODID = "brightsigns";
    public static final Logger LOGGER = LogManager.getLogger("Bright Signs");
    private static BrightSignsConfig config;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //Config
        config = new BrightSignsConfig();

        //Command
        ClientCommandHandler.instance.registerCommand(new BrightSignsCommand());
    }

    public static BrightSignsConfig getConfig(){
        return config;
    }
}
