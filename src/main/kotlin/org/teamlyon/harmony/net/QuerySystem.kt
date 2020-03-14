package org.teamlyon.harmony.net

import com.google.gson.Gson

enum class StoredGPLQueries(val resourcePath: String) {
    DETAILED_LEADERBOARD_QUERY("gplQueries/DetailedLeaderboardQuery"),
    DETAILED_PLAYGROUND_QUERY("gplQueries/DetailedPlaygroundQuery"),
    SESSION_INFO_QUERY("gplQueries/SessionInfoQuery");

    fun read(): String {
        return StoredGPLQueries::class.java.classLoader.getResource(this.resourcePath)!!
                .readText()
    }
}

fun <T> MayhemService.queryFromStore(token: String, query: StoredGPLQueries, clazz: Class<T>,
                                     vararg vars: Pair<String, Any>):
        T {
    val map = mutableMapOf<String, Any>()
    vars.forEach {
        map[it.first] = it.second
    }
    val query = this.queryService(MayhemService.GQLMayhemQuery(token, query.read(), map)).execute()
    return Gson().fromJson(query.body()!!.data, clazz)
}