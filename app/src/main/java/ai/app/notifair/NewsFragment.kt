package ai.app.notifair

import ai.app.notifair.news.NotifAir
import ai.app.notifair.news.adapter.NewsChannelsListAdapter
import ai.app.notifair.news.adapter.DataAdapter
import ai.app.notifair.news.model.ArticleStructure
import ai.app.notifair.news.model.Constants
import ai.app.notifair.news.model.NewsResponse
import ai.app.notifair.news.network.ApiClient
import ai.app.notifair.news.network.ApiInterface
import ai.app.notifair.news.network.interceptors.OfflineResponseCacheInterceptor
import ai.app.notifair.news.network.interceptors.ResponseCacheInterceptor
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
//import android.support.v7.app.AlertDialog
//import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.mikepenz.materialdrawer.AccountHeader
//import com.mikepenz.materialdrawer.AccountHeaderBuilder
//import com.mikepenz.materialdrawer.Drawer
//import com.mikepenz.materialdrawer.DrawerBuilder
//import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
//import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
//import com.mikepenz.materialdrawer.model.SectionDrawerItem
//import com.mikepenz.materialdrawer.model.interfaces.Nameable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : BottomSheetDialogFragment(), SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val SOURCE_ARRAY = arrayOf(
        "google-news-in",
        "bbc-news",
        "the-hindu",
        "the-times-of-india",
        "buzzfeed",
        "mashable",
        "mtv-news",
        "bbc-sport",
        "espn-cric-info",
        "talksport",
        "medical-news-today",
        "national-geographic",
        "crypto-coins-news",
        "engadget",
        "the-next-web",
        "the-verge",
        "techcrunch",
        "techradar",
        "ign",
        "polygon"
    )
    private var SOURCE: String? = null

    private var articleStructure: ArrayList<ArticleStructure> = ArrayList<ArticleStructure>()
    private var adapter: DataAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
//    private var result: Drawer? = null
//    private var accountHeader: AccountHeader? = null
    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var listState: Parcelable? = null
    private var montserrat_regular: Typeface? = null
    private var mTitle: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view:View = inflater.inflate(R.layout.fragment_news, container, false)

        SOURCE = SOURCE_ARRAY[1]

        createRecyclerView(view)
//        onLoadingSwipeRefreshLayout()

        return view
    }

//    private fun createToolbar() {
//        toolbar = view?.findViewById(R.id.toolbar_main_activity)
////        setSupportActionBar(toolbar)
////        if (getSupportActionBar() != null) {
////            getSupportActionBar().setDisplayShowTitleEnabled(false)
////        }
////        mTitle = view?.findViewById(R.id.toolbar_title)
////        mTitle!!.setTypeface(montserrat_regular)
//    }

    private fun createRecyclerView(view:View) {

        val recyclerViewNewsChannels: RecyclerView? = view.findViewById(R.id.news_channels_recycler_view)
        val layoutManagerNewsChannels = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNewsChannels?.layoutManager = layoutManagerNewsChannels
        ///////////////////////////////////////////TODO Change Adapter
        val recyclerViewNewsChannelsAdapter = NewsChannelsListAdapter(SOURCE_ARRAY)
        recyclerViewNewsChannels?.adapter = recyclerViewNewsChannelsAdapter
        ///////////////////////////////////////////


        recyclerView = view.findViewById(R.id.card_recycler_view)
//        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
//        swipeRefreshLayout!!.setOnRefreshListener(this)
//        swipeRefreshLayout!!.setColorSchemeResources(R.color.purple_200)
        val layoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = layoutManager

        loadJSON()

    }

    private fun loadJSON() {
//        swipeRefreshLayout!!.isRefreshing = true
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addNetworkInterceptor(ResponseCacheInterceptor())
        httpClient.addInterceptor(OfflineResponseCacheInterceptor())
        httpClient.cache(
            Cache(
                File(
                    NotifAir.getMyTimesApplicationInstance().cacheDir,
                    "ResponsesCache"
                ), 10 * 1024 * 1024
            )
        )
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)
        val request: ApiInterface = ApiClient.getClient(httpClient).create(ApiInterface::class.java)
        val call: Call<NewsResponse> = request.getHeadlines(SOURCE, Constants.API_KEY)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful && response.body()?.articles != null) {
                    if (articleStructure.isNotEmpty()) {
                        articleStructure.clear()
                    }
                    articleStructure = response.body()?.articles!!
                    adapter = DataAdapter(context, articleStructure)
                    recyclerView?.adapter = adapter
//                    swipeRefreshLayout!!.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
//                swipeRefreshLayout!!.isRefreshing = false
            }
        })
    }

    override fun onRefresh() {
        loadJSON()
    }

    /*
    ** TODO: APP INDEXING(App is not indexable by Google Search; consider adding at least one Activity with an ACTION-VIEW) .
    ** TODO: ADDING ATTRIBUTE android:fullBackupContent
    **/
