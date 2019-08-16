package com.carlisle.magnet.module.about

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.carlisle.magnet.R
import com.carlisle.magnet.module.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.*
import moe.feng.alipay.zerosdk.AlipayZeroSdk
import org.jetbrains.anko.browse
import org.jetbrains.anko.toast

class AboutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_heart -> showDonate()
            R.id.action_github -> browse(getString(R.string.github))
        }
        return true
    }

    private fun showDonate() {
        if (!AlipayZeroSdk.hasInstalledAlipayClient(this)) {
            toast("你还没有安装支付宝 ¯﹃¯")
        }
        AlipayZeroSdk.startAlipayClient(this, "fkx08522gehlg60pon96vf5")
    }
}