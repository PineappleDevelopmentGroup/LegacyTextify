package sh.miles.legacytextify

import org.bukkit.plugin.java.JavaPlugin
import sh.miles.legacytextify.command.LegacyTextifyCommand
import sh.miles.pineapple.PineappleLib

class LegacyTextifyPlugin : JavaPlugin() {

    override fun onEnable() {
        PineappleLib.initialize(this)
        PineappleLib.getCommandRegistry().register(LegacyTextifyCommand())
    }

    override fun onDisable() {
        PineappleLib.cleanup()
    }
}
