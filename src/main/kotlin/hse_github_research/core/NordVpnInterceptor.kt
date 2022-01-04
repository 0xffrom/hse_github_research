package hse_github_research.core

import hse_github_research.NORDVPN_PASS
import hse_github_research.NORDVPN_USER
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

fun CIOEngineConfig.withNordVpnProxy() {
    val (host, port) = AvailableServers.random()

    proxy = ProxyBuilder.http("socks5://$NORDVPN_USER:$NORDVPN_PASS@$host:$port")
}

val AvailableServers =
    listOf(
        "amsterdam.nl.socks.nordhold.net" to 1080,
        "atlanta.us.socks.nordhold.net" to 1080,
        "dallas.us.socks.nordhold.net" to 1080,
        "dublin.ie.socks.nordhold.net" to 1080,
        "ie.socks.nordhold.net" to 1080,
        "los-angeles.us.socks.nordhold.net" to 1080,
        "nl.socks.nordhold.net" to 1080,
        "se.socks.nordhold.net" to 1080,
        "stockholm.se.socks.nordhold.net" to 1080,
        "us.socks.nordhold.net " to 1080,
    )
