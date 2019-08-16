package com.carlisle.magnet.provider.http

import android.content.Context
import com.carlisle.magnet.provider.helper.ServerHelper
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet

object HttpCenter {
    private val TAG = "HttpCenter"

    private var domain = ServerHelper.DEFAULT_SERVER
    private lateinit var sourceApi: String
    private lateinit var searchApi: String

    private val updateUrl = "https://raw.githubusercontent.com/CarlisleChan/MSearch/master/config/update.json"

    fun setup(context: Context) {
        domain = ServerHelper.getSelected(context)
        sourceApi = "$domain/api/source"
        searchApi = "$domain/api/search"
    }

    fun checkUpdate(success: (Update) -> Unit, failure: () -> Unit) {
        updateUrl.format().httpGet().responseObject<Update> { request, response, result ->
            result.fold(success = {
                success(it)
            }, failure = {
                failure()
            })
        }
    }

    fun fetchSource(success: (List<MagnetRule>) -> Unit, failure: (String?) -> Unit) {
        sourceApi.httpGet().responseObject<Result<List<MagnetRule>>> { request, response, result ->
            result.fold(success = {
                success(it.data)
            }, failure = {
                failure(it.message)
            })
        }
    }

    fun search(request: SearchRequest, success: (SearchResult) -> Unit, failure: (String?) -> Unit) {
        val parameters = listOf(
                "source" to request.source,
                "keyword" to request.keyword,
                "page" to request.page,
                "sort" to request.sort
        )
        searchApi.httpGet(parameters).responseObject<Result<SearchResult>> { request, response, result ->
            result.fold(success = {
                if (it.success) {
                    success(it.data)
                } else {
                    failure(it.message)
                }
            }, failure = {
                failure(it.message)
            })
        }
    }
}