package uz.revolution.permissionscontacthelper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.permissionscontacthelper.R
import uz.revolution.permissionscontacthelper.models.Contact

class ContactAdapter:RecyclerView.Adapter<ContactAdapter.VH>(){

    private var contactList:ArrayList<Contact>?=null

    fun setAdapter(contactList: ArrayList<Contact>) {
        this.contactList=contactList
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

    }

    override fun getItemCount(): Int =contactList!!.size
}