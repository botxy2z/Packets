package me.botxy2z.api.title

import org.bukkit.entity.Player

object TitleAPI {

    fun sendTitle(player: Player, title: String, subtitle: String = "", fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20) {
        sendTimes(player, fadeIn, stay, fadeOut)
        sendTitlePacket(player, title, "TITLE")
        if (subtitle.isNotEmpty()) {
            sendTitlePacket(player, subtitle, "SUBTITLE")
        }
    }

    private fun sendTimes(player: Player, fadeIn: Int, stay: Int, fadeOut: Int) {
        val packetClass = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle")
        val enumTitleAction = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle\$EnumTitleAction")
        val action = enumTitleAction.getField("TIMES").get(null)

        val chatComponentClass = Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent")
        val chatSerializerClass = Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent\$ChatSerializer")
        val chatComponent = chatSerializerClass.getMethod("a", String::class.java)
            .invoke(null, "{\"text\":\"\"}")

        val constructor = packetClass.getConstructor(enumTitleAction, chatComponentClass, Int::class.java, Int::class.java, Int::class.java)
        val packet = constructor.newInstance(action, chatComponent, fadeIn, stay, fadeOut)

        sendPacket(player, packet)
    }

    private fun sendTitlePacket(player: Player, message: String, type: String) {
        val packetClass = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle")
        val enumTitleAction = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle\$EnumTitleAction")
        val action = enumTitleAction.getField(type).get(null)

        val chatComponentClass = Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent")
        val chatSerializerClass = Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent\$ChatSerializer")
        val chatComponent = chatSerializerClass.getMethod("a", String::class.java)
            .invoke(null, "{\"text\":\"$message\"}")

        val constructor = packetClass.getConstructor(enumTitleAction, chatComponentClass)
        val packet = constructor.newInstance(action, chatComponent)

        sendPacket(player, packet)
    }

    private fun sendPacket(player: Player, packet: Any) {
        val craftPlayer = Class.forName("org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer").cast(player)
        val handle = craftPlayer.javaClass.getMethod("getHandle").invoke(craftPlayer)
        val playerConnection = handle.javaClass.getField("playerConnection").get(handle)
        val sendPacketMethod = playerConnection.javaClass.getMethod("sendPacket", Class.forName("net.minecraft.server.v1_8_R3.Packet"))
        sendPacketMethod.invoke(playerConnection, packet)
    }
}
