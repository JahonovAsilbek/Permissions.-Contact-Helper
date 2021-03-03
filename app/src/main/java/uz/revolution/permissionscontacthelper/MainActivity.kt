package uz.revolution.permissionscontacthelper

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import uz.revolution.permissionscontacthelper.adapter.ContactAdapter
import uz.revolution.permissionscontacthelper.models.Contact

class MainActivity : AppCompatActivity() {
    private var adapter:ContactAdapter?=null
    var requestCode=1
    private var contacts:ArrayList<Contact>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        loadData()
        loadAdapters()
        checkCallPermission()
        onMessageClick()
    }

    private fun onMessageClick() {
        adapter?.onMessageClick =
            object : ContactAdapter.onAdapterMessageClick {
                override fun onMessageClick(contact: Contact) {
                    val fragment = SendMessageFragment.newInstance(contact, "")
                    supportFragmentManager.beginTransaction().addToBackStack("sms").add(R.id.container, fragment).commit()
                }
            }
    }

    private fun loadData() {
        contacts = ArrayList()
        contacts?.add(Contact("Olimjon Rustamov", "+998900123477"))
        contacts?.add(Contact("Jahonov Asilbek", "+998909920850"))
        contacts?.add(Contact("Shahzodbek", "+998332714061"))
    }

    private fun checkCallPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            makeCall()
        } else {
            requestSmsPermission()
        }
    }

    private fun requestSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)
            && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Permission required")
            dialog.setMessage("Permission to access the call is required")
            dialog.setPositiveButton("OK"
            ) { p0, p1 ->
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE,  Manifest.permission.READ_CONTACTS),requestCode)
            }
            val alertDialog=dialog.create()
            alertDialog.show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE,  Manifest.permission.READ_CONTACTS),requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == this.requestCode) {
            if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                makeCall()
            } else {

            }
        }
    }

    private fun loadAdapters() {
        adapter = ContactAdapter()
        adapter?.setAdapter(contacts!!)
        contacts_rv.adapter = adapter
    }

    fun makeCall() {
        adapter?.onCallClick=object :ContactAdapter.onAdapterCallClick{
            override fun onCallClick(contact: Contact) {
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val numberText = contact.phone
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:$numberText")
                    startActivity(intent)
                } else {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CALL_PHONE),requestCode)
                    return
                }
            }

        }

    }

}