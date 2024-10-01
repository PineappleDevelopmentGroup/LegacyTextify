package sh.miles.legacytextify

import net.md_5.bungee.api.ChatColor
import sh.miles.pineapple.chat.minecraft.legacy.parse.LegacyPineappleParserContext

fun main() {
    val parser = LegacyPineappleParserContext(true)
    val util = LegacyTextUtil(parser.parse("<gradient:#00FF00:#FF00FF><bold>Zero to Hero"))
    util.fold().ampersand()
    println(util.transition())
}

enum class TextUtilOption(private val change: (LegacyTextUtil) -> LegacyTextUtil) {
    FOLD({ util -> util.fold().ampersand() }),
    AMPERSAND(LegacyTextUtil::ampersand);

    companion object {
        fun fromString(string: String): TextUtilOption {
            return if (string.equals("fold", true)) {
                FOLD
            } else if (string.equals("ampersand", true)) {
                AMPERSAND
            } else {
                return FOLD
            }
        }
    }

    fun invoke(util: LegacyTextUtil): LegacyTextUtil {
        return change.invoke(util)
    }
}

class LegacyTextUtil(private val legacyText: String) {

    companion object {
        private const val SECTION = ChatColor.COLOR_CHAR
    }

    private var fold = false
    private var ampersand = false

    fun fold() = apply { this.fold = true }
    fun ampersand() = apply { this.ampersand = true }

    fun transition(): String {
        var modified = this.legacyText
        println(modified)
        if (fold) {
            modified = foldHex(modified)
        }

        if (ampersand) {
            modified = modified.replace(SECTION, '&')
        }

        return modified
    }

    private fun foldHex(input: String): String {
        if (input.isBlank()) return input
        val builder = StringBuilder()

        var index = 0
        var curchar = input[0]
        val length = input.length
        while (index < length) {
            if (curchar == SECTION && index + 18 < length && input[index + 1] == 'x') {
                builder.append(SECTION).append("#")
                    .append(input.substring(index + 6, index + 19).replace("$SECTION", ""))

                index += 18
                continue
            } else {
                builder.append(curchar)
            }

            ++index
            if (index < length) {
                curchar = input[index]
            }
        }

        return builder.toString()
    }

}
