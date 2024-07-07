package com.alexnemyr.happybirthday.ui.flow.anniversary

import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Intent
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Label
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.State
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStoreFactory
import com.alexnemyr.mvi.MviViewModel

class AnniversaryViewModel(
    storeFactory: AnniversaryStoreFactory
) : MviViewModel<Intent, State, Label, AnniversaryStore>(
    storeFactory.create()
)
