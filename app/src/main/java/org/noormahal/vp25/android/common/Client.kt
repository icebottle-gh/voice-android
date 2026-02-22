package org.noormahal.vp25.android.common

import org.noormahal.ib.vakkic.App
import org.noormahal.ib.vakkic.AppImpl
import org.noormahal.ib.vakkic.User

object Client {
    var app: App = AppImpl("https://ib-service.noormahal.org/ib-api/vakki", "9ciBrYwZePyjgDnutVoaDci9LGiHy6uJKV")
    var user: User? = null
}