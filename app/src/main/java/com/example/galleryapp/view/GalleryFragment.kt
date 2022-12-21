package com.example.galleryapp.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apppicker.util.launchWith
import com.example.galleryapp.R
import com.example.galleryapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class GalleryFragment @Inject constructor() : Fragment(R.layout.fragment_gallery) {

    private val viewModel by hiltNavGraphViewModels<GalleryViewModel>(R.id.galleryFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { }
        if (ContextCompat.checkSelfPermission(
                requireContext(), READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissionLauncher.launch(
                READ_EXTERNAL_STORAGE
            )
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentGalleryBinding.bind(view)
        val spanCount = 4

        val layoutManager = GridLayoutManager(requireContext(), spanCount).apply {
            recycleChildrenOnDetach = true
        }
        binding.recyclerView.layoutManager = layoutManager
        val adapter = GalleryAdapter()
        binding.recyclerView.adapter = adapter


        viewModel.imageFlow.onEach {
            adapter.updateDataset(it)
        }.launchWith(lifecycle)
    }
}