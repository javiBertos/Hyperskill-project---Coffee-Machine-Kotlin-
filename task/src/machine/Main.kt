package machine
import java.util.Scanner

fun getCoffeesAmount(): Int {
    val reader = Scanner(System.`in`)
    var coffees: Int?

    do {
        println("Write how many cups of coffee you will need:")
        coffees = reader.nextLine()?.toIntOrNull()
    } while (coffees == null)

    return coffees
}

fun main() {
    val waterPerCoffee = 200
    val milkPerCoffee = 50
    val beansPerCoffee = 15

    val coffees = getCoffeesAmount()

    println(
        """
            For $coffees cups of coffee you will need:
            ${coffees * waterPerCoffee} ml of water
            ${coffees * milkPerCoffee} ml of milk
            ${coffees * beansPerCoffee} g of coffee beans
        """.trimIndent()
    )
}
