# Harmony
Unofficial Mayhem client (mayhem.gg) wrapper that has:

- Mayhem Chat (receiving only, listener based)
- Mayhem GQL API (some of it; leaderboards, codes, and etc)

### Example
Establishing a session & adding a listener when a message is sent. (PubNub closes itself after
 the application is closed normally)
```kotlin
val session = SessionImpl(email, password, true)
session.playgrounds[0].channels[0].addListener {
    println(it.content)
}
```