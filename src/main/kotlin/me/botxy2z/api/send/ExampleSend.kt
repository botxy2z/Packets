package me.botxy2z.api.send

import me.botxy2z.api.actionbar.ActionBarAPI
import me.botxy2z.api.title.TitleAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ExampleSend : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        // Send Infinite ActionBar
        ActionBarAPI.sendActionBarInfinite(player, "Hello, ${player.name}, This is a infinite actionbar.")

        // Send ActionBar Temporary
        //ActionBarAPI.sendActionBar(player, "Hello, ${player.name}, This is a temporary actionbar.")

        // Send Title
        TitleAPI.sendTitle(
            player,
            title = "Title",
            subtitle = "Subtitle",
            fadeIn = 20,
            stay = 80,
            fadeOut = 20
        )
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        // Stop ActionBarAPI
        ActionBarAPI.stopActionBar(player)
    }
}
