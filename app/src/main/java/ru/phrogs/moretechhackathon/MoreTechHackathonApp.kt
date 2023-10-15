package ru.phrogs.moretechhackathon

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.phrogs.moretechhackathon.di.provideDomainModule
import ru.phrogs.moretechhackathon.di.providePresentationModule
import ru.phrogs.moretechhackathon.feature_chat_bot.di.provideChatDataModule
import ru.phrogs.moretechhackathon.feature_chat_bot.di.provideChatDomainModule
import ru.phrogs.moretechhackathon.feature_chat_bot.di.provideChatPresentationModule
import ru.phrogs.moretechhackathon.feature_visit_history.di.provideHistoryDataModule
import ru.phrogs.moretechhackathon.feature_visit_history.di.provideHistoryDomainModule
import ru.phrogs.moretechhackathon.feature_visit_history.di.provideHistoryPresentationModule

class MoreTechHackathonApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoreTechHackathonApp)
            modules(
                provideDomainModule(),
                providePresentationModule(),
                provideChatDomainModule(),
                provideChatPresentationModule(),
                provideChatDataModule(),
                provideHistoryDataModule(),
                provideHistoryDomainModule(),
                provideHistoryPresentationModule(),
            )
        }
        MapKitFactory.setApiKey("d213c2ba-a7ff-4bfa-aa57-d2f64ce8c666")
    }
}