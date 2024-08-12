package cn.mcdd.hikaricpexample;

import org.bukkit.plugin.java.JavaPlugin;

public final class HikariCPExample extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // 最好是在插件启动时初始化 避免启动时没有初始化 在使用时疯狂报错的情况
        MysqlUtil.getInstance().init();
        Service service = new Service();
        service.sayHello();
        getLogger().info("HikariCP Example Enabled");

    }

    @Override
    public void onDisable() {
        getLogger().info("HikariCP Example Disabled");
    }
}
