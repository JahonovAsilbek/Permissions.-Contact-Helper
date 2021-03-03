package uz.revolution.permissionscontacthelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import uz.revolution.permissionscontacthelper.adapter.ContactAdapter
import uz.revolution.permissionscontacthelper.models.Contact

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val adapter = ContactAdapter()
        val contacts = ArrayList<Contact>()
        contacts.add(Contact("Olimjon Rustamov","+998900123477"))
        contacts.add(Contact("Jahonov Asilbek", "+998909920850"))
        adapter.setAdapter(contacts)
        contacts_rv.adapter = adapter
        contacts_rv.layoutManager=LinearLayoutManager(this)
    }

}