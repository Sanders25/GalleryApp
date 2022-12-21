package com.example.galleryapp.view

import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.galleryapp.R
import com.example.galleryapp.databinding.ItemImageBinding
import com.example.galleryapp.view.GalleryAdapter.ImageViewHolder
import kotlinx.coroutines.*

class GalleryAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    private var dataset = emptyList<Uri>()

    fun updateDataset(newList: List<Uri>) {
        DiffUtil.calculateDiff(ImageDiffUtil(dataset, newList))
            .dispatchUpdatesTo(this)
        dataset = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
        private val binding = ItemImageBinding.bind(itemView)

        fun bind(item: Uri) {
            binding.image.setImageBitmap(null)
            val requiredSize = 150
            scope.launch {
                binding.image.setImageBitmap(
                    withContext(Dispatchers.IO) {
                        val options = BitmapFactory.Options().apply {
                            inJustDecodeBounds = true
                        }
                        BitmapFactory.decodeFile(item.path, options)
                        val imageHeight: Int = options.outHeight
                        val imageWidth: Int = options.outWidth

                        var _inSampleSize = 1

                        if (imageHeight > requiredSize || imageWidth > requiredSize) {

                            val halfHeight: Int = imageHeight / 2
                            val halfWidth: Int = imageWidth / 2

                            while (
                                halfHeight / _inSampleSize >= requiredSize &&
                                halfWidth / _inSampleSize >= requiredSize
                            ) {
                                _inSampleSize *= 2
                            }
                        }
                        options.inSampleSize = _inSampleSize
                        options.inJustDecodeBounds = false

                        ThumbnailUtils.extractThumbnail(
                            BitmapFactory.decodeFile(
                                item.path,
                                options
                            ),
                            requiredSize,
                            requiredSize
                        )
                    }
                )
                itemView.animation = AnimationUtils.loadAnimation(
                    itemView.context,
                    R.anim.item_recyclerview
                )
            }
        }

        fun clearItem() {
            scope.coroutineContext.cancelChildren()
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_image
    }

    override fun onViewRecycled(holder: ImageViewHolder) {

        holder.clearItem()
    }

    class ImageDiffUtil(private val oldList: List<Uri>, private val newList: List<Uri>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}