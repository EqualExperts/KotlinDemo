package za.co.ee.dataclasses

// Covers Data classes and Destructuring declarations

fun main() {
    // Data classes are classes that have only properties and no methods.
    // They automatically generate equals, hashcode, toString, copy and componentN methods.
    // Very similar to java's record classes in java 14


    // Immutable properties
    data class Person(val name: String, val age: Int)

    // Mutable properties
    data class PersonMutable(var name: String, var age: Int)

    // Using the copy function
    val person = Person("John", 25)
    val olderPerson = person.copy(age = 26)
    println("Copy of person $olderPerson")

    // Using mutable properties
    val mutablePerson = PersonMutable("John", 25)
    mutablePerson.age = 26
    println("Mutated person $mutablePerson")


    // Using destructuring declarations
    val (name, age) = person
}
