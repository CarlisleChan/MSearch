package com.carlisle.magnet.module.search

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlisle.magnet.R
import com.carlisle.magnet.module.base.BaseActivity
import com.carlisle.magnet.module.base.LazyFragment
import com.carlisle.magnet.provider.http.HttpCenter
import com.carlisle.magnet.provider.http.MagnetResource
import com.carlisle.magnet.provider.http.MagnetRule
import com.carlisle.magnet.provider.http.SearchRequest
import com.carlisle.magnet.support.utils.ClipboardUtils
import com.carlisle.seed.support.extension.hideSoftKeyboard
import com.carlisle.seed.support.extension.magnet
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.support.v4.share
import org.jetbrains.anko.support.v4.toast

class SearchFragment : LazyFragment(), BaseQuickAdapter.RequestLoadMoreListener {
    private val TAG = "SearchFragment"

    private lateinit var emptyView: View
    private lateinit var errorView: View
    private lateinit var loadingView: View

    private var adapter: SearchAdapter = SearchAdapter()
    private var request = SearchRequest()

    private lateinit var rule: MagnetRule
    private var searchContent = ""
    private var needReload = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onUserVisible() {
        super.onUserVisible()
        if (needReload) {
            reload()
        }
    }

    private fun setupViews() {
        rule = arguments?.getParcelable<MagnetRule>("rule")!!

        emptyView = activity!!.layoutInflater.inflate(R.layout.view_empty, recycler_view.parent as ViewGroup, false)
        errorView = activity!!.layoutInflater.inflate(R.layout.view_error, recycler_view.parent as ViewGroup, false)
        loadingView = activity!!.layoutInflater.inflate(R.layout.view_loading, recycler_view.parent as ViewGroup, false)
        errorView.setOnClickListener { reload() }

        adapter.emptyView = emptyView
        adapter.isFirstOnly(false)
        adapter.setLoadMoreView(SimpleLoadMoreView())
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        adapter.setOnLoadMoreListener(this, recycler_view)
        adapter.setOnItemChildClickListener { adapter, view, position ->
            val resource = adapter.data[position] as MagnetResource
            when (view.id) {
                R.id.ll_root_view -> handleCopyAction(resource)
                R.id.iv_more -> showPopMenu(view, resource)
            }
        }

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = adapter
    }

    private fun showPopMenu(view: View, resource: MagnetResource) {
        val popupMenu = PopupMenu(ContextThemeWrapper(activity!!, R.style.PopupMenuStyle), view, Gravity.RIGHT)
        popupMenu.inflate(R.menu.menu_resource_item)
        popupMenu.menu.findItem(R.id.action_copy).setOnMenuItemClickListener { handleCopyAction(resource) }
        popupMenu.menu.findItem(R.id.action_download).setOnMenuItemClickListener { context?.magnet(resource.magnet)!! }
        popupMenu.menu.findItem(R.id.action_share).setOnMenuItemClickListener { share(resource.name, resource.magnet) }
        popupMenu.show()
    }

    private fun handleCopyAction(resource: MagnetResource): Boolean {
        ClipboardUtils.copy(activity!!, resource.magnet)
        Snackbar.make(recycler_view, "已复制磁力链接", Snackbar.LENGTH_SHORT).show()
        return true
    }

    fun forceReload(content: String = searchContent) {
        this.searchContent = content
        if (isVisibleToUser) {
            reload()
        } else {
            needReload = true
        }
    }

    private fun reload() {
        needReload = false
        (activity as BaseActivity).hideSoftKeyboard()
        adapter.emptyView = loadingView
        adapter.data.clear()
        adapter.notifyDataSetChanged()
        load()
    }

    private fun load(isLoadMore: Boolean = false) {
        if (searchContent.isNullOrEmpty()) {
            toast("搜索内容不能为空!")
            return
        }

        request.keyword = searchContent
        request.source = rule.site
        if (isLoadMore) {
            request.page += 1
        } else {
            request.page = 1
        }

        HttpCenter.search(request, success = {
            if (request.page == 1) {
                if (it.results.isEmpty()) {
                    adapter.emptyView = emptyView
                } else {
                    adapter.setNewData(it.results)
                }
            } else {
                if (it.results.isEmpty()) {
                    adapter.loadMoreEnd()
                } else {
                    adapter.addData(it.results)
                    adapter.loadMoreComplete()
                }
            }
        }, failure = {
            if (request.page == 1) {
                adapter.emptyView = errorView
            } else {
                adapter.loadMoreFail()
            }
        })
    }

    override fun onLoadMoreRequested() {
        load(true)
    }

}