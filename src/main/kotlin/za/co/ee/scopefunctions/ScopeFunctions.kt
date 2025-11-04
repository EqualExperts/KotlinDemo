package za.co.ee.scopefunctions

    // scope functions are functions that
    // Execute a block of code within the context of an object.
    // Allow you to access the object without explicitly naming it.
    // Are used to structure code more concisely, especially for object configuration or chaining.

    // These are :
    // let, apply, run, with, also
    // let - executes a block of code and returns the result of the last expression
    // apply - executes a block of code on an object and returns the object itself
    // run - executes a block of code on an object and returns the result of the last expression
    // with - executes a block of code with a given receiver as its receiver and returns the result of
    //        the last expression
    // also - executes a block of code on an object and returns the object itself

fun main() {
    val person = Person("John", 25)

    // Use let as a shortcut
    val result = person.let {
        println("Name: ${it.name}")
        println("Age: ${it.age}")
        "done"
    }
    println(result)

    // Instead of
    // println("Name: ${person.name}")
    // println("Name: ${person.age}")

    // Same with apply, except returns the object itself and uses "this" because the object is set as the receiver
    println(
        person.apply {
            println("Name: $name")
            println("Age: $age")
        }
    )


    // Let as a null check
    // Let is very often used to check for null rather than == null
    var nullPerson: Person? = null

    val car = nullPerson?.let {
        Car(it.name)
    }
    println("Car could be null here:  $car")

    // Equivalent to
    val car2 = if (nullPerson != null) Car(nullPerson.name) else null
    println("Car2 could be null here: $car2")

}

data class Person(val name: String, val age: Int)
data class Car(val owner: String)
