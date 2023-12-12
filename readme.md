# SimpleCloud PrefixesApi
Plugin/Extension that manages Prefixes and Suffixes in Tab, and formats the Chat to your liking. [API methods](#api-usage) for doing this are also provided.
You can [implement it on your own](#own-implementation), or use the default implementation for [LuckPerms](https://github.com/LuckPerms/LuckPerms).
For prefixes and suffixes, [Adventure Components]() are used, as well as [MiniMessage]() in the LuckPerms default implementation.

## Supported server versions
| server implementation | PrefixesApi support | LuckPerms support | out of the box support                                                                                  |
|-----------------------|---------------------|-------------------|---------------------------------------------------------------------------------------------------------|
| Paper and Forks       | ✅                   | ✅                 | ✅                                                                                                       |
| Spigot and Forks      | ✅                   | ✅                 | ✅                                                                                                       |
| Minestom              | ✅                   | ❌                 | ❌ (you can easily get it working by using your [own implementation](#creating-api-and-indexing-groups)) |

## Configuring
### LuckPerms
PrefixesApi will automatically register any LuckPerms Group as a PrefixGroup, alongside its prefix, suffix, and TeamColor.
#### Setting a groups prefix/suffix
`/lp group <groupname> meta addprefix/addsuffix <weight> <prefix/suffix in MiniMessage format>`
#### Setting a groups TeamColor
Depending on what server version your on, you have to set a hex value or a ChatColor enum as color.
On Spigot, color should be in ChatColor format (e.g. WHITE). On every other server implementation you can use hex colors

`/lp group <groupname> meta set color <color>`
### Config
You will find the `config.json` File in the DataFolder. It will look like this:
````json
{
  "chatFormat": "<prefix><name_colored><suffix><gray>:</gray> <white><message></white>"
}
````
You can configure the chat message format by editing the value of `chatFormat`. [MiniMessage]() is used, to provide quick and easy color and gradient support to the format.
There also are placeholders you can use for the format:

| Placeholder  | Value                                                       |
|--------------|-------------------------------------------------------------|
| name         | The players name                                            |
| name_colored | The players name, colored the same way as in the scoreboard |
| prefix       | The players prefix                                          |
| suffix       | The players suffix                                          |
| message      | The sent message                                            |

## API usage

### Getting the API object

#### Bukkit/Paper

When PrefixesApi is active, a bukkit service provider is registered containing the Api object. To get it, use the following code.
````kotlin
val prefixesApiProvider: RegisteredServiceProvider<PrefixesApi>? = Bukkit.getServicesManager().getRegisteredServiceProvider(PrefixesApi::class.java)
if(prefixesApiProvider != null) {
    val myPrefixesApi: PrefixesApi = prefixesApiProvider.provider
    //Code using the PrefixApi
}
````
> **IMPORTANT**: To make sure everything works as expected, add `PrefixesApi` to your plugin.yml `depends` list.

#### Minestom
To get the `PrefixesApi` object on Minestom, you need to call the `getApi()` Singleton in the `PrefixesExtension` class.
````kotlin
val myPrefixesApi: PrefixesApi = PrefixesExtension.getApi()
````
> **IMPORTANT**: To make sure everything works as expected, add `PrefixesApi` to your extension.json `dependencies` array.

### Using the API
Now that you have a `PrefixesApi` object, you have access to all of these Functions:
````kotlin
    /**
     * Sets the prefix and suffix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param group
     */
    fun setWholeName(uniqueId: UUID, group: PrefixesGroup)

    /**
     * Sets the prefix and suffix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param groupName
     */
    fun setWholeName(uniqueId: UUID, groupName: String)

    /**
     * Sets the prefix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param prefix prefix to set
     */
    fun setPrefix(uniqueId: UUID, prefix: Component)

    /**
     * Sets the prefix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param suffix suffix to set
     */
    fun setSuffix(uniqueId: UUID, suffix: Component)

    /**
     * Returns all registered PrefixesGroup ordered by priority
     */
    fun getGroups(): List<PrefixesGroup>

    /**
     * Returns the highest PrefixesGroup of a player
     * @param uniqueId UUID of the target player
     */
    fun getHighestGroup(uniqueId: UUID): PrefixesGroup

    /**
     * Adds a [PrefixesGroup]
     * @param group
     */
    fun addGroup(group: PrefixesGroup)

    /**
     * Changes the PrefixesActor of the server instance (e.g. to a bukkit actor)
     * @param actor
     */
    fun setActor(actor: PrefixesActor)

    /**
     * Changes the Scoreboard Team color of the target player (Used in 1.12+ to make player names colorful)
     * @param uniqueId UUID of the target player
     * @param color Color string (ChatColor on spigot, hex colors on other server implementations)
     */
    fun setColor(uniqueId: UUID, color: String)

    /**
     * Sets the used PrefixesConfig
     * @param config Specifies the new [PrefixesConfig]
     */
    fun setConfig(config: PrefixesConfig)
````
## Own implementation
PrefixesApi supports the reimplementation of features through third-party Plugins.
### PrefixesGroup
The interface `PrefixesGroup` provides the structure to all necessary features for a PrefixesGroup. By implementing it in a custom class, you can add this group to the PrefixesApi by calling the `addGroup` function.

#### Example group implementation

````kotlin
class MyPrefixesGroup : PrefixesGroup {
    override fun getName(): String {
        return "mygroup"
    }
    override fun getPrefix(): Component
    {
        return Component.text("")
    }
    override fun getColor(): String
    {
        return "WHITE"
    }
    override fun getSuffix(): Component {
        return Component.text("")
    }
    override fun getPriority(): Int {
        return 0
    }
    override fun containsPlayer(uniqueId: UUID): Boolean {
        //Custom contains player logic
    }

    override fun containsPlayerFuture(uniqueId: UUID): CompletableFuture<Boolean> {
        //Custom async contains player logic
    }
}
````

### PrefixesActor
A `PrefixesActor` is responsible for applying, and unapplying groups and prefixes as well as suffixes from players. It also manages Chat-Message formatting.
To enable your custom `PrefixesActor`, you have to call the `setActor` function on the `PrefixesApi` object.

#### Example actor implementation
````kotlin
class MyPrefixesActor : PrefixesActor {
    override fun applyGroup(
        target: UUID,
        group: PrefixesGroup
    ) {
        //Custom group apply logic
    }

    override fun remove(target: UUID) {
        //Custom remove logic
    }

    override fun setPrefix(target: UUID, prefix: Component) {
        //Custom prefix apply logic
    }

    override fun setSuffix(target: UUID, suffix: Component) {
        //Custom suffix apply logic
    }

    override fun formatMessage(target: UUID, format: String, message: Component): Component {
        //Custom chat formatting logic
    }

    override fun setColor(target: UUID, color: String) {
        //Custom color apply logic
    }
}
````
### Custom plugin implementation
#### Creating API and indexing groups
To create an API object, you need to implement `PrefixesApiImpl`. From there, overwriting the `indexGroups` adds parsing groups from anywhere
##### Example API implementation
````kotlin
class MyApiImplementation : PrefixesApiImpl() {
    override fun indexGroups() {
        //Custom group parsing implementation
    }
}
````
From there, you can register this as a service provider in your custom plugin.
> **IMPORTANT**: You still need to register the events on your own! Take a look at the source code to see how this is done.
