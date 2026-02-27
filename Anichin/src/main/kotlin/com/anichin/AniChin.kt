package com.anichin

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class AniChin : MainAPI() {

    override var name = "AniChin"
    override var mainUrl = "https://anichin.care"
    override var lang = "id"

    override val supportedTypes = setOf(
        TvType.Anime
    )

    override val hasMainPage = true

    override val mainPage = mainPageOf(
        "$mainUrl/ongoing/" to "Ongoing",
        "$mainUrl/completed/" to "Completed",
        "$mainUrl/movie/" to "Movie",
        "$mainUrl/" to "Update Terbaru"
    )

    // =========================
    // HOMEPAGE
    // =========================

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {

        val url = request.data + "page/$page"
        val document = app.get(url).document

        val home = document.select("article")

        val items = home.mapNotNull {
            val title = it.selectFirst("h2")?.text() ?: return@mapNotNull null
            val link = it.selectFirst("a")?.attr("href") ?: return@mapNotNull null
            val poster = it.selectFirst("img")?.attr("src")

            newAnimeSearchResponse(title, link) {
                this.posterUrl = poster
            }
        }

        return newHomePageResponse(
            request.name,
            items
        )
    }
}

override suspend fun search(query: String): List<SearchResponse> {
    val url = "$mainUrl/?s=$query"
    val document = app.get(url).document

    return document.select("article").mapNotNull {
        val title = it.selectFirst("h2")?.text() ?: return@mapNotNull null
        val link = it.selectFirst("a")?.attr("href") ?: return@mapNotNull null
        val poster = it.selectFirst("img")?.attr("src")

        newAnimeSearchResponse(title, link) {
            this.posterUrl = poster
        }
    }
}


override suspend fun load(url: String): LoadResponse {
    val document = app.get(url).document

    val title = document.selectFirst("h1")?.text() ?: ""
    val poster = document.selectFirst(".thumb img")?.attr("src")
    val desc = document.selectFirst(".entry-content")?.text()

    val episodes = document.select(".episodelist li a").map {
        val name = it.text()
        val link = it.attr("href")

        Episode(
            data = link,
            name = name
        )
    }

    return newAnimeLoadResponse(title, url, TvType.Anime) {
        this.posterUrl = poster
        this.plot = desc
        this.episodes = episodes
    }
}


override suspend fun loadLinks(
    data: String,
    isCasting: Boolean,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
): Boolean {

    val document = app.get(data).document

    val iframe = document.selectFirst("iframe")?.attr("src") ?: return false

    loadExtractor(iframe, data, subtitleCallback, callback)

    return true
}