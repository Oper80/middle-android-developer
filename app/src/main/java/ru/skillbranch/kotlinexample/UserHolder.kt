package ru.skillbranch.kotlinexample

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ):User{
        val newUser = User.makeUser(fullName, email = email, password = password)
        if(map.containsKey(newUser.login)) throw IllegalArgumentException("A user with this email already exists")
        return newUser.also {user -> map[user.login] = user}
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User{
        if(!validatePhone(rawPhone)) throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
        val newUser = User.makeUser(fullName, rawPhone)
        if(map.containsKey(newUser.login)) throw IllegalArgumentException("A user with this phone already exists")
        return newUser.also {user -> map[user.login] = user}
    }


    fun loginUser(login:String, password:String): String?{
        return map[login.trim()]?.run{
            if(checkPassword(password)) this.userInfo
            else null
        }
    }

    fun requestAccessCode(login:String){
        map[login]?.updateAccessCode()
    }

    private fun validatePhone(rawPhone: String): Boolean {
        return rawPhone.matches("""^+[0-9]{11}$""".toRegex())
    }


}