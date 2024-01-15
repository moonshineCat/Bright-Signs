package com.github.moonshinecat.brightsigns.command;

import com.github.moonshinecat.brightsigns.BrightSigns;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class BrightSignsCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "brightsigns";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>(){{add("bs");}}; //bs
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/brightsigns";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length < 2){
            return getListOfStringsMatchingLastWord(args, "toggle");
        }

        return super.addTabCompletionOptions(sender, args, pos);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("toggle")){
                boolean next = !BrightSigns.getConfig().state;
                BrightSigns.getConfig().write("general", "state", next);
                String suffix = next ? EnumChatFormatting.GREEN + "on" : EnumChatFormatting.RED + "off";
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Set mod state to: " + suffix));
                return;
            }
        }

        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Missing arguments! Usage: /brightsigns toggle"));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
