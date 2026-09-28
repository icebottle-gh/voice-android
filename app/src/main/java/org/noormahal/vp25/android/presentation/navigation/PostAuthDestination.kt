package org.noormahal.vp25.android.presentation.navigation

import org.noormahal.ib.vakkic.enums.AccountState

enum class PostAuthDestination { ACCOUNT_SETUP, UNVERIFIED, HOME }

fun AccountState.toPostAuthDestination(): PostAuthDestination = when (this) {
    AccountState.SETUP -> PostAuthDestination.ACCOUNT_SETUP
    AccountState.UNVERIFIED -> PostAuthDestination.UNVERIFIED
    AccountState.ACTIVE -> PostAuthDestination.HOME
}
