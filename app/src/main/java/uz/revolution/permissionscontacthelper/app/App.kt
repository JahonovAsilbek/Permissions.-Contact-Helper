package uz.revolution.permissionscontacthelper.app

import android.app.Application
import uz.revolution.permissionscontacthelper.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}