package cn.mcdd.hikaricpexample;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Mysql工具类
 * @author dakuo
 */
public class MysqlUtil {

    private HikariDataSource dataSource;

    private static final MysqlUtil instance = new MysqlUtil();
    private MysqlUtil(){

    }

    /**
     * 单例模式工具类
     * @return 唯一实例
     */
    public synchronized static MysqlUtil getInstance() {
        return instance;
    }

    /**
     * 初始化方法
     */
    public void init(){
        HikariCPExample plugin = HikariCPExample.getPlugin(HikariCPExample.class);
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection databaseSection = config.getConfigurationSection("database");
        // 连接数据库的基础配置
        String host = databaseSection.getString("host", "localhost");
        int port = databaseSection.getInt("port", 3306);
        String username = databaseSection.getString("username", "root");
        String password = databaseSection.getString("password", "");
        String database = databaseSection.getString("database", "minecraft");

        // 连接池参数
        ConfigurationSection hikaricpSection = databaseSection.getConfigurationSection("hikaricp");
        long connectionTimeout = hikaricpSection.getLong("connectionTimeout",30000L);
        long idleTimeout = hikaricpSection.getLong("idleTimeout",600000L);
        long maxLifetime = hikaricpSection.getLong("maxLifetime",1800000L);
        String connectionTestQuery = hikaricpSection.getString("connectionTestQuery","SELECT 1");
        int minimumIdle = hikaricpSection.getInt("minimumIdle",10);
        int maximumPoolSize = hikaricpSection.getInt("maximumPoolSize",30);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setMaxLifetime(maxLifetime);
        hikariConfig.setConnectionTestQuery(connectionTestQuery);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        // 设置驱动类
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // 设置jdbc URL
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database+"?useUnicode=true&characterEncoding=utf8&useSSL=false");
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * 测试方法
     * 注意：此处仅作简单示例 在实战中请使用 connection.prepareStatement(); 对sql语句进行预处理 以免造成sql注入
     * @return helloHikari
     */
    public String test(){
        try {
            Connection connection = dataSource.getConnection(); // 此处拿到的Connection实现为 com.zaxxer.hikari.pool.ProxyConnection
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select 'helloHikari' as ret");
            rs.next(); //数据库结果集 表结果 要滚动行
            String ret =  rs.getString("ret");
            // statement.close(); 与传统JDBC操作不同 此处不需要手动关闭 statement 在ProxyConnection#close方法的第一行中执行了关闭所有statement的操作
            connection.close(); // 并非真正的关闭连接而是通知连接池请对这个连接进行回收
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
