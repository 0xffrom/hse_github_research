package hse_github_research.core

import hse_github_research.NORDVPN_PASS
import hse_github_research.NORDVPN_USER
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

data class NordVpnClient(val host: String, val port: Int) {

    companion object {

        val FIRST_CLIENT =
            NordVpnClient(AvailableNordVpnServers[0].first, AvailableNordVpnServers[0].second)

        val SECOND_CLIENT =
            NordVpnClient(AvailableNordVpnServers[1].first, AvailableNordVpnServers[1].second)
    }
}

fun CIOEngineConfig.withNordVpnProxy(client: NordVpnClient) {
    proxy = ProxyBuilder.socks("$NORDVPN_USER:$NORDVPN_PASS@${client.host}", client.port)
}

val AvailableNordVpnServers =
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
