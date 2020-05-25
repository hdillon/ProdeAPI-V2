package com.solution.prode.configuration

import java.lang.reflect.Method
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.util.StringUtils

class CacheKeyGenerator : KeyGenerator {
    override fun generate(target: Any, method: Method, vararg params: Any?): Any {
        return target.javaClass.simpleName + ":" + method.name + "(" + StringUtils.arrayToDelimitedString(params, ",") + ")"
    }
}
