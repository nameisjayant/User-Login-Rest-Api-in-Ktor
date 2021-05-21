package com.codingwithjks.routes

import com.codingwithjks.API_VERSION
import com.codingwithjks.respository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val USER_CREATE = "$API_VERSION/user"
const val GET_USER_BY_ID = "$API_VERSION/user/{id}"

@OptIn(KtorExperimentalLocationsAPI::class)
@Location(USER_CREATE)
class UserCreateRoute

@OptIn(KtorExperimentalLocationsAPI::class)
@Location(GET_USER_BY_ID)
class GetUserById

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.user(
    db: UserRepository
) {
    post<UserCreateRoute> {
        val parameter = call.receive<Parameters>()

        val name = parameter["name"] ?: return@post call.respondText(
            "missing field",
            status = HttpStatusCode.Unauthorized
        )

        val age = parameter["age"] ?: return@post call.respondText(
            "missing field",
            status = HttpStatusCode.Unauthorized
        )

        try {
            val user = db.insert(name, age.toInt())
            user?.userId?.let {
                call.respond(status = HttpStatusCode.OK, user)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems creating User")
        }
    }

    get<UserCreateRoute> {
        try {
            val userList = db.getAllUser()
            if(userList.isNotEmpty()){
                call.respond(userList)
            }else{
                call.respondText("No Data found!!")
            }
        }catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems creating User")
        }
    }

    get("$API_VERSION/user/{id}") {
        val parameter = call.parameters["id"]
        try {
            val user = parameter?.toInt()?.let { it1 -> db.getUserById(it1) } ?: return@get call.respondText(
                "invalid id",
                status = HttpStatusCode.BadRequest
            )
            user.userId.let {
                call.respond(status = HttpStatusCode.OK, user)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems creating User")
        }
    }

    delete("$API_VERSION/user/{id}") {
        val parameter = call.parameters["id"]
        try {
            val user = parameter?.toInt()?.let { it1 -> db.deleteUserById(it1) } ?: return@delete call.respondText(
                "no id found..",
                status = HttpStatusCode.BadRequest
            )
            if (user == 1) {
                call.respondText("deleted successfully", status = HttpStatusCode.OK)
            } else {
                call.respondText("id not found", status = HttpStatusCode.BadRequest)
            }

        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems creating User")
        }
    }

    put("$API_VERSION/user/{id}"){
        val id = call.parameters["id"] ?: return@put call.respondText(
            "id not found",
            status = HttpStatusCode.BadRequest
        )
        val updateInfo = call.receive<Parameters>()
        val name = updateInfo["name"] ?: return@put call.respondText(
            "missing field",
            status = HttpStatusCode.Unauthorized
        )
        val age = updateInfo["age"] ?: return@put call.respondText(
            "missing field",
            status = HttpStatusCode.Unauthorized
        )

        try {
            val result = id.toInt().let { it1 -> db.updateUser(it1, name , age.toInt() ) }
            if(result == 1 ){
                call.respondText("updated successfully....")
            }else{
                call.respondText("something went wrong..")
            }
        }catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems creating User")
        }
    }
}