package ru.skillbranch.kotlinexample

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        val newUser = User.makeUser(fullName, email = email, password = password)
        if (map.containsKey(newUser.login)) throw IllegalArgumentException("A user with this email already exists")
        return newUser.also { user -> map[user.login] = user }
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        if (!validatePhone(rawPhone)) throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
        val newUser = User.makeUser(fullName, phone = rawPhone)
        if (map.containsKey(newUser.login)) throw IllegalArgumentException("A user with this phone already exists")
        return newUser.also { user -> map[user.login] = user }
    }


    fun loginUser(login: String, password: String): String? {
        val user = if (map.containsKey(login.trim().toLowerCase())) {
            map[login.trim().toLowerCase()]
        } else {
            map[login.toPhone()]
        }
        return user?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }


    fun requestAccessCode(login: String) {
        map[login.toPhone()]?.updateAccessCode()
    }

    fun importUsers(list: List<String>): List<User>{
        val userList = mutableListOf<User>()
        for (item in list){
            val itemList = item.split(";")
            val (salt, hash) = itemList[2].split(":")
            val email = if(itemList[1].isNotEmpty()) itemList[1].trim() else null
            val phone = if(itemList[3].isNotEmpty()) itemList[3].trim() else null
            val user = User.makeUser(itemList[0].trim(), email, phone = phone, salt = salt, hash = hash)
            userList.add(user)
            map[user.login] = user
        }
        return userList
    }

    private fun validatePhone(rawPhone: String): Boolean {
        return rawPhone.toPhone().matches("^\\+[0-9]{11}$".toRegex())
    }

    private fun String.toPhone(): String {
        return this.replace("[^+\\d]".toRegex(), "")
    }
}