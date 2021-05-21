package com.codingwithjks

import com.codingwithjks.respository.DatabaseFactory
import com.codingwithjks.respository.UserRepository
import com.codingwithjks.routes.user
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.gson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads

fun Application.module(testing: Boolean = false) {

    DatabaseFactory.init()
    val db = UserRepository()
    install(Locations) {
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {
        user(db)
    }
}

const val API_VERSION ="/v1"