//    private fun onLoadingSwipeRefreshLayout() {
//        if (!UtilityMethods.isNetworkAvailable()) {
//            Toast.makeText(
//                context,
//                "Could not load latest News. Please turn on the Internet.",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//        swipeRefreshLayout!!.post { loadJSON() }
//    }


//    private fun createDrawer(
//        savedInstanceState: Bundle,
//        toolbar: android.support.v7.widget.Toolbar,
//        montserrat_regular: Typeface
//    ) {
//        val item0 = PrimaryDrawerItem().withIdentifier(0).withName("GENERAL")
//            .withTypeface(montserrat_regular).withSelectable(false)
//        val item1: PrimaryDrawerItem =
//            PrimaryDrawerItem().withIdentifier(1).withName("Google News India")
//                .withIcon(R.drawable.ic_googlenews).withTypeface(montserrat_regular)
//        val item2: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(2).withName("BBC News")
//            .withIcon(R.drawable.ic_bbcnews).withTypeface(montserrat_regular)
//        val item3: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(3).withName("The Hindu")
//            .withIcon(R.drawable.ic_thehindu).withTypeface(montserrat_regular)
//        val item4: PrimaryDrawerItem =
//            PrimaryDrawerItem().withIdentifier(4).withName("The Times of India")
//                .withIcon(R.drawable.ic_timesofindia).withTypeface(montserrat_regular)
//        val item5 = SectionDrawerItem().withIdentifier(5).withName("ENTERTAINMENT")
//            .withTypeface(montserrat_regular)
//        val item6: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(6).withName("Buzzfeed")
//            .withIcon(R.drawable.ic_buzzfeednews).withTypeface(montserrat_regular)
//        val item7: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(7).withName("Mashable")
//            .withIcon(R.drawable.ic_mashablenews).withTypeface(montserrat_regular)
//        val item8: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(8).withName("MTV News")
//            .withIcon(R.drawable.ic_mtvnews).withTypeface(montserrat_regular)
//        val item9 = SectionDrawerItem().withIdentifier(9).withName("SPORTS")
//            .withTypeface(montserrat_regular)
//        val item10: PrimaryDrawerItem =
//            PrimaryDrawerItem().withIdentifier(10).withName("BBC Sports")
//                .withIcon(R.drawable.ic_bbcsports).withTypeface(montserrat_regular)
//        val item11: PrimaryDrawerItem =
//            PrimaryDrawerItem().withIdentifier(11).withName("ESPN Cric Info")
//                .withIcon(R.drawable.ic_espncricinfo).withTypeface(montserrat_regular)
//        val item12: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(12).withName("TalkSport")
//            .withIcon(R.drawable.ic_talksport).withTypeface(montserrat_regular)
//        val item13 = SectionDrawerItem().withIdentifier(13).withName("SCIENCE")
//            .withTypeface(montserrat_regular)
//        val item14: PrimaryDrawerItem =
//            PrimaryDrawerItem().withIdentifier(14).withName("Medical News Today")
//                .withIcon(R.drawable.ic_medicalnewstoday).withTypeface(montserrat_regular)
//        val item15: PrimaryDrawerItem =
//            PrimaryDrawerItem().withIdentifier(15).withName("National Geographic")
//                .withIcon(R.drawable.ic_nationalgeographic).withTypeface(montserrat_regular)
//        val item16 = SectionDrawerItem().withIdentifier(16).withName("TECHNOLOGY")
//            .withTypeface(montserrat_regular)
//        val item17: PrimaryDrawerItem =
//            PrimaryDrawerItem().withIdentifier(17).withName("Crypto Coins News")
//                .withIcon(R.drawable.ic_ccnnews).withTypeface(montserrat_regular)
//        val item18: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(18).withName("Engadget")
//            .withIcon(R.drawable.ic_engadget).withTypeface(montserrat_regular)
//        val item19: PrimaryDrawerItem =
//            PrimaryDrawerItem().withIdentifier(19).withName("The Next Web")
//                .withIcon(R.drawable.ic_thenextweb).withTypeface(montserrat_regular)
//        val item20: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(20).withName("The Verge")
//            .withIcon(R.drawable.ic_theverge).withTypeface(montserrat_regular)
//        val item21: PrimaryDrawerItem =
//            PrimaryDrawerItem().withIdentifier(21).withName("TechCrunch")
//                .withIcon(R.drawable.ic_techcrunch).withTypeface(montserrat_regular)
//        val item22: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(22).withName("TechRadar")
//            .withIcon(R.drawable.ic_techradar).withTypeface(montserrat_regular)
//        val item23 = SectionDrawerItem().withIdentifier(23).withName("GAMING")
//            .withTypeface(montserrat_regular)
//        val item24: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(24).withName("IGN")
//            .withIcon(R.drawable.ic_ignnews).withTypeface(montserrat_regular)
//        val item25: PrimaryDrawerItem = PrimaryDrawerItem().withIdentifier(25).withName("Polygon")
//            .withIcon(R.drawable.ic_polygonnews).withTypeface(montserrat_regular)
//        val item26 = SectionDrawerItem().withIdentifier(26).withName("MORE INFO")
//            .withTypeface(montserrat_regular)
//        val item27: SecondaryDrawerItem =
//            SecondaryDrawerItem().withIdentifier(27).withName("About the app")
//                .withIcon(R.drawable.ic_info).withTypeface(montserrat_regular)
//        val item28: SecondaryDrawerItem =
//            SecondaryDrawerItem().withIdentifier(28).withName("Open Source")
//                .withIcon(R.drawable.ic_code).withTypeface(montserrat_regular)
//        val item29: SecondaryDrawerItem =
//            SecondaryDrawerItem().withIdentifier(29).withName("Powered by newsapi.org")
//                .withIcon(R.drawable.ic_power).withTypeface(montserrat_regular)
//        val item30: SecondaryDrawerItem =
//            SecondaryDrawerItem().withIdentifier(30).withName("Contact us")
//                .withIcon(R.drawable.ic_mail).withTypeface(montserrat_regular)
//        accountHeader = AccountHeaderBuilder()
//            .withActivity(this)
//            .withHeaderBackground(R.drawable.ic_back)
//            .withSavedInstance(savedInstanceState)
//            .build()
//        result = DrawerBuilder()
//            .withAccountHeader(accountHeader)
//            .withActivity(this)
//            .withToolbar(toolbar)
//            .withSelectedItem(1)
//            .addDrawerItems(
//                item0,
//                item1,
//                item2,
//                item3,
//                item4,
//                item5,
//                item6,
//                item7,
//                item8,
//                item9,
//                item10,
//                item11,
//                item12,
//                item13,
//                item14,
//                item15,
//                item16,
//                item17,
//                item18,
//                item19,
//                item20,
//                item21,
//                item22,
//                item23,
//                item24,
//                item25,
//                item26,
//                item27,
//                item28,
//                item29,
//                item30
//            )
//            .withOnDrawerItemClickListener { view, position, drawerItem ->
//                val selected: Int = drawerItem.identifier as Long.toInt()
//                when (selected) {
//                    1 -> {
//                        SOURCE = SOURCE_ARRAY[0]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    2 -> {
//                        SOURCE = SOURCE_ARRAY[1]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    3 -> {
//                        SOURCE = SOURCE_ARRAY[2]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    4 -> {
//                        SOURCE = SOURCE_ARRAY[3]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    6 -> {
//                        SOURCE = SOURCE_ARRAY[4]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    7 -> {
//                        SOURCE = SOURCE_ARRAY[5]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    8 -> {
//                        SOURCE = SOURCE_ARRAY[6]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    10 -> {
//                        SOURCE = SOURCE_ARRAY[7]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    11 -> {
//                        SOURCE = SOURCE_ARRAY[8]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    12 -> {
//                        SOURCE = SOURCE_ARRAY[9]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    14 -> {
//                        SOURCE = SOURCE_ARRAY[10]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    15 -> {
//                        SOURCE = SOURCE_ARRAY[11]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    17 -> {
//                        SOURCE = SOURCE_ARRAY[12]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    18 -> {
//                        SOURCE = SOURCE_ARRAY[13]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    19 -> {
//                        SOURCE = SOURCE_ARRAY[14]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    20 -> {
//                        SOURCE = SOURCE_ARRAY[15]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    21 -> {
//                        SOURCE = SOURCE_ARRAY[16]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    22 -> {
//                        SOURCE = SOURCE_ARRAY[17]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    24 -> {
//                        SOURCE = SOURCE_ARRAY[18]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    25 -> {
//                        SOURCE = SOURCE_ARRAY[19]
//                        onLoadingSwipeRefreshLayout()
//                        mTitle!!.text = (drawerItem as Nameable<*>).name.getText(this@MainActivity)
//                    }
//                    27 -> {}
//                    28 -> {}
//                    29 -> {}
//                    30 -> {}
//                    else -> {}
//                }
//                false
//            }
//            .withSavedInstance(savedInstanceState)
//            .build()
//    }





