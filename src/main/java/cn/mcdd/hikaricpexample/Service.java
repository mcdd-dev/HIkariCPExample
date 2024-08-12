package cn.mcdd.hikaricpexample;

import java.util.logging.Logger;

/**
 * 测试业务类
 * @author dakuo
 */
public class Service {

    private final HikariCPExample plugin = HikariCPExample.getPlugin(HikariCPExample.class);

    public void sayHello() {
        String test = MysqlUtil.getInstance().test();

        Logger logger = plugin.getLogger();
        logger.info(test);

    }
}
