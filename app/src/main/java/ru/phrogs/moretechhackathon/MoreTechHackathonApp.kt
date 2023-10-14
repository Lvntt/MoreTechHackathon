package ru.phrogs.moretechhackathon

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.phrogs.moretechhackathon.di.provideDomainModule
import ru.phrogs.moretechhackathon.di.providePresentationModule
import ru.phrogs.moretechhackathon.feature_chat_bot.di.provideChatDomainModule
import ru.phrogs.moretechhackathon.feature_chat_bot.di.provideChatPresentationModule

class MoreTechHackathonApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoreTechHackathonApp)
            modules(
                provideDomainModule(),
                providePresentationModule(),
                provideChatDomainModule(),
                provideChatPresentationModule()
            )
        }
        //Yep, I leaked the API key, I know
        MapKitFactory.setApiKey("d213c2ba-a7ff-4bfa-aa57-d2f64ce8c666")
    }
}