//    fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        getMenuInflater().inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_menu -> {}
//            R.id.action_search -> openSearchActivity()
//            else -> {}
//        }
//        return super.onOptionsItemSelected(item)
//    }


//    private fun openAboutActivity() {
//        val aboutIntent = Intent(this, AboutActivity::class.java)
//        startActivity(aboutIntent)
//        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left)
//    }

//    private fun openSearchActivity() {
//        val searchIntent = Intent(this, SearchActivity::class.java)
//        startActivity(searchIntent)
//        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left)
//    }

//    private fun sendEmail() {
//        val emailIntent = Intent(Intent.ACTION_SENDTO)
//        emailIntent.data = Uri.parse("mailto: d.basak.db@gmail.com")
//        startActivity(Intent.createChooser(emailIntent, "Send feedback"))
//    }

//    fun onBackPressed() {
//        if (result!!.isDrawerOpen) {
//            result!!.closeDrawer()
//        } else {
//            val builder = AlertDialog.Builder(this@MainActivity, R.style.MyAlertDialogStyle)
//            builder.setTitle(R.string.app_name)
//            builder.setIcon(R.mipmap.ic_launcher_round)
//            builder.setMessage("Do you want to Exit?")
//                .setCancelable(false)
//                .setPositiveButton(
//                    "Yes"
//                ) { dialog, id -> finish() }
//                .setNegativeButton(
//                    "No"
//                ) { dialog, which -> dialog.cancel() }
//            val alert = builder.create()
//            alert.show()
//        }
//    }

