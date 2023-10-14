package ru.phrogs.moretechhackathon

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MoreTechHackathonApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("API_KEY")
    }
}