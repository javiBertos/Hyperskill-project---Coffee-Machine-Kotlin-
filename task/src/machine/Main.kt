package machine
import java.util.Scanner

val reader = Scanner(System.`in`)

var currentWater = 400
var currentMilk = 540
var currentBeans = 120
var currentDisposableCups = 9
var currentCash = 550

fun printMachineStatus(): Unit {
    println(
        """
            The coffee machine has:
            $currentWater ml of water
            $currentMilk ml of milk
            $currentBeans g of coffee beans
            $currentDisposableCups disposable cups
            $$currentCash of money
            
        """.trimIndent()
    )
}

fun getInputFromUser(product: String): Int {
    var amount: Int?

    do {
        println(
            when (product) {
                "water" -> "Write how many ml of water you want to add:"
                "milk" -> "Write how many ml of milk you want to add:"
                "beans" -> "Write how many grams of coffee beans you want to add:"
                "cups" -> "Write how many disposable cups you want to add:"
                else -> "What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:"
            }
        )
        amount = reader.nextLine()?.toIntOrNull()
    } while (amount == null)

    return amount
}

fun fillTheMachine(): Unit {
    currentWater += getInputFromUser("water")
    currentMilk += getInputFromUser("milk")
    currentBeans += getInputFromUser("beans")
    currentDisposableCups += getInputFromUser("cups")
}

fun takeMoneyFromMachine(): Unit {
    println("I gave you \$$currentCash\n")
    currentCash = 0
}

fun getParamsByCoffeeType(type: String): List<Int> {
    var water = 0
    var milk = 0
    var beans = 0
    var cost = 0

    when(type) {
        "espresso" -> {
            water = 250
            beans = 16
            cost = 4
        }
        "latte" -> {
            water = 350
            milk = 75
            beans = 20
            cost = 7
        }
        "capuccino" -> {
            water = 200
            milk = 100
            beans = 12
            cost = 6
        }
    }

    return listOf(water, milk, beans, cost)
}

fun buyACoffee(): Unit {
    val coffeeType = getInputFromUser("coffee")
    val (waterPerCoffee, milkPerCoffee, beansPerCoffee, costPerCoffee) = getParamsByCoffeeType(listOf("espresso", "latte", "capuccino")[coffeeType - 1])

    currentWater -= waterPerCoffee
    currentMilk -= milkPerCoffee
    currentBeans -= beansPerCoffee

    currentDisposableCups -= 1

    currentCash += costPerCoffee
}

fun main() {
    printMachineStatus()

    var option: String

    do {
        println("Write action (buy, fill, take):")
        option = reader.nextLine().trim()

        when(option) {
            "fill" -> fillTheMachine()
            "take" -> takeMoneyFromMachine()
            "buy" -> buyACoffee()
            else -> println("Not valid option...")
        }
    } while (option != "buy" && option != "fill" && option != "take")

    printMachineStatus()
}
