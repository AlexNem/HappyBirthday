package com.alexnemyr.happybirthday.ui.flow.input

import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStoreFactory
import com.alexnemyr.mvi.MviViewModel

class InputMviViewModel(
    storeFactory: InputStoreFactory
) : MviViewModel<InputStore.Intent, InputStore.State, InputStore.Label, InputStore>(storeFactory.create()) {


}