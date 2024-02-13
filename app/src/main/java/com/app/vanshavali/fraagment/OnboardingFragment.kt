package com.app.vanshavali.fraagment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.vanshavali.R
import com.app.vanshavali.databinding.OnBoardingFragmentBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_onboarding.view.*

class OnboardingFragment : Fragment() {
    private lateinit var categoryId: String
    private lateinit var categoryName: String
    private lateinit var categoryDetail: String
    private lateinit var categoryImage: String
    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvDescription: AppCompatTextView
    private lateinit var image: ImageView
    private lateinit var binding: OnBoardingFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            categoryId = requireArguments().getString(ARG_CATEGORY_ID).toString()
            categoryName = requireArguments().getString(ARG_CATEGORY_NAME).toString()
            categoryDetail = requireArguments().getString(ARG_CATEGORY_DETAILS).toString()
            categoryImage = requireArguments().getString(ARG_CATEGORY_IMAGE).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false)

        tvTitle = binding.root.text_onboarding_title
        tvDescription = binding.root.text_onboarding_description
        image = binding.root.image_onboarding
        binding.name = categoryName
        binding.details = categoryDetail
        binding.pulsator.start()
        Glide
            .with(this)
            .load(categoryImage)
            .centerCrop()
            .placeholder(R.drawable.parasuhrama)
            .error(R.drawable.parasuhrama)
            .into(image)
        return binding.root
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_CATEGORY_ID = "categoryId"
        private const val ARG_CATEGORY_NAME = "categoryName"
        private const val ARG_CATEGORY_DETAILS = "categoryDetail"
        private const val ARG_CATEGORY_IMAGE = "categoryImage"
        fun newInstance(
            categoryId: String?,
            categoryName: String?,
            categoryDetail: String,
            categoryImage: String,
        ): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY_ID, categoryId)
            args.putString(ARG_CATEGORY_NAME, categoryName)
            args.putString(ARG_CATEGORY_DETAILS, categoryDetail)
            args.putString(ARG_CATEGORY_IMAGE, categoryImage)
            fragment.arguments = args
            return fragment
        }
    }
}
