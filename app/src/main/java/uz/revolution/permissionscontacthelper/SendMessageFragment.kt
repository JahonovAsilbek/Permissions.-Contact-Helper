package uz.revolution.permissionscontacthelper

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_send_message.view.*
import uz.revolution.permissionscontacthelper.models.Contact


private const val ARG_PARAM1 = "contact"
private const val ARG_PARAM2 = "param2"

class SendMessageFragment : Fragment() {

    lateinit var root: View
    private var contact: Contact? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contact = it.getSerializable(ARG_PARAM1) as Contact?
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_send_message, container, false)
        loadDataToView()
        checkSmsPermission()
        backClick()
        return root
    }

    private fun backClick() {
        root.back_btn.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun checkSmsPermission() {
        askPermission(Manifest.permission.SEND_SMS) {
            //all permissions already granted or just granted
            sendMessage()
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(root.context)
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


    private fun loadDataToView() {
        root.name.text = contact?.name
        root.phone.text = contact?.phone
    }

    private fun sendMessage() {
        root.send_message.setOnClickListener {
            val phone = contact?.phone
            val message = root.message.text.toString()

            if (message.trim().isNotEmpty()) {
                val sms: SmsManager = SmsManager.getDefault()
                sms.sendTextMessage(phone, null, message, null, null)
                Snackbar.make(root, "Xabar jo'natildi", Snackbar.LENGTH_LONG).show()
                fragmentManager?.popBackStack()
            } else {
                Snackbar.make(root, "Bo'sh xabar jo'natib bo'lmaydi", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(contact: Contact, param2: String) =
            SendMessageFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, contact)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}