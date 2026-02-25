# AFKAdvertiser
![Dynamic JSON Badge](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fapi.modrinth.com%2Fv2%2Fproject%2FsMCVKnbd&query=%24.downloads&style=flat&logo=modrinth&label=Downloads&labelColor=black&color=yellow)

AFKAdvertiser is a Minecraft mod which sends a random chat message from a list on an interval.
## Dependencies
The mod depends on [Fabric-Api](https://modrinth.com/mod/fabric-api).
## Configuration
To configure the mod, put the mod in your mods folder and launch the game. A txt file will be created in `.minecraft\config` named `afkadvertiser.txt`. This txt file will have the following contents by default:
```
60
10
First Line
Second Line
```
The first line is the interval in seconds between messages, the second is the random offest in seconds, all the following lines are the messages which are randomly selected, there can be an infinite number of messages (well, actually 2,147,483,647).
When in-game, toggle it with /afkadvertiser.
### Timing
How the mod internally works is that a task is run after `interval` seconds, this task then adds an extra random amount of seconds between zero and `random_offset` (a float, so not alligned to the second), after that extra time has elapsed, it sends a random message.
So the time between messages is `interval+random(0,random_offset)`.
## Issues
If you have issues or found a bug, create an issue on GitHub.
## Contributing
If you want a feature to be added or want to add something yourself, please first open a feature request on GitHub.
