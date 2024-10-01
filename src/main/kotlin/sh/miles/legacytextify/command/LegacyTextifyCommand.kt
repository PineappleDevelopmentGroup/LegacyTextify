package sh.miles.legacytextify.command

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import sh.miles.legacytextify.LegacyTextUtil
import sh.miles.legacytextify.TextUtilOption
import sh.miles.pineapple.chat.PineappleChat
import sh.miles.pineapple.command.Command
import sh.miles.pineapple.command.CommandLabel

class LegacyTextifyCommand : Command(CommandLabel("legacytextify", "legacytextify.command")) {

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        if (args.size <= 1) {
            sender.spigot().sendMessage(
                PineappleChat.parse("<red>Can not convert empty string")
            )
            return true
        }

        val list: MutableList<String> = args.toMutableList()
        val function = TextUtilOption.fromString(list.removeFirst())
        val joined = list.joinToString(" ")
        if (sender is ConsoleCommandSender) {
            asConsole(sender, joined, function)
        } else {
            asEntity(sender, joined, function)
        }

        return true
    }

    override fun complete(sender: CommandSender, args: Array<out String>): MutableList<String> {
        if (args.size == 1) {
            return TextUtilOption.entries.map { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }
                .toMutableList()
        }
        return mutableListOf()
    }

    private fun asConsole(sender: CommandSender, text: String, option: TextUtilOption) {
        val asLegacy = LegacyTextUtil(PineappleChat.parseLegacy(text))
        val transition = option.invoke(asLegacy).transition()
        sender.spigot().sendMessage(
            ComponentBuilder()
                .append(PineappleChat.parse("<green>Parsed Pineapple Chat Successfully\n"))
                .append(PineappleChat.parse("<gray>Transformed Text: "))
                .append(TextComponent(transition)).build()
        )
    }

    private fun asEntity(sender: CommandSender, text: String, option: TextUtilOption) {
        val asLegacy = LegacyTextUtil(PineappleChat.parseLegacy(text))
        val transition = option.invoke(asLegacy).transition()
        sender.spigot().sendMessage(
            ComponentBuilder().append(PineappleChat.parse("<green>Parsed Pineapple Chat Successfully\n"))
                .append(PineappleChat.parse("<gray>Transformed Text: "))
                .append(PineappleChat.parse(text))
                .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(TextComponent(transition))))
                .event(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, transition)).build()
        )
    }


}
