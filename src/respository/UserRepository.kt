package com.codingwithjks.respository

import com.codingwithjks.data.dao.UserDao
import com.codingwithjks.data.model.User
import com.codingwithjks.data.model.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserRepository : UserDao {
    override suspend fun insert(name: String, age: Int): User? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statement = UserTable.insert { user ->
                user[UserTable.name] = name
                user[UserTable.age] = age
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun getAllUser(): List<User> =
        DatabaseFactory.dbQuery {
            UserTable.selectAll().mapNotNull {
                rowToUser(it)
            }
        }

    override suspend fun getUserById(userId: Int): User? =
        DatabaseFactory.dbQuery {
            UserTable.select { UserTable.userId.eq(userId) }
                .map {
                    rowToUser(it)
                }.singleOrNull()
        }

    override suspend fun deleteUserById(userId: Int): Int =
      DatabaseFactory.dbQuery {
          UserTable.deleteWhere { UserTable.userId.eq(userId) }
      }

    override suspend fun updateUser(userId: Int, name: String, age: Int): Int =
        DatabaseFactory.dbQuery {
                UserTable.update({UserTable.userId.eq(userId)}){user->
                    user[UserTable.name] = name
                    user[UserTable.age] = age
                }
        }


    private fun rowToUser(row: ResultRow?): User? {
        if (row == null)
            return null
        return User(
            name = row[UserTable.name],
            age = row[UserTable.age],
            userId = row[UserTable.userId]
        )
    }
}