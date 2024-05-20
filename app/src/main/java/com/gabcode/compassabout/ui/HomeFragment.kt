package com.gabcode.compassabout.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.gabcode.compassabout.R
import com.gabcode.compassabout.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass to represent the home screen.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<AboutViewModel>()

    private var navigator: Navigator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigator = context as? Navigator ?: throw ClassCastException("$context must implement Navigator")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeStartButton.setOnClickListener {
            sharedViewModel.startProcessingData()
            navigator?.navigate(NavigatorRoute.ABOUT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navigator = null
        _binding = null
    }

    companion object {

        fun newInstance() = HomeFragment()
    }
}
