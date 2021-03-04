package uz.revolution.permissionscontacthelper

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import uz.revolution.permissionscontacthelper.adapter.ContactAdapter
import uz.revolution.permissionscontacthelper.database.AppDatabase
import uz.revolution.permissionscontacthelper.models.Contact


class MainActivity : AppCompatActivity() {
    private var adapter: ContactAdapter? = null
    var requestCode = 1
    private var contactList: ArrayList<Contact>? = null
    private var database:AppDatabase?=null
    private var contactDao:ContactDAO?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        loadDatabase()
        loadData()
        loadAdapters()
        checkCallPermission()
        onMessageClick()
        getAllContacts()
    }

    private fun loadDatabase() {
        database=AppDatabase.get.getDatabase()
        contactDao=database!!.getContactDao()
    }

    private fun getAllContacts() {
        var name = ""
        var phoneNo=""
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val cr = contentResolver
            val cur: Cursor? = cr.query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null
            )

                if ((cur?.count ?: 0) > 0) {
                    while (cur != null && cur.moveToNext()) {
                        val id: String = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID)
                        )
                        name= cur.getString(
                            cur.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME
                            )
                        )
                        if (cur.getInt(
                                cur.getColumnIndex(
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER
                                )
                            ) > 0
                        ) {
                            val pCur: Cursor? = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(id),
                                null
                            )

                            while (pCur!!.moveToNext()) {
                                phoneNo = pCur.getString(
                                    pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                    )
                                )
//                                if (contactDao!!.getAllContacts().isEmpty()) {
//                                    contactDao?.insertContact(Contact(name,phoneNo))
                                    contactList?.add(Contact(name, phoneNo))
//                                }
                            }

                            pCur.close()

                        }
                    }
                    cur?.close()
                }

        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                requestCode
            )
            return
        }
    }

    private fun onMessageClick() {
        adapter?.onMessageClick =
            object : ContactAdapter.onAdapterMessageClick {
                override fun onMessageClick(contact: Contact) {
                    val fragment = SendMessageFragment.newInstance(contact, "")
                    supportFragmentManager.beginTransaction().addToBackStack("sms").add(
                        R.id.container,
                        fragment
                    ).commit()
                }
            }
    }

    private fun loadData() {
        contactList = ArrayList()
        contactList?.add(Contact("Sanjar Suvonov", "+998970079620"))
        contactList?.add(Contact("Asilbek Jahonov", "+998909920850"))
//        contactList?.addAll(contactDao!!.getAllContacts())
    }

    private fun checkCallPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getAllContacts()
            makeCall()
        } else {
            requestSmsPermission()
        }
    }

    private fun requestSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CALL_PHONE
            )
            && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            )
        ) {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Permission required")
            dialog.setMessage("Permission to access the call is required")
            dialog.setPositiveButton(
                "OK"
            ) { p0, p1 ->
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CONTACTS
                    ), requestCode
                )
            }
            val alertDialog = dialog.create()
            alertDialog.show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CONTACTS
                ), requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == this.requestCode) {
            if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getAllContacts()
                loadAdapters()
                makeCall()
            } else {

            }
        }
    }

    private fun loadAdapters() {
        adapter = ContactAdapter()
        adapter?.setAdapter(contactList!!)
        contacts_rv.adapter = adapter
    }

    fun makeCall() {
        adapter?.onCallClick = object : ContactAdapter.onAdapterCallClick {
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
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        requestCode
                    )
                    return
                }
            }

        }

    }

}