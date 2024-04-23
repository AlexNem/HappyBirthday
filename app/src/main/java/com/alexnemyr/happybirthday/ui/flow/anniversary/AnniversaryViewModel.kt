package com.alexnemyr.happybirthday.ui.flow.anniversary

import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStoreFactory
import com.alexnemyr.mvi.MviViewModel

class AnniversaryViewModel(
    storeFactory: AnniversaryStoreFactory
) : MviViewModel<AnniversaryStore.Intent, AnniversaryStore.State, AnniversaryStore.Label, AnniversaryStore>(
    storeFactory.create()
) {


}