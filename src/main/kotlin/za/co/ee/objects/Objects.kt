package za.co.ee.objects

// Kotlin object replaces java's static keyword by creating singleton objects that can be referenced as static
// Singleton Class
// Companion Object

// Singleton class (static class but implemented as a singleton under the hood)
object MySingleton {
    const val name = "MySingleton"
}


class MyClassWithCompanion {

    val name = "MyClassWithCompanion"
    // Companion object is a named object that can be referenced as MyClassWithCompanion.Companion
    companion object {
        const val staticLikeField = "static like field"
        var anotherStaticLikeField = "another static like field"
    }
}

fun main() {

    // ---- singleton class ---

    // Reference singleton object - no need to create object reference
    println(MySingleton.name)

    // ---- companion object ---

    // Reference "static" field - actually just a field on a singleton object associated with the owning class.
    println(MyClassWithCompanion.staticLikeField)


    // Or reference the field via the created internal object
    val instance = MyClassWithCompanion()
    println(instance.name)
    println(MyClassWithCompanion.Companion.staticLikeField)

    // -- Object expression - or Anonymouse object
    // Note that this does not extend a class or implement an interface so the "Type" is not well defined.
    val someObject = object {
        val name = "Object Expression"
    }
    println(someObject.name)

    // Anonymous object that implements MyInterface
    // Now we have a type
    val myInterface = object : MyInterface {
        override fun doStuff() {
            println("doing stuff")
        }
    }
    myInterface.doStuff()
}

interface MyInterface {
    fun doStuff()
}

