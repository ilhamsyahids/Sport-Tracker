package com.myandroid.sporttracker.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.adapters.NewsAdapter
import com.myandroid.sporttracker.adapters.ReminderAdapter
import com.myandroid.sporttracker.ui.schedule.ScheduleViewModel
import com.myandroid.sporttracker.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.newsList.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { news ->
                        newsAdapter.differ.submitList(news.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { msg ->
                        Log.e("NewsListError", msg)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        newsProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        newsProgressBar.visibility = View.INVISIBLE
    }


    private fun setupRecyclerView() = rvNews.apply {
        newsAdapter = NewsAdapter()
        adapter = newsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}