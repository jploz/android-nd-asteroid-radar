package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val asteroidListAdapter = AsteroidListAdapter(AsteroidListAdapter.OnClickListener {
            viewModel.navigateToAsteroidDetails(it)
        })
        asteroidListAdapter.setHasStableIds(true)
        binding.asteroidRecycler.adapter = asteroidListAdapter

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.doneNavigateToAsteroidDetails()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_asteroids_weeks_item -> viewModel.setAsteroidsListUiFilter(
                AsteroidsUiFilter.SHOW_ASTEROIDS_WEEKS
            )
            R.id.show_asteroids_today_item -> viewModel.setAsteroidsListUiFilter(
                AsteroidsUiFilter.SHOW_ASTEROIDS_TODAYS
            )
            R.id.show_asteroids_all_item -> viewModel.setAsteroidsListUiFilter(
                AsteroidsUiFilter.SHOW_ASTEROIDS_ALL
            )
        }
        return true
    }
}
