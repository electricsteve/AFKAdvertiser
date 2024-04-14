# AFKAdvertiser
AFKAdvertiser is a Minecraft mod which sends a random chat message from a list on an interval.
## Dependencies
The mod depends on [Fabric-Api](https://modrinth.com/mod/fabric-api).
## Configuration
To configure the mod, put the mod in your mods folder and launch the game. A txt file will be created in `.minecraft\config` named `afkadvertiser.txt`. This txt file will have the following contents by default:
```
60
First Line
Second Line
```
The first line is the interval in seconds between messages, all the following lines are the messages which are randomly selected, there can be an infinite number of messages (well, actually 2,147,483,647).
When in-game, toggle it with /afkadvertiser.
## Issues
If you have issues or found a bug, create an issue on github.
## Contributing
If you want a feature to be added or want to add something yourself, please first open a feature request on github.
