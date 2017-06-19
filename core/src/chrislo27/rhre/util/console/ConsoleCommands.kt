package chrislo27.rhre.util.console

import chrislo27.rhre.Main
import chrislo27.rhre.registry.*
import chrislo27.rhre.util.JsonHandler
import com.badlogic.gdx.Gdx
import java.util.*

object ConsoleCommands {

	fun handle(main: Main, command: String, args: List<String>): Boolean {
		return when (command.toLowerCase(Locale.ROOT)) {
			"quit", "exit" -> {
				Gdx.app.exit()
				true
			}
			"dumpids" -> {
				val filteredGames = GameRegistry.gameList.filter { it.series != Series.CUSTOM }
				val allIDs: String = JsonHandler.toJson(IDDump(
						filteredGames.map(Game::id),
						filteredGames.flatMap(Game::soundCues).map(SoundCue::id),
						filteredGames.flatMap(Game::patterns).filter { !it.autoGenerated }.map(Pattern::id)
															  ))

				println("Write to preferences? (Y/N)")
				if ((if (args.firstOrNull()?.equals("w", ignoreCase = true) ?: false)
					"y"
				else
					(readLine() ?: throw IllegalArgumentException("Got null for yes/no")))
						.equals("y", ignoreCase = true)) {
					println("Writing to preferences...")
					main.preferences.putString("idDump", allIDs).flush()
				}

				println("\n" + allIDs + "\n\n")

				false
			}
			"checkids" -> {
				val json = main.preferences.getString("idDump", null) ?: throw IllegalStateException(
						"Cached ID dump is null")
				val list: IDDump = JsonHandler.fromJson(json)

				println("Checking game list")
				list.games.forEach {
					if (GameRegistry[it] == null) {
						println("[GAME] Not found: $it")
					}
				}
				println("\nChecking SFX list")
				list.sfx.forEach {
					if (GameRegistry.getCue(it) == null) {
						println("[SFX] Not found: $it")
					}
				}
				println("\nChecking pattern list")
				list.patterns.forEach {
					if (GameRegistry.getPattern(it) == null) {
						println("[PATTERN] Not found: $it")
					}
				}
				println()

				println("Complete")

				false
			}

			else -> {
				println("Commands: quit/exit, help/?, dumpids [w], checkids")
				false
			}
		}
	}

	private data class IDDump(var games: List<String>, var sfx: List<String>, var patterns: List<String>)

}