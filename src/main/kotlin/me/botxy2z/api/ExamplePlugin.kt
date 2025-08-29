package me.botxy2z.api

import me.botxy2z.api.actionbar.ActionBarAPI
import me.botxy2z.api.send.ExampleSend
import org.bukkit.plugin.java.JavaPlugin

class ExamplePlugin : JavaPlugin() {

    override fun onEnable() {
        ActionBarAPI.init(this)
        server.pluginManager.registerEvents(ExampleSend(), this)
    }

    override fun onDisable() {
    }
}
