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
import androidx.core.content.ContextCompat
import com.github.florent37.runtimepermission.kotlin.askPermission
import kotlinx.android.synthetic.main.activity_main.*
import uz.revolution.permissionscontacthelper.adapter.ContactAdapter
import uz.revolution.permissionscontacthelper.database.AppDatabase
import uz.revolution.permissionscontacthelper.models.Contact


class MainActivity : AppCompatActivity() {
    private var adapter: ContactAdapter? = null
    private var contactList: ArrayList<Contact>? = null
    private var database: AppDatabase? = null
    private var contactDao: ContactDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = ContactAdapter()
        loadDatabase()
        loadData()
        checkCallPermission()
        onMessageClick()
        loadAdapters()

    }

    override fun onResume() {
        super.onResume()
        loadData()
        loadAdapters()
    }

    private fun loadDatabase() {
        database = AppDatabase.get.getDatabase()
        contactDao = database!!.getContactDao()
    }

    private fun getAllContacts() {
        var name = ""
        var phoneNo = ""
        if (contactDao!!.getAllContacts().isEmpty()) {

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
                    name = cur.getString(
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
                            contactDao?.insertContact(Contact(name, phoneNo))
                        }
                    }
                }
                cur?.let {
                    it.close()
                }
            }
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
        contactList = contactDao?.getAllContacts() as ArrayList
//        contactList?.addAll(contactDao!!.getAllContacts())
    }

    private fun checkCallPermission() {
        askPermission(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS
        ) {
            //all permissions already granted or just granted
            getAllContacts()
            makeCall()

        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }
    }

    private fun loadAdapters() {
        contactList?.let { adapter?.setAdapter(it) }
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
                    checkCallPermission()
                    return
                }
            }

        }

    }

}