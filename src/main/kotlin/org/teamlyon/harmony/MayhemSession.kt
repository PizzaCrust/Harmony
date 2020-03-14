package org.teamlyon.harmony

interface MayhemSession {

    interface User {
        val slug: String
        val id: String
        val username: String
        val avatarUrl: String
        val discord: String?
        val fnUsername: String?
    }

    interface Myself: User {
        val apiToken: String
        val fnUserHash: String
        val email: String
    }

    val me: Myself

    data class ClientRelease(var version: String, var id: String)

    val latestBinaryRelease: ClientRelease
    val latestSupportedBinaryRelease: ClientRelease

    val playgrounds: List<Playground>

}