//    protected override fun onSaveInstanceState(bundle: Bundle) {
//        //add the values which need to be saved from the drawer to the bundle
//        var bundle = bundle
//        bundle = result!!.saveInstanceState(bundle)
//        //add the values which need to be saved from the accountHeader to the bundle
//        bundle = accountHeader!!.saveInstanceState(bundle)
//        super.onSaveInstanceState(bundle)
//        listState = recyclerView!!.layoutManager!!.onSaveInstanceState()
//        bundle.putParcelable(Constants.RECYCLER_STATE_KEY, listState)
//        bundle.putString(Constants.SOURCE, SOURCE)
//        bundle.putString(Constants.TITLE_STATE_KEY, mTitle!!.text.toString())
//    }

//    protected fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        super.onRestoreInstanceState(savedInstanceState)
//        if (savedInstanceState != null) {
//            SOURCE = savedInstanceState.getString(Constants.SOURCE)
//            createToolbar()
//            mTitle!!.text = savedInstanceState.getString(Constants.TITLE_STATE_KEY)
//            listState = savedInstanceState.getParcelable(Constants.RECYCLER_STATE_KEY)
//            createDrawer(savedInstanceState, toolbar, montserrat_regular!!)
//        }
//    }

//    protected override fun onResume() {
//        super.onResume()
//        if (listState != null) {
//            recyclerView!!.layoutManager!!.onRestoreInstanceState(listState)
//        }
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                NewsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


}