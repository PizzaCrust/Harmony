package org.teamlyon.harmony.impl

import org.teamlyon.harmony.*
import org.teamlyon.harmony.net.*

class PlaygroundImpl(private val apiToken: String, pgSlug: String): Playground {

    private val playgroundQuery: DetailedPlaygroundQuery = getMayhemRestService().queryFromStore(apiToken, StoredGPLQueries
            .DETAILED_PLAYGROUND_QUERY, DetailedPlaygroundQuery::class.java, Pair("playgroundSlug", pgSlug))

    class UserImpl(val userModel: UserModel): MayhemSession.User {
        override val slug: String
            get() = userModel.slug
        override val id: String
            get() = userModel.slug
        override val username: String
            get() = userModel.username
        override val avatarUrl: String
            get() = userModel.avatarUrl
        override val discord: String?
            get() = userModel.socialDiscord
        override val fnUsername: String?
            get() = TODO("Not yet implemented")

    }

    class ChannelImpl(private val channelModel: TextChannelModel): Playground.Channel {
        override val name: String
            get() = channelModel.name
        override val id: String
            get() = channelModel.id

        override fun addListener(listener: (Playground.Channel.Message) -> Unit) {
            if (chatService != null) {
                chatService!!.addListener(channelModel, listener)
            }
        }

        override val read: Boolean
            get() = channelModel.canIRead
        override val write: Boolean
            get() = channelModel.canIWrite
        override val rateLimit: Boolean
            get() = channelModel.canRateLimit
        override val rateLimitMs: Long
            get() {
                if (channelModel.rateLimitMs == null) return -1
                return channelModel.rateLimitMs!!
            }

    }

    override val channels: List<Playground.Channel>
        get() {
            val list = mutableListOf<Playground.Channel>()
            for (textChannel in playgroundQuery.playground.textChannels) {
                list.add(ChannelImpl(textChannel))
            }
            return list
        }
    override val name: String
        get() = playgroundQuery.playground.name
    override val id: String
        get() = playgroundQuery.playground.id
    override val description: String?
        get() = TODO("Not yet implemented")
    override val slug: String
        get() = playgroundQuery.playground.slug
    override val myselfKicked: Boolean
        get() = playgroundQuery.playground.amIKicked
    override val myselfBanned: Boolean
        get() = playgroundQuery.playground.amIBanned
    override val myselfMuted: Boolean
        get() = playgroundQuery.playground.amIMuted
    override val myselfRole: Playground.Role
    get() = playgroundQuery.playground.myPrimaryRole
    override val myselfPermissons: List<String>
        get() = playgroundQuery.playground.myPermissions
    override val roles: List<Playground.Role>
        get() = playgroundQuery.playground.roles.nodes
    override val members: Int
        get() = playgroundQuery.playground.membersCount
    override val creator: MayhemSession.User
        get() = UserImpl(playgroundQuery.playground.creator)
    override val lobbies: List<Lobby>
        get() {
            val lobbies = mutableListOf<Lobby>()
            for (node in playgroundQuery.playground.playgroundLobbies.nodes) {
                lobbies.add(LobbyImpl(apiToken, node))
            }
            return lobbies
        }
}

class LeaderboardConfigImpl(private val config: LobbyLeaderboardConfig): LobbyLeaderboardReference.Configuration {
    override val game: String
        get() = config.game
    override val scores: List<LobbyLeaderboardReference.Configuration.ScoringSystem>
        get() = config.scores
    override val partySize: String
        get() = config.conditions.party_size
    override val consensusMinimum: Int
        get() = config.consensus_minimum
    override val type: String
        get() = config.contest_type
    override val gameMode: String
        get() = config.game_mode

}

class ExtendedLeaderboardImpl(private val query: DetailedLeaderboardQuery): ExtendedLeaderboard {
    override val codes: List<ExtendedLeaderboard.LobbyCode>
        get() = query.lobbyLeaderboard.fortniteLobbyCodes.nodes
}

class LobbyLeaderboardReferenceImpl(private val token: String, private val lobby: LobbyLeaderboard):
        LobbyLeaderboardReference {
    override val id: String
        get() = lobby.id
    override val slug: String
        get() = lobby.slug
    override val participants: Int
        get() = lobby.participantsCount
    override val config: LobbyLeaderboardReference.Configuration
        get() = LeaderboardConfigImpl(lobby.config)

    override fun extend(): ExtendedLeaderboard {
        val resp = getMayhemRestService().queryFromStore(token, StoredGPLQueries
                .DETAILED_LEADERBOARD_QUERY, DetailedLeaderboardQuery::class.java, Pair("contestSlug", slug))
        return ExtendedLeaderboardImpl(resp)
    }

}

class LobbyImpl(private val token: String, private val lobbyModel: PlaygroundLobbyModel): Lobby {
    override val id: String
        get() = lobbyModel.id
    override val slug: String
        get() = lobbyModel.slug
    override val name: String
        get() = lobbyModel.name
    override val myselfCanJoin: Boolean
        get() = lobbyModel.canJoin
    override val currentLobbyLeaderboard: LobbyLeaderboardReference
        get() {
            return LobbyLeaderboardReferenceImpl(token, lobbyModel.currentLobbyLeaderboard)
        }

}