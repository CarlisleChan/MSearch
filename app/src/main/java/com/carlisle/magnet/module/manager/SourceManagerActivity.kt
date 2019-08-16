package com.carlisle.magnet.module.manager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlisle.magnet.R
import com.carlisle.magnet.module.base.BaseActivity
import com.carlisle.magnet.provider.helper.SourceHelper
import com.carlisle.magnet.provider.http.HttpCenter
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_source_manager.*
import org.jetbrains.anko.toast

class SourceManagerActivity : BaseActivity() {

    companion object {
        val BUNDLE_NEED_RECREATE = "bundle_need_recreate"
    }

    private lateinit var emptyView: View
    private lateinit var errorView: View
    private lateinit var loadingView: View

    private lateinit var adapter: SourceAdapter
    private var needRecreate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source_manager)

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }

        setupView()
        load()
    }

    private fun setupView() {
        emptyView = layoutInflater.inflate(R.layout.view_empty, recycler_view.parent as ViewGroup, false)
        errorView = layoutInflater.inflate(R.layout.view_error, recycler_view.parent as ViewGroup, false)
        loadingView = layoutInflater.inflate(R.layout.view_loading, recycler_view.parent as ViewGroup, false)
        errorView.setOnClickListener { load() }

        adapter = SourceAdapter(SourceHelper.getSelected(this).associateBy { it.site }.toMutableMap())
        adapter.emptyView = loadingView
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        adapter.isFirstOnly(false)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
    }

    private fun load() {
        HttpCenter.fetchSource(success = {
            adapter.setNewData(it)
        }, failure = {
            adapter.emptyView = errorView
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_source, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                needRecreate = true
                val selected = adapter.data.intersect(adapter.selected.values.toList()).toList()
                SourceHelper.saveSelected(this, selected)
                toast("保存成功")
                finish()
            }
        }
        return true
    }

    override fun finish() {
        val intent = Intent()
        intent.putExtra(BUNDLE_NEED_RECREATE, needRecreate)
        setResult(RESULT_OK, intent)
        super.finish()
    }

}