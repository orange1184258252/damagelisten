package me.chengzi.damagelisten;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;  // 导入Listener
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Damagelisten extends JavaPlugin implements Listener { // 实现Listener接口

    @Override
    public void onEnable() {
        System.out.println("橙子伤害监听已加载!");

        saveDefaultConfig(); // 加载配置文件
        getServer().getPluginManager().registerEvents(this, this); // 注册事件监听
    }

    @Override
    public void onDisable() {
        System.out.println("橙子伤害监听已卸载!");
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getDamage() > 0) {
                FileConfiguration config = getConfig();
                String command = config.getString("death-command");

                if (command != null && !command.isEmpty()) {
                    command = command.replace("<p>", player.getName());

                    if (isValidCommand(command)) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    } else {
                        getLogger().warning("无效的命令: " + command);
                    }
                } else {
                    getLogger().warning("配置文件中未找到有效的死亡命令");
                }
            }
        }
    }

    private boolean isValidCommand(String command) {
        if (command.contains(";") || command.contains("&")) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("Damagelisten")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("damagelisten.reload")) {
                    this.reloadConfig();
                    sender.sendMessage("插件配置已重载!");
                    return true;
                } else {
                    sender.sendMessage("您没有权限执行此命令!");
                    return true;
                }
            }
        }
        return false;
    }
}
