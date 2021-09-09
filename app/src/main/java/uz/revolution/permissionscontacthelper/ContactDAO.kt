package uz.revolution.permissionscontacthelper

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.revolution.permissionscontacthelper.models.Contact

@Dao
interface ContactDAO {

    @Insert
    fun insertContact(contact: Contact)

    @Query("SELECT * FROM contact order by name")
    fun getAllContacts():List<Contact>

}