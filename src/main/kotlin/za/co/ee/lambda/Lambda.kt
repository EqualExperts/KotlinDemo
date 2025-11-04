package za.co.ee.lambda

// Defining a property as a lambda
// Lambda as the last parameter can exist outside of parenthesis

fun main() {
    // Lambda as a variable
    val sum: (Int, Int) -> Int = { x, y -> x + y }
    println(sum(1, 2))

    // Lambda as a function argument
    fun operation(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
        return operation(x, y)
    }
    println(operation(1, 2, sum))

    // Lambda as the last functional argument
    println(
        operation(1, 2) {
                    x, y -> (x + y) * 10
        }
    )
}
