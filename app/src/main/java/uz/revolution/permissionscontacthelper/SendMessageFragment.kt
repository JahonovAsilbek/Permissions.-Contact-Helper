package uz.revolution.permissionscontacthelper

import android.Manifest
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_send_message.view.*
import uz.revolution.permissionscontacthelper.models.Contact


private const val ARG_PARAM1 = "contact"
private const val ARG_PARAM2 = "param2"

class SendMessageFragment : Fragment() {

    lateinit var root: View
    var requestCode=1
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
        if (ContextCompat.checkSelfPermission(
                root.context,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            sendMessage()
        } else {
            requestSmsPermission()
        }
    }

    private fun requestSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity as AppCompatActivity,Manifest.permission.SEND_SMS)){
            val dialog = AlertDialog.Builder(root.context)
            dialog.setTitle("Permission required")
            dialog.setMessage("Permission to access the sms is required")
            dialog.setPositiveButton("OK"
            ) { p0, p1 ->
                ActivityCompat.requestPermissions(activity as AppCompatActivity, arrayOf(Manifest.permission.SEND_SMS),requestCode)
            }
            val alertDialog=dialog.create()
            alertDialog.show()
        }else{
            ActivityCompat.requestPermissions(activity as AppCompatActivity, arrayOf(Manifest.permission.SEND_SMS),requestCode)
        }
    }


    private fun loadDataToView() {
        root.name.text = contact?.name
        root.phone.text=contact?.phone
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == this.requestCode) {
            if (grantResults.size == 3 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                sendMessage()
            } else {
            }
        }
    }

    private fun sendMessage() {
        root.send_message.setOnClickListener {
            if (ContextCompat.checkSelfPermission(root.context,Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){
                val phone = contact?.phone
                val message = root.message.text.toString()

                if (message.trim().isNotEmpty()) {
                    val sms: SmsManager = SmsManager.getDefault()
                    sms.sendTextMessage(phone, null, message, null, null)
                    Snackbar.make(root, "Xabar jo'natildi", Snackbar.LENGTH_LONG).show()
                    fragmentManager?.popBackStack()
                } else {
                    Snackbar.make(root,"Bo'sh xabar jo'natib bo'lmaydi",Snackbar.LENGTH_LONG).show()
                }

            }else{
                checkSmsPermission()
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