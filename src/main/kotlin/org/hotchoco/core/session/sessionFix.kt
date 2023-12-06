package org.hotchoco.core.session

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

// if set header, sent `Set-{header}`
class SessionTransportHeaderFix(
    public val name: String,
    public val transformers: List<SessionTransportTransformer>
) : SessionTransport {
    init {
        HttpHeaders.checkHeaderName(name)
    }

    override fun receive(call: ApplicationCall): String? {
        return transformers.transformRead(call.request.headers[name])
    }

    override fun send(call: ApplicationCall, value: String) {
        call.response.header("Set-$name", transformers.transformWrite(value))
    }

    override fun clear(call: ApplicationCall) {}

    override fun toString(): String {
        return "SessionTransportHeader: $name"
    }
}

internal fun <S : Any> SessionsConfig.headerFix(
    name: String,
    sessionType: KClass<S>,
    storage: SessionStorage?,
    builder: HeaderSessionBuilderFix<S>
) {
    val transport = SessionTransportHeaderFix(name, builder.transformers)
    val tracker = when {
        storage != null && builder is HeaderIdSessionBuilderFix<S> -> SessionTrackerById(
            sessionType,
            builder.serializer,
            storage,
            builder.sessionIdProvider
        )
        else -> SessionTrackerByValue(sessionType, builder.serializer)
    }
    val provider = SessionProvider(name, sessionType, transport, tracker)
    register(provider)
}

internal inline fun <reified S : Any> SessionsConfig.headerFix(
    name: String,
    storage: SessionStorage,
    block: HeaderIdSessionBuilderFix<S>.() -> Unit
) {
    val sessionType = S::class

    val builder = HeaderIdSessionBuilderFix(sessionType, typeOf<S>()).apply(block)
    headerFix(name, sessionType, storage, builder)
}

public open class HeaderSessionBuilderFix<S : Any> constructor(
    public val type: KClass<S>,
    public val typeInfo: KType
) {

    @Deprecated("Use builder functions instead.", level = DeprecationLevel.ERROR)
    @Suppress("UNREACHABLE_CODE")
    public constructor(type: KClass<S>) : this(
        type,
        throw IllegalStateException("Use builder functions with reified type parameter instead.")
    )

    /**
     * Specifies a serializer used to serialize session data.
     */
    public var serializer: SessionSerializer<S> = defaultSessionSerializer(typeInfo)

    private val _transformers = mutableListOf<SessionTransportTransformer>()

    /**
     * Gets transformers used to sign and encrypt session data.
     */
    public val transformers: List<SessionTransportTransformer> get() = _transformers

    /**
     * Registers a [transformer] used to sign and encrypt session data.
     */
    public fun transform(transformer: SessionTransportTransformer) {
        _transformers.add(transformer)
    }
}

/**
 * A configuration that allows you to configure header settings for [Sessions].
 */
public class HeaderIdSessionBuilderFix<S : Any> constructor(
    type: KClass<S>,
    typeInfo: KType
) : HeaderSessionBuilderFix<S>(type, typeInfo) {

    @Deprecated("Use builder functions instead.", level = DeprecationLevel.ERROR)
    @Suppress("UNREACHABLE_CODE")
    public constructor(type: KClass<S>) : this(
        type,
        throw IllegalStateException("Use builder functions with reified type parameter instead.")
    )

    /**
     * Registers a function used to generate a session ID.
     */
    public fun identity(f: () -> String) {
        sessionIdProvider = f
    }

    /**
     * A function used to provide a current session ID.
     */
    public var sessionIdProvider: () -> String = { generateSessionId() }
        private set
}
