package org.teamlyon.harmony.impl

import org.teamlyon.harmony.MayhemSession
import org.teamlyon.harmony.Playground
import org.teamlyon.harmony.net.*
import java.lang.RuntimeException
import java.lang.UnsupportedOperationException
import java.util.*

class SessionImpl(email: String, password: String, enabledChat: Boolean = false): MayhemSession {

    val apiToken: String
    val sessionInfo: SessionInfoQuery

    init {
        val resp = getMayhemRestService()
                .login(email, password, TimeZone.getDefault().toString()).execute()
        if (!resp.isSuccessful) {
            throw RuntimeException("Failed to login")
        }
        apiToken = resp.body()!!.api_token!!
        sessionInfo = getMayhemRestService().queryFromStore(apiToken, StoredGPLQueries.SESSION_INFO_QUERY,
                SessionInfoQuery::class.java)
        if (enabledChat) {
            getChat(sessionInfo).closeOnShutdown()
        }
    }

    class MyselfImpl(val info: SessionInfoQuery, val token: String): MayhemSession.Myself {
        override val slug: String
            get() = info.me.slug
        override val id: String
            get() = info.me.id
        override val username: String
            get() = info.me.username
        override val avatarUrl: String
            get() = info.me.avatarUrl
        override val discord: String?
            get() = info.me.socialDiscord
        override val fnUsername: String?
            get() = throw UnsupportedOperationException()
        override val apiToken: String
            get() = token
        override val fnUserHash: String
            get() = info.me.fnUserHash
        override val email: String
            get() = info.me.email
    }

    override val me: MayhemSession.Myself
        get() = MyselfImpl(this.sessionInfo, this.apiToken)
    override val latestBinaryRelease: MayhemSession.ClientRelease
        get() = sessionInfo.latestBinaryRelease
    override val latestSupportedBinaryRelease: MayhemSession.ClientRelease
        get() = sessionInfo.latestSupportedBinaryRelease
    override val playgrounds: List<Playground>
        get() {
            val list = mutableListOf<Playground>()
            for (joinedPlayground in sessionInfo.me.joinedPlaygrounds) {
                // playground here is just basic, use the slug to get more info
                list.add(PlaygroundImpl(apiToken, joinedPlayground.slug))
            }
            return list
        }


}