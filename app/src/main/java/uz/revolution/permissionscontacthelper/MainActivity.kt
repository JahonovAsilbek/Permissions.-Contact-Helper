package uz.revolution.permissionscontacthelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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

        adapter.onCallClick=object:ContactAdapter.onAdapterCallClick{
            override fun onCallClick(contact: Contact) {
                Toast.makeText(contacts_rv.context, "$contact", Toast.LENGTH_SHORT).show()
//                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.phone));
//                startActivity(intent)
            }
        }
        adapter.onMessageClick=object:ContactAdapter.onAdapterMessageClick{
            override fun onMessageClick(contact: Contact) {
                Snackbar.make(contacts_rv,"Shoshma, to'xta ....",Snackbar.LENGTH_LONG).show()
            }

        }
    }

}