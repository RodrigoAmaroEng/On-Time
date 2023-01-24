package dev.amaro.on_time.ui.spice

import java.lang.reflect.Field
import java.lang.reflect.Method


fun Any.getFromField(name: String): Any {
    return getField(name).get(this)
}

fun Any.setField(name: String, value: Any) {
    getField(name).set(this, value)
}

fun Any.getField(name: String): Field {
    return reflectSearch(javaClass) { it.getDeclaredField(name) }.apply { isAccessible = true }
}


fun Any.getMethod(name: String): Method {
    return reflectSearch(javaClass) { it.getDeclaredMethod(name) }.apply { isAccessible = true }
}

fun <T> reflectSearch(rootClazz: Class<*>, operation: (Class<*>) -> T): T {
    var clazz: Class<*>? = rootClazz
    while (clazz != null) {
        try {
            return operation(clazz)
        } catch (e: NoSuchFieldException) {
            clazz = clazz.superclass
        }
    }
    throw NoSuchFieldException("Field not found in class ${rootClazz.name}")
}