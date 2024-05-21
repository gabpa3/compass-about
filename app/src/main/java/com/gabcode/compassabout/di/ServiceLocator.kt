package com.gabcode.compassabout.di

object ServiceLocator {

    private val instances = mutableMapOf<Class<*>, Any>()

    fun <T : Any> register(clazz: Class<T>, instance: T) {
        instances[clazz] = instance
    }

    fun <T : Any> register(instance: T) {
        instances[instance::class.java] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(clazz: Class<T>): T =
        instances[clazz] as? T
            ?: throw IllegalArgumentException("No instance found for ${clazz.simpleName}")

    inline fun <reified T : Any> get(): T = get(T::class.java)

    fun removeAll() {
        instances.clear()
    }
}
