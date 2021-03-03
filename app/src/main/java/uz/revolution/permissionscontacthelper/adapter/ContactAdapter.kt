package uz.revolution.permissionscontacthelper.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_contact.view.*
import uz.revolution.permissionscontacthelper.R
import uz.revolution.permissionscontacthelper.models.Contact

class ContactAdapter:RecyclerView.Adapter<ContactAdapter.VH>(){

    private var contactList:ArrayList<Contact>?=null
    var onItemClick:onAdapterItemClick?=null

    fun setAdapter(contactList: ArrayList<Contact>) {
        this.contactList=contactList
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun viewBind(contact: Contact) {
            itemView.call_btn.setOnClickListener {
                if (onItemClick != null) {
                    onItemClick!!.onCallClick(contact)
                }
            }
            itemView.message_btn.setOnClickListener {
                if (onItemClick != null) {
                    onItemClick!!.onMessageClick(contact)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.viewBind(contactList!![position])
    }

    override fun getItemCount(): Int =contactList!!.size

    interface onAdapterItemClick {

        fun onCallClick(contact: Contact)

        fun onMessageClick(contact: Contact)
    }
}