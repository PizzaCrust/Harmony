package org.teamlyon.harmony.net

import org.teamlyon.harmony.ExtendedLeaderboard
import org.teamlyon.harmony.LobbyLeaderboardReference
import org.teamlyon.harmony.MayhemSession
import org.teamlyon.harmony.Playground

data class ListNode<T>(var nodes: List<T>)

data class ConditionsModel(var party_size: String)

data class LobbyLeaderboardConfig(var game: String,
                                  var scores: LobbyLeaderboardReference.Configuration.ScoringSystem,
                                  var conditions: ConditionsModel,
                                  var consensus_minimum: Int,
                                  var contest_type: String,
                                  var game_mode: String)

data class LobbyLeaderboard(var id: String,
                            var slug: String,
                            var config: LobbyLeaderboardConfig,
                            var participantsCount: Int,
                            var fortniteLobbyCodes: ListNode<ExtendedLeaderboard.LobbyCode> // DLQ
)

data class PlaygroundLobbyModel(var id: String,
                                var slug: String,
                                var name: String,
                                var canJoin: Boolean,
                                var currentLobbyLeaderboard: LobbyLeaderboard)

data class TextChannelModel(var id: String,
                            var name: String,
                            var canJoin: Boolean,
                            var pubnubMessageChannelName: String,
                            var pubnubMessageSignalChannelName: String,
                            var canIRead: Boolean,
                            var canIWrite: Boolean,
                            var canRateLimit: Boolean,
                            var rateLimitMs: Long?)

data class PlaygroundModel(var id: String,
                           var slug: String,
                           var modActionChannelName: String,
                           var name: String,
                           var avatarUrl: String,
                           var myPermissions: List<String>, // DPQ
                           var roles: ListNode<Playground.Role>,
                           var amIKicked: Boolean,
                           var amIBanned: Boolean,
                           var playgroundLobbies: ListNode<PlaygroundLobbyModel>,
                           var creator: UserModel,
                           var amIMuted: Boolean,
                           var myPrimaryRole: Playground.Role,
                           var membersCount: Int,
                           var textChannels: List<TextChannelModel>)

data class UserModel(var slug: String,
                     var username: String,
                     var channelGroupName: String,
                     var fnUserHash: String,
                     var joinedPlaygrounds: List<PlaygroundModel>,
                     var id: String,
                     var pubnubMessageChannelName: String,
                     var avatarUrl: String,
                     var email: String,
                     var socialDiscord: String)

data class SessionInfoQuery(var latestBinaryRelease: MayhemSession.ClientRelease,
                            var latestSupportedBinaryRelease: MayhemSession.ClientRelease,
                            var me: UserModel)