package org.teamlyon.harmony

interface Playground {

    interface Channel {

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

}