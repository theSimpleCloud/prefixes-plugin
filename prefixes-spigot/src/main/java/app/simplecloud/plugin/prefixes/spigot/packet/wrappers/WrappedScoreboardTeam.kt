import com.comphenix.protocol.reflect.FuzzyReflection
import com.comphenix.protocol.reflect.accessors.Accessors
import com.comphenix.protocol.reflect.accessors.FieldAccessor
import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.wrappers.AbstractWrapper
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.WrappedChatComponent
import org.bukkit.ChatColor
import org.bukkit.scoreboard.Team

class WrappedScoreboardTeam private constructor(handle: Any) :
    AbstractWrapper(MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam\$b")) {
    init {
        setHandle(handle)
    }

    var displayName: WrappedChatComponent
        get() = WrappedChatComponent.fromHandle(DISPLAY_NAME[handle])
        set(displayName) {
            DISPLAY_NAME[handle] = displayName.handle
        }

    var prefix: WrappedChatComponent
        get() = WrappedChatComponent.fromHandle(PREFIX[handle])
        set(prefix) {
            PREFIX[handle] = prefix.handle
        }

    var suffix: WrappedChatComponent
        get() = WrappedChatComponent.fromHandle(SUFFIX[handle])
        set(suffix) {
            SUFFIX[handle] = suffix.handle
        }

    var nameTagVisibility: Team.OptionStatus
        get() {
            val value = NAME_TAG_VISIBILITY[handle] as String
            return when (value) {
                "always" -> Team.OptionStatus.ALWAYS
                "never" -> Team.OptionStatus.NEVER
                "hideForOtherTeams" -> Team.OptionStatus.FOR_OTHER_TEAMS
                "hideForOwnTeam" -> Team.OptionStatus.FOR_OWN_TEAM
                else -> throw IllegalArgumentException("Unexpected value: $value")
            }
        }
        set(value) {
            when (value) {
                Team.OptionStatus.ALWAYS -> NAME_TAG_VISIBILITY[handle] =
                    "always"

                Team.OptionStatus.NEVER -> NAME_TAG_VISIBILITY[handle] =
                    "never"

                Team.OptionStatus.FOR_OTHER_TEAMS -> NAME_TAG_VISIBILITY[handle] =
                    "hideForOtherTeams"

                Team.OptionStatus.FOR_OWN_TEAM -> NAME_TAG_VISIBILITY[handle] =
                    "hideForOwnTeam"

                else -> throw IllegalArgumentException("Unexpected value: $value")
            }
        }

    var collisionRule: Team.OptionStatus
        get() {
            val value = COLLISION_RULE[handle] as String
            return when (value) {
                "always" -> Team.OptionStatus.ALWAYS
                "never" -> Team.OptionStatus.NEVER
                "pushOtherTeams" -> Team.OptionStatus.FOR_OTHER_TEAMS
                "pushOwnTeam" -> Team.OptionStatus.FOR_OWN_TEAM
                else -> throw IllegalArgumentException("Unexpected value: $value")
            }
        }
        set(value) {
            when (value) {
                Team.OptionStatus.ALWAYS -> COLLISION_RULE[handle] =
                    "always"

                Team.OptionStatus.NEVER -> COLLISION_RULE[handle] =
                    "never"

                Team.OptionStatus.FOR_OTHER_TEAMS -> COLLISION_RULE[handle] =
                    "pushOtherTeams"

                Team.OptionStatus.FOR_OWN_TEAM -> COLLISION_RULE[handle] =
                    "pushOwnTeam"

                else -> throw IllegalArgumentException("Unexpected value: $value")
            }
        }

    var teamColor: ChatColor?
        get() = ChatColor.getByChar(TEAM_COLOR[handle].toString()[1])
        set(value) {
            if (value!!.isColor || value === ChatColor.RESET) {
                TEAM_COLOR[handle] = CHATCOLOR_CONVERTER.getGeneric(value)
            }
        }

    var friendlyFire: Boolean = false
        get() {
            val intValue = (FRIENDLY_FLAGS[handle] as Int)
            return (intValue and 0x01) == 1
        }
        set(value) {
            val currentValue = (FRIENDLY_FLAGS[handle] as Int)
            if (field && !value) {
                // is already friendly fire but should not be
                FRIENDLY_FLAGS[handle] = currentValue xor 0x01
            } else if (value) {
                // is already friendly fire but should not be
                FRIENDLY_FLAGS[handle] = currentValue or 0x01
            }
            field = value
        }

    var friendlySeeInvisible: Boolean
        get() {
            val intValue = (FRIENDLY_FLAGS[handle] as Int)
            return (intValue and 0x02) == 2
        }
        set(value) {
            val currentValue = (FRIENDLY_FLAGS[handle] as Int)
            if (friendlyFire && !value) {
                // is already friendly fire but should not be
                FRIENDLY_FLAGS[handle] = currentValue xor 0x02
            } else if (value) {
                // is already friendly fire but should not be
                FRIENDLY_FLAGS[handle] = currentValue or 0x02
            }
        }

    companion object {
        private val DISPLAY_NAME: FieldAccessor = getFieldAccessor(
                MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam\$b"),
                "a",
                true
            )
        private val PREFIX: FieldAccessor = getFieldAccessor(
                MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam\$b"),
                "b",
                true
            )
        private val SUFFIX: FieldAccessor = getFieldAccessor(
                MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam\$b"),
                "c",
                true
            )

        private val NAME_TAG_VISIBILITY: FieldAccessor = getFieldAccessor(
                MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam\$b"),
                "d",
                true
            )
        private val COLLISION_RULE: FieldAccessor = getFieldAccessor(
                MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam\$b"),
                "e",
                true
            )
        private val TEAM_COLOR: FieldAccessor = getFieldAccessor(
                MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam\$b"),
                "f",
                true
            )
        private val FRIENDLY_FLAGS: FieldAccessor = getFieldAccessor(
                MinecraftReflection.getMinecraftClass("network.protocol.game.PacketPlayOutScoreboardTeam\$b"),
                "g",
                true
            )

        private val ENUM_CHAT_FORMAT_CLASS: Class<*> = MinecraftReflection.getMinecraftClass("EnumChatFormat")
        private val CHATCOLOR_CONVERTER = EnumWrappers.IndexedEnumConverter(
            ChatColor::class.java,
            ENUM_CHAT_FORMAT_CLASS
        )

        fun fromHandle(handle: Any): WrappedScoreboardTeam {
            return WrappedScoreboardTeam(handle)
        }

        private fun getFieldAccessor(instanceClass: Class<*>?, fieldName: String, forceAccess: Boolean): FieldAccessor {
            val field = FuzzyReflection.fromClass(instanceClass, forceAccess).getFieldByName(fieldName)
            return Accessors.getFieldAccessor(field)
        }
    }

}