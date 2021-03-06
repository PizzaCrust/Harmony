package org.teamlyon.harmony

interface Lobby {
    val id: String
    val slug: String
    val name: String

    val myselfCanJoin: Boolean

    val currentLobbyLeaderboard: LobbyLeaderboardReference
}

interface LobbyLeaderboardReference {

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

        val partySize: String

        val consensusMinimum: Int
        val type: String
        val gameMode: String

    }

    val config: Configuration

    fun extend(): ExtendedLeaderboard // pulls specifically for contest to receive codes etc


}

interface ExtendedLeaderboard {

    data class LobbyCode(val name: String,
                         val code: String,
                         val createdAt: Long,
                         val startedAt: Long,
                         val region: String,
                         val description: String,
                         val creator: MayhemSession.User)

    val codes: List<LobbyCode>

}