package com.carlisle.magnet.module.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.carlisle.magnet.R
import com.carlisle.magnet.module.about.AboutActivity
import com.carlisle.magnet.module.base.BaseActivity
import com.carlisle.magnet.module.manager.SourceManagerActivity
import com.carlisle.magnet.module.manager.SourceManagerActivity.Companion.BUNDLE_NEED_RECREATE
import com.carlisle.magnet.provider.helper.ServerHelper
import com.carlisle.magnet.provider.helper.SourceHelper
import com.carlisle.magnet.provider.helper.UpdateHelper
import com.carlisle.magnet.support.utils.SimpleFileUtils
import com.carlisle.magnet.support.widget.FragmentAdapter
import com.carlisle.seed.support.extension.instanceOf
import com.carlisle.seed.support.extension.market
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.email
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult

class SearchActivity : BaseActivity() {
    private val TAG = "SearchActivity"

    private val REQUEST_CODE_MARKET = 0x0603

    private lateinit var fragmentAdapter: FragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        setupSearchView()
        setupViewPager()
        UpdateHelper.checkUpdate(this)
    }

    private fun setupSearchView() {
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId === EditorInfo.IME_ACTION_SEARCH) {
                load()
            }
            true
        }
    }

    private fun setupViewPager() {
        val rules = SourceHelper.getSelected(this)
        val fragments = arrayListOf<SearchFragment>()
        val titles = arrayListOf<String>()
        rules.forEach {
            fragments.add(instanceOf<SearchFragment>("rule" to it))
            titles.add(it.site)
        }

        fragmentAdapter = FragmentAdapter(supportFragmentManager, fragments, titles)
        view_pager.offscreenPageLimit = fragments.size
        view_pager.adapter = fragmentAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun load() {
        val searchContent = et_search.text.toString().trim()
        for (i in 0 until fragmentAdapter.count) {
            (fragmentAdapter.getItem(i) as SearchFragment).forceReload(searchContent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_MARKET -> {
                val needRecreate = data?.getBooleanExtra(BUNDLE_NEED_RECREATE, false)!!
                if (needRecreate) {
                    setupViewPager()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_server -> ServerHelper.showServerChangeDialog(this)
            R.id.action_source_manager -> startActivityForResult<SourceManagerActivity>(REQUEST_CODE_MARKET)
            R.id.action_score -> market()
            R.id.action_share -> share(getString(R.string.app_name), "下载地址: ${getString(R.string.coolapk)}")
            R.id.action_feedback -> email(getString(R.string.email), "有关 ${getString(R.string.app_name)} 的反馈")
            R.id.action_about -> startActivity<AboutActivity>()
            R.id.action_guide -> showGuideDialog()
        }
        return true
    }

    private fun showGuideDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.DialogStyle))
        builder.setTitle("说明")
        builder.setMessage(SimpleFileUtils.readStringFromAssets(this@SearchActivity, "guide.txt"))
        builder.setPositiveButton("确定") { _, _ -> }
        builder.show()
    }

}