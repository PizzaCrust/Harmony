package org.teamlyon.harmony

interface Playground {

    interface Channel {
        //TODO

        val name: String
        val id: String

        interface Message {
            val version: String
            val id: String
            val sender: MayhemSession.User
            val content: String
            val sentAt: Long
        }

        fun addListener(listener: (Message) -> Unit)

        val read: Boolean
        val write: Boolean

        val rateLimit: Boolean
        val rateLimitMs: Long

    }

    val channels: List<Channel>

    val name: String
    val id: String
    val description: String?
    val slug: String

    data class Role(val id: String, val name: String, val color: String?)

    val myselfKicked: Boolean
    val myselfBanned: Boolean
    val myselfMuted: Boolean
    val myselfRole: Role
    val myselfPermissons: List<String>

    val roles: List<Role>

    val members: Int

    val creator: MayhemSession.User

    val lobbies: List<Lobby>

}