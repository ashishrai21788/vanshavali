package com.app.vanshavali.home.view


import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.vanshavali.PreferenceData
import com.app.vanshavali.R
import com.app.vanshavali.databinding.CatDetailsBinding
import com.app.vanshavali.landing.view.ListActivity
import com.app.vanshavali.utils.Animatoo
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.cat_details.*

class CatDetailsActivity : AppCompatActivity() {
    private lateinit var btnStart: LinearLayout
    private val ARG_CATEGORY_ID = "categoryId"
    private val ARG_CATEGORY_NAME = "categoryName"
    private val ARG_CATEGORY_DETAILS = "categoryDetail"
    private val ARG_CATEGORY_IMAGE = "categoryImage"

    private lateinit var categoryId: String
    private lateinit var categoryName: String
    private lateinit var categoryDetail: String
    private lateinit var categoryImage: String
    private var binding: CatDetailsBinding? = null


    private lateinit var ivAvatar: ImageView
    private var EXPAND_AVATAR_SIZE: Float = 0F
    private var COLLAPSE_IMAGE_SIZE: Float = 0F
    private var margin: Float = 0F
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private var cashCollapseState: Pair<Int, Int>? = null
    private lateinit var titleToolbarText: AppCompatTextView
    private lateinit var titleToolbarTextSingle: AppCompatTextView
    private lateinit var collapsingAvatarContainer: FrameLayout
    private lateinit var background: FrameLayout
    private lateinit var preferenceData: PreferenceData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.cat_details)
        preferenceData = PreferenceData(CatDetailsActivity@this)

        binding?.backTool?.setOnClickListener {
            finish()
        }
        binding?.backExpend?.setOnClickListener {
            finish()
        }
        binding?.layoutStart?.setOnClickListener {
            preferenceData.setCategoryId(categoryId)
            preferenceData.setCategoryName( categoryName)
            preferenceData.setCategoryDetails(categoryDetail)
            preferenceData.setCategoryImage(categoryImage)

            val intent =
                Intent(applicationContext, ListActivity::class.java)
                    .putExtra(ARG_CATEGORY_ID, categoryId)
                    .putExtra(ARG_CATEGORY_NAME, categoryName)
                    .putExtra(ARG_CATEGORY_DETAILS, categoryDetail)
                    .putExtra(ARG_CATEGORY_IMAGE, categoryImage)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        if(intent != null) {
            categoryId = intent.getStringExtra(ARG_CATEGORY_ID).toString()
            categoryName =
                intent.getStringExtra(ARG_CATEGORY_NAME).toString()
            categoryDetail =
                intent.getStringExtra(ARG_CATEGORY_DETAILS).toString()
            categoryImage =
                intent.getStringExtra(ARG_CATEGORY_IMAGE).toString()
        }

        binding?.details = categoryDetail
        binding?.name = categoryName

        EXPAND_AVATAR_SIZE = resources.getDimension(R.dimen._80sdp)
        COLLAPSE_IMAGE_SIZE = resources.getDimension(R.dimen.default_collapsed_image_size)
        margin = resources.getDimension(R.dimen.avatar_margin)
        collapsingAvatarContainer = findViewById(R.id.stuff_container)
        appBarLayout = findViewById(R.id.app_bar_layout)
        toolbar = findViewById(R.id.anim_toolbar)
        toolbar.visibility = View.INVISIBLE
        ivAvatar = findViewById(R.id.imgb_avatar_wrap)

        Glide
            .with(this)
            .load(categoryImage)
            .centerCrop()
            .placeholder(R.drawable.parasuhrama)
            .error(R.drawable.parasuhrama)
            .into(ivAvatar)

        titleToolbarText = findViewById(R.id.tv_profile_name)
        titleToolbarTextSingle = findViewById(R.id.tv_profile_name_single)
        background = findViewById(R.id.fl_background)

        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
                if (isCalculated.not()) {
                    startAvatarAnimatePointY = Math.abs((appBarLayout.height - EXPAND_AVATAR_SIZE - toolbar.height / 2) / appBarLayout.totalScrollRange)
                    animateWeigt = 1 / (1 - startAvatarAnimatePointY)
                    isCalculated = true
                }

                val offset = Math.abs(i / appBarLayout.totalScrollRange.toFloat())
                updateViews(offset)
            })

