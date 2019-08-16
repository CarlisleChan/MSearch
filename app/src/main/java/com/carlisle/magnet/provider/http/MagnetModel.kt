package com.carlisle.magnet.provider.http

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Result<T>(
        var success: Boolean,
        var message: String,
        var data: T
)

@Parcelize
data class MagnetRule(
        var site: String,
        var proxy: Boolean,
        var group: String,
        var magnet: String,
        var name: String,
        var size: String,
        var date: String,
        var hot: String,
        var host: String,
        var url: String,
        var paths: MagnetPathRule
) : Parcelable

@Parcelize
data class MagnetPathRule(
        var time: String,
        var size: String,
        var hot: String
) : Parcelable

data class MagnetResource(
        var magnet: String,
        var name: String,
        var nameHtml: String,
        var formatSize: String,
        var size: Long,
        var date: String,
        var hot: String,
        var detailUrl: String,
        var resolution: String
)

data class SearchRequest(
        var source: String = "",
        var keyword: String = "",
        var page: Int = 1,
        var sort: String = "time"
)

data class SearchResult(
        var results: List<MagnetResource>
)

data class Update(
        var title: String,
        var latestVersion: String,
        var latestVersionCode: Int,
        var download: String,
        var releaseNotes: List<String> = arrayListOf(),
        var force: Boolean
)