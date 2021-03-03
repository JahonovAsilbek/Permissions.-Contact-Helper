package uz.revolution.permissionscontacthelper

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
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
        contacts.add(Contact("Olimjon Rustamov", "+998900123477"))
        contacts.add(Contact("Jahonov Asilbek", "+998909920850"))
        adapter.setAdapter(contacts)
        contacts_rv.adapter = adapter

        adapter.onCallClick = object : ContactAdapter.onAdapterCallClick {
            override fun onCallClick(contact: Contact) {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    makeCall(contact)
                }
            }
        }

        adapter.onMessageClick =
            object : ContactAdapter.onAdapterMessageClick {
                override fun onMessageClick(contact: Contact) {
                    Snackbar.make(contacts_rv, "Shoshma, to'xta ....", Snackbar.LENGTH_LONG).show()
                }

            }
    }

    fun makeCall(contact: Contact) {
        val numberText = contact.phone
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$numberText")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            return
        }
        startActivity(intent)
    }

}