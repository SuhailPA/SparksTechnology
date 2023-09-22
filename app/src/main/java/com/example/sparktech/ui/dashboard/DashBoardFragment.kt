package com.example.sparktech.ui.dashboard

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparktech.R
import com.example.sparktech.data.model.DashboardData
import com.example.sparktech.databinding.FragmentDashBoardBinding
import com.example.sparktech.ui.MainActivity
import com.example.sparktech.utils.ApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashBoardFragment : Fragment() {

    private var _binding: FragmentDashBoardBinding? = null
    private val binding get() = _binding
    private val viewModel: DashBoardViewModel by viewModels()
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        navController = findNavController()
        (activity as MainActivity).supportActionBar?.apply {
            title = "Dashboard Screen"
            setDisplayHomeAsUpEnabled(false)
        }
        initUi()
        initSignOut()
        return binding?.root
    }

    private fun initSignOut() {
        (activity as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.logout -> {
                        alertPopUp()
                        return true
                    }
                }
                return false
            }

        }, viewLifecycleOwner)
    }

    private fun alertPopUp() {
        AlertDialog.Builder(context)
            .setTitle("Exit")
            .setMessage(getString(R.string.are_you_sure_to_exit))
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                viewModel.clearSharedPref()
                navController.navigate(DashBoardFragmentDirections.actionDashBoardFragmentToLoginFragment())
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi() {
        val dashBoardAdapter = DashBoardListAdapter()
        binding?.dashboardRecyclerView?.apply {
            adapter = dashBoardAdapter
            layoutManager = LinearLayoutManager(context)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dashBoardList.collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        dashBoardAdapter.differ.submitList(it.data as List<DashboardData>)
                    }

                    is ApiState.Error<*> -> {
                        Toast.makeText(context, it.error.toString(), Toast.LENGTH_LONG).show()
                    }

                    else -> {}
                }
            }
        }
    }
}