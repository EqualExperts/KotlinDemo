package za.co.ee.ext

fun main(args: Array<String>) {
    // Any class can be extended with an extension function

    val sentence = "This is a sentence"

    //removeSpaces is an extension function defined on the String class
    val withoutSpaces = sentence.removeSpaces()
    println(sentence)
    println(withoutSpaces)


    // Example of a built in extention functions on String with nullable receiver
    val mySentence = "Hello World"
    println("Is a valid String - ${isValidString(mySentence)}")

    println("Is a valid String - ${isValidString(null)}")

}

@Suppress("ReturnCount")
fun isValidString(example: String?): Boolean {
    // Works even on a null as the isNullOrEmpty tests for a null receiver. E.g you can call the method on a null
    // This is because, while this looks like a typical OO function call on an object, it's actually not...
    // It's a static function call passing it the string value.
    if (example.isNullOrEmpty()) return false

    if (example == "Hello World") {
        return true
    }

    return false
}
