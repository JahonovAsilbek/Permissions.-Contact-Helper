package uz.revolution.permissionscontacthelper

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        sendMessage()


        return root
    }

    private fun sendMessage() {
        root.send_message.setOnClickListener {
            val phone=contact?.phone
            val message = root.message.text.toString()

            val intent = Intent(root.context, SendMessageFragment::class.java)
            val pendingIntent = PendingIntent.getActivity(root.context, 0, intent, 0)
            val sms:SmsManager= SmsManager.getDefault()
            sms.sendTextMessage(phone, null, message, pendingIntent, null)

            Snackbar.make(root, "Xabar jo'natildi", Snackbar.LENGTH_LONG).show()
            fragmentManager?.popBackStack()
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