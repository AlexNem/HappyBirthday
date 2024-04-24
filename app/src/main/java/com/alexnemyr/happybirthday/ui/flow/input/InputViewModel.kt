package com.alexnemyr.happybirthday.ui.flow.input

import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Intent
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Label
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.State
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStoreFactory
import com.alexnemyr.mvi.MviViewModel

class InputViewModel(
    storeFactory: InputStoreFactory
) : MviViewModel<Intent, State, Label, InputStore>(storeFactory.create()) {


}