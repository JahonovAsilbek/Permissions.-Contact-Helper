package uz.revolution.permissionscontacthelper.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.revolution.permissionscontacthelper.ContactDAO
import uz.revolution.permissionscontacthelper.models.Contact

@Database(entities = [Contact::class],version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getContactDao():ContactDAO

    companion object{

        @Volatile
        private var database:AppDatabase?=null

        fun init(context: Context) {
            synchronized(this){
                if (database == null) {
                    database=Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"contact.db")
                        .allowMainThreadQueries()
                        .build()
                }
            }
        }
    }

    object get{
        fun getDatabase():AppDatabase{
            return database!!
        }
    }

}