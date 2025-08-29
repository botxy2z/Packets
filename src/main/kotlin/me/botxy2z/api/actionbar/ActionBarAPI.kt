package me.botxy2z.api.actionbar

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

object ActionBarAPI {

    private val activeTasks = mutableMapOf<UUID, BukkitTask>()
    private lateinit var plugin: Plugin

    fun init(plugin: Plugin) {
        this.plugin = plugin
    }

    fun sendActionBar(player: Player, message: String) {
        val packet = createActionBarPacket(message)
        sendPacket(player, packet)
    }

    fun sendActionBarInfinite(player: Player, message: String) {
        stopActionBar(player)

        val task = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (!player.isOnline) {
                stopActionBar(player)
                return@Runnable
            }
            sendActionBar(player, message)
        }, 0L, 20L)

        activeTasks[player.uniqueId] = task
    }

    fun stopActionBar(player: Player) {
        activeTasks[player.uniqueId]?.cancel()
        activeTasks.remove(player.uniqueId)
        sendActionBar(player, "")
    }

    private fun createActionBarPacket(message: String): Any {
        val chatComponentClass = Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent")
        val chatSerializerClass = Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent\$ChatSerializer")
        val packetClass = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutChat")

        val chatComponent = chatSerializerClass.getMethod("a", String::class.java)
            .invoke(null, "{\"text\":\"$message\"}")

        val constructor = packetClass.getConstructor(chatComponentClass, Byte::class.java)
        return constructor.newInstance(chatComponent, 2.toByte())
    }

    private fun sendPacket(player: Player, packet: Any) {
        val craftPlayer = Class.forName("org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer").cast(player)
        val handle = craftPlayer.javaClass.getMethod("getHandle").invoke(craftPlayer)
        val playerConnection = handle.javaClass.getField("playerConnection").get(handle)
        val sendPacketMethod = playerConnection.javaClass.getMethod("sendPacket", Class.forName("net.minecraft.server.v1_8_R3.Packet"))
        sendPacketMethod.invoke(playerConnection, packet)
    }
}
