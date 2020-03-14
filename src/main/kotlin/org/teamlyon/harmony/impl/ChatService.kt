package org.teamlyon.harmony.impl

import com.google.gson.Gson
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNMembershipResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSpaceResult
import com.pubnub.api.models.consumer.pubsub.objects.PNUserResult
import org.teamlyon.harmony.MayhemSession
import org.teamlyon.harmony.Playground
import org.teamlyon.harmony.net.SessionInfoQuery
import org.teamlyon.harmony.net.TextChannelModel
import org.teamlyon.harmony.net.UserModel
import java.lang.UnsupportedOperationException

/*
    Keys to access Mayhem chat service
 */
const val PUB_KEY: String = "16007599-3ad8-4766-87a0-ef5f02afb75b"
const val SUB_KEY: String = "e0c40c28-f1da-11e9-ad72-8e6732c0d56b"

var chatService: ChatService? = null

fun getChat(sessionInfo: SessionInfoQuery): ChatService {
    if (chatService == null) {
        chatService = ChatService(sessionInfo)
    }
    return chatService!!
}

class ChatService(sessionInfo: SessionInfoQuery) {

    val pb = PubNub(PNConfiguration()
            .setPublishKey(PUB_KEY)
            .setSubscribeKey(SUB_KEY)
            .setUuid(sessionInfo.me.slug)
            .setSecure(true))

    data class MessageJson(var version: String,
                           var id: String,
                           var sender: UserModel,
                           var content: String,
                           var sentAt: Long)

    init {
        if (chatService != null) throw UnsupportedOperationException()
    }

    class MessageImpl(private val json: MessageJson): Playground.Channel.Message {
        override val version: String
            get() = json.version
        override val id: String
            get() = json.id
        override val sender: MayhemSession.User
            get() = PlaygroundImpl.UserImpl(json.sender)
        override val content: String
            get() = json.content
        override val sentAt: Long
            get() = json.sentAt
    }

    class CListener(val channelName: String,
                    val body: (Playground.Channel.Message) -> Unit): SubscribeCallback() {
        override fun signal(p0: PubNub, p1: PNSignalResult) {}
        override fun status(p0: PubNub, p1: PNStatus) {}
        override fun user(p0: PubNub, p1: PNUserResult) {}
        override fun messageAction(p0: PubNub, p1: PNMessageActionResult) {}
        override fun presence(p0: PubNub, p1: PNPresenceEventResult) {}
        override fun membership(p0: PubNub, p1: PNMembershipResult) {}
        override fun space(p0: PubNub, p1: PNSpaceResult) {}
        override fun message(p0: PubNub, p1: PNMessageResult) {
            if (p1.channel ==  channelName) {
                val obj = Gson().fromJson(p1.message, MessageJson::class.java)
                body(MessageImpl(obj))
            }
        }
    }

    val listeners: MutableList<CListener> = mutableListOf()
    val channelsSubscribed: MutableList<String> = mutableListOf()

    fun addListener(channel: TextChannelModel, listener: (Playground.Channel.Message) -> Unit) {
        channelsSubscribed.add(channel.pubnubMessageChannelName)
        val listener = CListener(channel.pubnubMessageChannelName, listener)
        listeners.add(listener)
        pb.addListener(listener)
        pb.subscribe().channels(listOf(channel.pubnubMessageChannelName)).execute()
    }

    fun close() {
        pb.unsubscribeAll()
        listeners.forEach {
            pb.removeListener(it)
        }
        pb.destroy()
    }

    fun closeOnShutdown() {
        Runtime.getRuntime().addShutdownHook(Thread {
            close()
        })
    }

}