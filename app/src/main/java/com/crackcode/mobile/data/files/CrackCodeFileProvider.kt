package com.crackcode.mobile.data.files

import android.net.Uri
import androidx.core.content.FileProvider
import com.crackcode.mobile.data.miniapps.MiniAppSigning

/**
 * CrackCode's FileProvider. It tells other apps that *.agapp files are mini app packages, so a package sent through
 * WhatsApp / Telegram / email keeps that type and tapping it on the other phone opens CrackCode's installer
 * (Android doesn't know the extension, so plain FileProvider would say application/octet-stream).
 */
class CrackCodeFileProvider : FileProvider() {
    override fun getType(uri: Uri): String? =
        if (uri.path?.endsWith(".agapp", ignoreCase = true) == true) MiniAppSigning.MIME else super.getType(uri)

    override fun getTypeAnonymous(uri: Uri): String? =
        if (uri.path?.endsWith(".agapp", ignoreCase = true) == true) MiniAppSigning.MIME else super.getTypeAnonymous(uri)
}
