package com.anichin

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class AniChinPlugin : Plugin() {
    override fun load() {
        registerMainAPI(AniChin())
    }
}