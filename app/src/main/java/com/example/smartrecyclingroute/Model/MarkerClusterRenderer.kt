package com.example.smartrecyclingroute.Model
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.smartrecyclingroute.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator


class MarkerClusterRenderer(private val context: Context?, map: GoogleMap?, clusterManager: ClusterManager<EcopontoClusterItem>) :
    DefaultClusterRenderer<EcopontoClusterItem>(context, map, clusterManager) {

    companion object {
        // 1
        private const val MARKER_DIMENSION = 48 // 2
    }

    private val iconGenerator: IconGenerator = IconGenerator(context)
    private val markerImageView: ImageView = ImageView(context)

    override fun onBeforeClusterItemRendered(
        item: EcopontoClusterItem,
        markerOptions: MarkerOptions
    ) { // 5
        //markerImageView.setImageBitmap(R.drawable.ic_info_) // 6
        //val icon = iconGenerator.makeIcon() // 7
        markerOptions.icon(context?.let { bitmapDescriptorFromVector(it, R.drawable.ic_info_marker)}) // 8
        markerOptions.title(item.title)
    }



    init {
        // 3
        markerImageView.layoutParams = ViewGroup.LayoutParams(
            MARKER_DIMENSION,
            MARKER_DIMENSION
        )
        iconGenerator.setContentView(markerImageView) // 4
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}