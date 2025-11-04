package za.co.ee.constructors

// Constructors. Primary and secondary
// Default and named parameters
// File and class naming rules
// Functions at root/file level

fun main() {

    // Class can be defined in any file. Class names don't need to match file names.
    // Constructor takes a String
    val myClass = AnyClassName("My Class")

    // Invoke the public (by default) function
    myClass.sayHello()

    println("Items defined in constructor are public when defined as val or var : ${myClass.name}")

    // Functions defined in the class are public by default
    println(myClass.whatsMyAgeAndName())

    // I can reference and change the value of the mutable public property of the class
    myClass.age = 25
    println(myClass.whatsMyAgeAndName())

    // --------------- Constructors ----------------//

    // Using primary constructor
    val constructor = za.co.ee.constructors.Constructors("Primary Constructor", "Surname", 30)
    println("External view - Constructor name ${constructor.name} age doubled ${constructor.ageDoubled}")

    // Primary with defaults
    // Default values for parameters
    // I can call the constructor with less parameters than specified in the constructor if there are default values
    // The default values are used for the missing parameters
    val constructorPrimaryDefaults = Constructors("Primary Constructor", "With Defaults")
    println("External view - Constructor name ${constructorPrimaryDefaults.name} age " +
            "doubled ${constructorPrimaryDefaults.ageDoubled}")


    // Using secondary constructor
    var constructorSecondary = Constructors("Secondary Constructor")
    println("Constructor name ${constructorSecondary.name} age ${constructorSecondary.ageDoubled}")

    // Named parameters
    // These help to avoid mistakes and make the code more readable
    // But also help when it's not just the last parameter that has a default value.
    val constructorFourth = Constructors(secondary = "Bob", height = 180)
    println("Constructor name ${constructorFourth.name} age ${constructorFourth.ageDoubled}")

    // or using the named param in order to use the defaults for the other parameters.
    val constructor5 = Constructors(height = 180)
    println("Constructor name ${constructor5.name} age ${constructor5.ageDoubled}")
    //val constructor6 = Constructors(25) // Doesn't work

}
