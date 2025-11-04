package za.co.ee.nulltypeop

// Kotlin tried to avoid nulls.
// Types by default cannot be null unless explicitly declared as such

// Not valid as it can't be null
// var myVar: Nulls = null

// Valid because we've declared it nullable
var myVar:Nulls? = null

// However, now that it's null, we must always check for the null when it's being used.
fun main() {
    // Won't compile
    // println(myVar.calculate())

    // But with the null check, it will
    println("Printing value of calculate or null [${myVar?.calculate()}]")
    // Call is short circuited so we won't even enter the calculate function


    // Elvis operator
    // If the left hand side is null, then return the value on the right hand side
    println("Printing value of calculate or 0 [${myVar?.calculate() ?: 0}]")
    // Elvis operator is only executed if the left hand side is null

}

class Nulls {
    @Suppress("FunctionOnlyReturningConstant")
    fun calculate(): Int {
        return 5
    }
}
