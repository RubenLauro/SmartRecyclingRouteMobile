package com.example.smartrecyclingroute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartrecyclingroute.Adapters.CustomEcopontosAdapter
import com.example.smartrecyclingroute.Model.Ecoponto
import com.example.smartrecyclingroute.Model.EcopontoList
import com.example.smartrecyclingroute.Model.GroupList
import com.example.smartrecyclingroute.Networking.RetrofitInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_ecopontos.*
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EcopontosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecopontos)

        val group_name = intent.getStringExtra("group_name")

        title = "Group $group_name"

        recyclerViewEcopontos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val call = RetrofitInitializer().services().listEcopontos(group_name!!)
        call.enqueue(object : Callback<EcopontoList> {
            override fun onResponse(call: Call<EcopontoList>, response: Response<EcopontoList>) {
                if (response.body()?.data?.isEmpty()!!){
                    progressBar_ecopontos.visibility = View.INVISIBLE
                    txt_no_ecopontos.visibility = View.VISIBLE
                }else{
                    val adapter = response.body()?.data?.let {
                        CustomEcopontosAdapter(it)
                    }
                    recyclerViewEcopontos.adapter = adapter
                    constraint_layout_progress_bar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<EcopontoList>, t: Throwable) {
                Log.e("onFailure error", t?.message)
            }
        })
    }
}
