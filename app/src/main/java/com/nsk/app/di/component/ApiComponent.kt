package com.nsk.app.business.di.component

import com.nsk.app.base.*
import com.nsk.app.business.di.module.ApiModule
import com.nsk.app.service.UpdateService
import com.nsk.app.widget.HealthyJoinDialog
import com.nsk.app.widget.HealthyProDialog
import com.nsk.app.widget.PacketDialog
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApiModule::class))
interface ApiComponent {
   fun inject (activity: BaseTransStatusActivity)
   fun inject (activity: BaseTitleActivity)
   fun inject(baseViewModel: BaseViewModel)
   fun inject (baseFragment: BaseContentFragment)
   fun inject (healthtJoinDialog: HealthyJoinDialog)
   fun inject (healthyProDialog: HealthyProDialog)
   fun inject (packetDialog: PacketDialog)
   fun inject (updateService: UpdateService)

}