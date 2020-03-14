package org.teamlyon.harmony

interface Lobby {
    val id: String
    val slug: String
    val name: String

    val myselfCanJoin: Boolean

    val currentLobbyLeaderboard: LobbyLeaderboardReference
}

interface LobbyLeaderboardReference {
    //TODO Lobby codes

    val id: String
    val slug: String
    val participants: Int

    interface Configuration {
        val game: String

        data class ScoringDetails(val weight: Int,
                                  val type: String?,
                                  val multipliers: Map<String, Any>)

        data class ScoringManifest(val kills: ScoringDetails, val wins: ScoringDetails)

        data class ScoringSystem(val title: String, val manifest: ScoringManifest)

        val scores: List<ScoringSystem>

        val partySize: Int

        val consensusMinimum: Int
        val type: String
        val gameMode: String

    }

    val config: Configuration


}