package com.example.smartrecyclingroute.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartrecyclingroute.Model.Ecoponto
import com.example.smartrecyclingroute.R
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_final_report.*

class CustomEcopontosAdapter(val ecopontosList: List<Ecoponto>) :
    RecyclerView.Adapter<CustomEcopontosAdapter.ViewHolder>() {

    val baseUrl = "http://68.183.44.22/storage/assets/"

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (ecopontosList[position].category) {
            "blue" -> {
                holder.txtName?.text = "Ecoponto Azul"
                Picasso.get().load(baseUrl+"ecoponto_azul_texto.png").into(holder.ecopontoImage)
            }
            "yellow" -> {
                holder.txtName?.text = "Ecoponto Amarelo"
                Picasso.get().load(baseUrl+"ecoponto_amarelo_texto.png").into(holder.ecopontoImage)
            }
            "green" -> {
                holder.txtName?.text = "Ecoponto Verde"
                Picasso.get().load(baseUrl+"ecoponto_verde_texto.png").into(holder.ecopontoImage)
            }
            "brown" -> {
                holder.txtName?.text = "Lixo Comum"
                Picasso.get().load(baseUrl+"lixo_comum_texto.png").into(holder.ecopontoImage)
            }
            else -> { holder.txtName?.text = "Ecoponto"}
        }

        when {
            ecopontosList[position].capacity < 50 -> {
                holder.chipStatus.setChipBackgroundColorResource(R.color.green)
                holder.chipStatus.setChipIconResource(R.drawable.ic_check_circle_white_24dp)
                holder.chipStatus.text = "Vazio"
            }
            ecopontosList[position].capacity >= 50 &&  ecopontosList[position].capacity < 85 -> {
                holder.chipStatus.setChipBackgroundColorResource(R.color.dark_yellow)
                holder.chipStatus.setChipIconResource(R.drawable.ic_warning_white_24dp)
                holder.chipStatus.text = "Quase Cheio"
            }
            else -> {
                holder.chipStatus.setChipBackgroundColorResource(R.color.red)
                holder.chipStatus.setChipIconResource(R.drawable.ic_error_white_24dp)
                holder.chipStatus.text = "Cheio"
            }
        }

        holder.txtCapacity?.text = "Capacidade " + ecopontosList[position].capacity.toString() + " %"

        holder.lastUpdate.text = "Last Update " + ecopontosList[position].updated_at

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.ecoponto_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return ecopontosList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById<TextView>(R.id.txtEcopontoName)
        val txtCapacity = itemView.findViewById<TextView>(R.id.txtCapacity)
        val chipStatus = itemView.findViewById<Chip>(R.id.status_chip)
        val ecopontoImage = itemView.findViewById<ImageView>(R.id.imageViewEcoponto)
        val lastUpdate = itemView.findViewById<TextView>(R.id.txt_last_update)
    }

}