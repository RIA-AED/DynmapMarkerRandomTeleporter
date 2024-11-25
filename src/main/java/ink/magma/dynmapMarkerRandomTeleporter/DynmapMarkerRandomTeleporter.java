package ink.magma.dynmapMarkerRandomTeleporter;

import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;

public final class DynmapMarkerRandomTeleporter extends JavaPlugin {
    // 存储 Dynmap 的公共 API 实例
    private DynmapCommonAPI dynmapApiInstance = null;
    // 存储插件实例
    private static DynmapMarkerRandomTeleporter pluginInstance;

    @Override
    public void onEnable() {
        // 设置插件实例
        pluginInstance = this;

        // 注册 Dynmap API 监听器
        DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {
            @Override
            public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
                // 当 Dynmap API 启用时，存储 API 实例
                dynmapApiInstance = dynmapCommonAPI;
                getLogger().info("Dynmap 已注册其 API");
            }
        });

        // 注册随机传送命令
        this.getCommand("randomteleport").setExecutor(new RandomTeleportCommand());
    }

    @Override
    public void onDisable() {
        // 插件关闭逻辑
    }

    // 提供一个静态方法以获取插件实例
    public static DynmapMarkerRandomTeleporter getInstance() {
        return pluginInstance;
    }

    // 提供一个方法以获取 Dynmap API 实例
    public DynmapCommonAPI getDynmapApiInstance() {
        return dynmapApiInstance;
    }
}

