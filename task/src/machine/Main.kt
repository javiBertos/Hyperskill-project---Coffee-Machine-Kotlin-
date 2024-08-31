package machine
import java.util.Scanner

fun getAmountFromUser(product: String): Int {
    val reader = Scanner(System.`in`)
    var amount: Int?

    do {
        println(
            when (product) {
                "water" -> "Write how many ml of water the coffee machine has:"
                "milk" -> "Write how many ml of milk the coffee machine has:"
                "beans" -> "Write how many grams of coffee beans the coffee machine has:"
                else -> "Write how many cups of coffee you will need:"
            }
        )
        amount = reader.nextLine()?.toIntOrNull()
    } while (amount == null)

    return amount
}

fun main() {
    val waterPerCoffee = 200
    val milkPerCoffee = 50
    val beansPerCoffee = 15

    val currentWater = getAmountFromUser("water")
    val currentMilk = getAmountFromUser("milk")
    val currentBeans = getAmountFromUser("beans")
    val coffees = getAmountFromUser("coffee")

    val maximumCoffees = listOf<Int>(
        currentWater / waterPerCoffee,
        currentMilk / milkPerCoffee,
        currentBeans / beansPerCoffee
    ).minOrNull()

    val remainingCoffees: Int = maximumCoffees?.minus(coffees) ?: 0

    println(when {
        remainingCoffees == 0 -> "Yes, I can make that amount of coffee"
        remainingCoffees > 0 -> "Yes, I can make that amount of coffee (and even $remainingCoffees more than that)"
        else -> "No, I can make only $maximumCoffees cups of coffee"
    })
}
