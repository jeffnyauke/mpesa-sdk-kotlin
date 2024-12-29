package io.github.jeffnyauke.mpesa.store

public open class BaseAuthStore(accessToken: AccessToken? = null) {
    public var token: AccessToken? = accessToken

    public open fun save(token: AccessToken) {
        this.token = token
    }

    public open fun clear() {
        this.token = null
    }
}
