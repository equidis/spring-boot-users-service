package com.github.jntakpe.commons.test

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider

interface TestDataProvider<T> : ArgumentsProvider {

    fun data(): Iterable<T>

    override fun provideArguments(context: ExtensionContext) = data().map { Arguments.of(it) }.stream()
}
