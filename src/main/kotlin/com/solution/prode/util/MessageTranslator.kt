package com.solution.prode.util

import java.util.Locale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class MessageTranslator {

    @Autowired
    private lateinit var messageSource: MessageSource

    fun getMessage(code: String, args: Array<Any>? = null, locale: Locale = LocaleContextHolder.getLocale()): String {

        return messageSource.getMessage(code, args, locale)
    }
}
