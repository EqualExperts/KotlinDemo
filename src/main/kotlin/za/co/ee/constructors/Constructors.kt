package za.co.ee.constructors

// The val's, vars and params defined here define the default/primary constructor
class Constructors(val name: String, surname: String, var age: Int = 20) {

    // Use the init block to initialize the class members
    // This is equivalent to the constructor in Java
    init {
        println("Init block called")
        // I can reference the parameters in the init block
        println(">> Hello, I'm $name, my surname is $surname and I'm $age years old")
    }

    // I can still reference the surname constructor parameter here because it's part of the class initialization phase
    // That is, constructor params are available during class initialization
    val surnameAndName = "$surname, $name"

    fun notHere() {
        //println("I'm not here $surname")
    }


    // ------------ Secondary Constructors ----- //

    // Define a secondary constructor
    // Must invoke the primary constructor first // Unlike java, primary initialization logic must always run
    constructor(secondary: String) : this("From Secondary", "Constructor") {
        println("1st Secondary constructor called")
        println(">> secondary parameter is: $secondary")
    }

    // Third constructor - parameter naming
    constructor(secondary: String = "second-Bob", height: Int) : this("From 2nd Secondary", "Constructor", 0) {
        println("2nd Secondary constructor called")
        println(">> secondary parameter is: $secondary with height $height")
    }

    val ageDoubled = age * 2

}
