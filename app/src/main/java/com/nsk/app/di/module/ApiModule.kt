package com.nsk.app.business.di.module

import android.content.Context
import com.nsk.cky.ckylibrary.http.ServiceApi
import com.nsk.cky.ckylibrary.http.client.HttpInstance
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule(val url:String,val context: Context) {
    @Provides
    @Singleton
    fun provideHttpInstance():ServiceApi = HttpInstance.getInstance(url,context).retrofit.create(ServiceApi::class.java)
}