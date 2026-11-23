package com.crackcode.mobile

import androidx.compose.runtime.Composable
import com.crackcode.mobile.ui.chat.ChatScreen
import com.crackcode.mobile.ui.chat.ChatViewModel

@Composable
fun MainNavigation(viewModel: ChatViewModel? = null) {
    if (viewModel != null) {
        ChatScreen(viewModel = viewModel)
    } else {
        ChatScreen()
    }
}