//        findViewById<Button>(R.id.b_go_demo_1).setOnClickListener {
//            finish()
//        }


    }

    private var startAvatarAnimatePointY: Float = 0F
    private var animateWeigt: Float = 0F
    private var isCalculated = false

    private fun updateViews(percentOffset: Float) {
        /* Collapsing avatar transparent*/
        when {
            percentOffset > mUpperLimitTransparently -> {
                //avatarContainerView.alpha = 0.0f
                titleToolbarText.alpha = 0.0F
            }

            percentOffset < mUpperLimitTransparently -> {
                //  avatarContainerView.alpha = 1 - percentOffset
                titleToolbarText.alpha = 1f
            }
        }

        /*Collapsed/expended sizes for views*/
        val result: Pair<Int, Int> = when {
            percentOffset < ABROAD -> {
                Pair(TO_EXPANDED_STATE, cashCollapseState?.second
                    ?: WAIT_FOR_SWITCH)
            }
            else -> {
                Pair(TO_COLLAPSED_STATE, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
            }
        }
        result.apply {
            var translationY = 0f
            var headContainerHeight = 0f
            val translationX: Float
            var currentImageSize = 0
            when {
                cashCollapseState != null && cashCollapseState != this -> {
                    when (first) {
                        TO_EXPANDED_STATE -> {
                            translationY = toolbar.height.toFloat()
                            headContainerHeight = appBarLayout.totalScrollRange.toFloat()
                            currentImageSize = EXPAND_AVATAR_SIZE.toInt()
                            /**/
                            titleToolbarText.visibility = View.VISIBLE
                            titleToolbarTextSingle.visibility = View.INVISIBLE

                            binding?.backTool?.visibility = View.INVISIBLE
                            binding?.backExpend?.visibility = View.VISIBLE
                            background.setBackgroundColor(ContextCompat.getColor(this@CatDetailsActivity, R.color.color_transparent))
                            /**/
                            ivAvatar.translationX = 0f
                        }

                        TO_COLLAPSED_STATE -> {
                            background.setBackgroundColor(ContextCompat.getColor(this@CatDetailsActivity, R.color.white))
                            currentImageSize = COLLAPSE_IMAGE_SIZE.toInt()
                            translationY = appBarLayout.totalScrollRange.toFloat() - (toolbar.height - COLLAPSE_IMAGE_SIZE) / 2
                            headContainerHeight = toolbar.height.toFloat()
                            translationX = appBarLayout.width / 2f - COLLAPSE_IMAGE_SIZE / 2 - margin * 2
                            /**/
                            ValueAnimator.ofFloat(ivAvatar.translationX, translationX).apply {
                                addUpdateListener {
                                    if (cashCollapseState!!.first == TO_COLLAPSED_STATE) {
                                        ivAvatar.translationX = it.animatedValue as Float
                                    }
                                }
                                interpolator = AnticipateOvershootInterpolator()
                                startDelay = 69
                                duration = 350
                                start()
                            }
                            /**/

                            binding?.backExpend?.visibility = View.INVISIBLE
                            binding?.backTool?.visibility = View.VISIBLE

                            titleToolbarText.visibility = View.INVISIBLE
                            titleToolbarTextSingle.apply {
                                visibility = View.VISIBLE
                                alpha = 0.2f
                                this.translationX = width.toFloat() / 2
                                animate().translationX(0f)
                                    .setInterpolator(AnticipateOvershootInterpolator())
                                    .alpha(1.0f)
                                    .setStartDelay(69)
                                    .setDuration(450)
                                    .setListener(null)
                            }
                        }
                    }

                    ivAvatar.apply {
                        layoutParams.height = currentImageSize
                        layoutParams.width = currentImageSize
                    }
                    collapsingAvatarContainer.apply {
                        layoutParams.height = headContainerHeight.toInt()
                        this.translationY = translationY
                        requestLayout()
                    }
                    /**/
                    cashCollapseState = Pair(first, SWITCHED)
                }
                else -> {
                    /* Collapse avatar img*/
                    ivAvatar.apply {
                        if (percentOffset > startAvatarAnimatePointY) {

                            val animateOffset = (percentOffset - startAvatarAnimatePointY) * animateWeigt
                            //Timber.d("offset for anim $animateOffset")
                            val avatarSize = EXPAND_AVATAR_SIZE - (EXPAND_AVATAR_SIZE - COLLAPSE_IMAGE_SIZE) * animateOffset

                            this.layoutParams.also {
                                if (it.height != Math.round(avatarSize)) {
                                    it.height = Math.round(avatarSize)
                                    it.width = Math.round(avatarSize)
                                    this.requestLayout()
                                }
                            }

                            this.translationY = (COLLAPSE_IMAGE_SIZE - avatarSize) * animateOffset

                        } else {
                            this.layoutParams.also {
                                if (it.height != EXPAND_AVATAR_SIZE.toInt()) {
                                    it.height = EXPAND_AVATAR_SIZE.toInt()
                                    it.width = EXPAND_AVATAR_SIZE.toInt()
                                    this.layoutParams = it
                                }
                            }
                        }
                    }
                    cashCollapseState = Pair(first, WAIT_FOR_SWITCH)
                }
            }


        }
    }

    companion object {
        const val ABROAD = 0.99f
        const val TO_EXPANDED_STATE = 0
        const val TO_COLLAPSED_STATE = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1
    }

    private val mLowerLimitTransparently = ABROAD * 0.45
    private val mUpperLimitTransparently = ABROAD * 0.65

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
