package ai.app.notifair

import ai.app.notifair.adapters.EventListAdapter
import ai.app.notifair.adapters.RecentAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var drawer: Advance3DDrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initRecyclers()
        initNavigationViews()

        val tvShowAll: TextView = findViewById(R.id.tv_show_all)
        tvShowAll.setOnClickListener {
            val bottomSheetFragment = EventsShowAllFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        val fabNotification: FloatingActionButton = findViewById(R.id.fab_right_notification)
        fabNotification.setOnClickListener {
            openNotificationsBottomSheet()
        }

        val fabLeftDrawer: FloatingActionButton = findViewById(R.id.fab_left_drawer)
        fabLeftDrawer.setOnClickListener {
            openDrawerMenu()
        }

        val fabMaps = findViewById<View>(R.id.fabMaps) as FloatingActionButton
        fabMaps.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    private fun initNavigationViews() {
        val itemHome = findViewById<View>(R.id.card_view_item_home) as CardView
        val itemChannel = findViewById<View>(R.id.card_view_item_channel) as CardView
        val itemNews = findViewById<View>(R.id.card_view_item_news) as CardView
        val itemBookmarks = findViewById<View>(R.id.card_view_item_bookmarks) as CardView
        val itemHelp = findViewById<View>(R.id.card_view_item_help) as CardView
        val itemReminders = findViewById<View>(R.id.card_view_item_reminders) as CardView
        val itemHistory = findViewById<View>(R.id.card_view_item_history) as CardView
        val itemAccount = findViewById<View>(R.id.card_view_item_accounts) as CardView
        val itemControlCenter = findViewById<View>(R.id.card_view_item_control_center) as CardView
        val itemLogout = findViewById<View>(R.id.card_view_item_logout) as CardView

        itemHistory.setOnClickListener {
//            drawer!!.closeDrawer(GravityCompat.START)
            openHistoryBottomSheet()
        }


        itemAccount.setOnClickListener {
//            drawer!!.closeDrawer(GravityCompat.START)
            openAccountBottomSheet()
        }

        itemChannel.setOnClickListener {
//            drawer!!.closeDrawer(GravityCompat.START)
            openChannelBottomSheet()
        }

        itemNews.setOnClickListener {
//            drawer!!.closeDrawer(GravityCompat.START)
            openNewsBottomSheet()
        }


        val mNavigationView = findViewById<View>(R.id.nav_view_drawer) as NavigationView
        mNavigationView.setNavigationItemSelectedListener(this)
    }

    private fun initRecyclers() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val adapter = EventListAdapter()
        recyclerView.adapter = adapter


        val recyclerViewVertical: RecyclerView = findViewById(R.id.recycler_view1)
        val layoutManagerVertical = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewVertical.layoutManager = layoutManagerVertical
        val adapter1 = RecentAdapter()
        recyclerViewVertical.adapter = adapter1
    }

    private fun openNotificationsBottomSheet() {

        val notificationBottomSheetFragment = NotificationsListFragment()
        notificationBottomSheetFragment.show(
            supportFragmentManager,
            notificationBottomSheetFragment.tag
        )
    }

    private fun openHistoryBottomSheet() {

        val historyBottomSheetFragment = HistoryFragment()
        historyBottomSheetFragment.show(
            supportFragmentManager,
            historyBottomSheetFragment.tag
        )
    }

    private fun openAccountBottomSheet() {

        val accountBottomSheetFragment = AccountFragment()
        accountBottomSheetFragment.show(
            supportFragmentManager,
            accountBottomSheetFragment.tag
        )
    }

    private fun openChannelBottomSheet() {

        val channelBottomSheetFragment = ChannelFragment()
        channelBottomSheetFragment.show(
            supportFragmentManager,
            channelBottomSheetFragment.tag
        )
    }

    private fun openNewsBottomSheet() {

        val newsBottomSheetFragment = NewsFragment()
        newsBottomSheetFragment.show(
            supportFragmentManager,
            newsBottomSheetFragment.tag
        )
    }

    private fun openDrawerMenu() {

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        drawer = findViewById<View>(R.id.drawer_layout) as Advance3DDrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<View>(R.id.nav_view_drawer) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        drawer!!.setViewScale(GravityCompat.START, 0.96f)
//        drawer!!.setRadius(GravityCompat.START, 20f)
        drawer!!.setViewElevation(GravityCompat.START, 8f)
        drawer!!.setViewRotation(GravityCompat.START, 15f)

        drawer!!.open()

    }


    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_right_drawer -> {
                drawer!!.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }


}