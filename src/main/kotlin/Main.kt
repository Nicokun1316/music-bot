package io.github.nicokun1316

import io.github.cdimascio.dotenv.dotenv


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val dotenv = dotenv()
    val appId = "697511271167164548"
    val publicKey = "f9c8398c4813de6498cdf1dea51a3aa375d72538bb48ffd9aa4d2786e5a7ff0a"
    val secret = dotenv["BITRISE_SECRET"]
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")

    for (i in 1..5) {
        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
        println("i = $i")
    }
}