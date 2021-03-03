package uz.revolution.permissionscontacthelper.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
class Contact {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "phone")
    var phone:String?=null

    constructor(name: String?, phone: String?) {
        this.name = name
        this.phone = phone
    }
}