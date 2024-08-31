package machine
import java.util.Scanner

enum class CoffeMachineStatus {
    MAIN, FILLING, FILLING_WATER, FILLING_MILK, FILLING_BEANS, FILLING_CUPS, TAKING_MONEY, CHECKING_LEVELS, BUYING_COFFEE, FINISHING
}

class CoffeMachine {
    private var currentWater = 400
    private var currentMilk = 540
    private var currentBeans = 120
    private var currentDisposableCups = 9
    private var currentCash = 550

    private var machineStatus = CoffeMachineStatus.MAIN

    private fun printMachineStatus(): Unit {
        println(
            """
            The coffee machine has:
            ${this.currentWater} ml of water
            ${this.currentMilk} ml of milk
            ${this.currentBeans} g of coffee beans
            ${this.currentDisposableCups} disposable cups
            $${this.currentCash} of money
            
            """.trimIndent()
        )
    }

    private fun takeMoneyFromMachine(): Unit {
        println("I gave you \$${this.currentCash}\n")
        this.currentCash = 0
    }

    private fun getParamsByCoffeeType(type: String): List<Int> {
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

    private fun processSelectedCoffee(option: Int): Unit {
        val (waterPerCoffee, milkPerCoffee, beansPerCoffee, costPerCoffee) = this.getParamsByCoffeeType(listOf("espresso", "latte", "capuccino")[option - 1])

        when {
            this.currentWater < waterPerCoffee -> println("Sorry, not enough water!")
            this.currentMilk < milkPerCoffee -> println("Sorry, not enough milk!")
            this.currentBeans < beansPerCoffee -> println("Sorry, not enough coffee beans!")
            this.currentDisposableCups < 1 -> println("Sorry, not enough disposable cups!")
            else -> {
                this.currentWater -= waterPerCoffee
                this.currentMilk -= milkPerCoffee
                this.currentBeans -= beansPerCoffee

                this.currentDisposableCups -= 1

                this.currentCash += costPerCoffee

                println("I have enough resources, making you a coffee!")
            }
        }
    }

    private fun manageBuyingStep(order: String): CoffeMachineStatus {
        if (this.machineStatus != CoffeMachineStatus.BUYING_COFFEE) {
            println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")

            return CoffeMachineStatus.BUYING_COFFEE
        }

        if (order != "back" && order.toIntOrNull() != null && order.toInt() in 1..3) {
            this.processSelectedCoffee(order.toInt())
        }

        return CoffeMachineStatus.MAIN
    }

    private fun manageFillingStep(fillingOrder: String): CoffeMachineStatus {
        return when (this.machineStatus) {
            CoffeMachineStatus.MAIN -> {
                println("Write how many ml of water you want to add:")
                CoffeMachineStatus.FILLING_WATER
            }
            CoffeMachineStatus.FILLING_WATER -> {
                this.currentWater += fillingOrder.toInt()
                println("Write how many ml of milk you want to add:")
                CoffeMachineStatus.FILLING_MILK
            }
            CoffeMachineStatus.FILLING_MILK -> {
                this.currentMilk += fillingOrder.toInt()
                println("Write how many grams of coffee beans you want to add:")
                CoffeMachineStatus.FILLING_BEANS
            }
            CoffeMachineStatus.FILLING_BEANS -> {
                this.currentBeans += fillingOrder.toInt()
                println("Write how many disposable cups you want to add:")
                CoffeMachineStatus.FILLING_CUPS
            }
            CoffeMachineStatus.FILLING_CUPS -> {
                this.currentDisposableCups += fillingOrder.toInt()
                CoffeMachineStatus.MAIN
            }
            else -> CoffeMachineStatus.MAIN
        }
    }

    fun processAction(order: String): CoffeMachineStatus {
        when {
            order == CoffeMachineStatus.MAIN.name -> {
                println("Write action (buy, fill, take, remaining, exit):")
            }

            order == CoffeMachineStatus.CHECKING_LEVELS.name -> {
                this.printMachineStatus()
            }

            order == CoffeMachineStatus.TAKING_MONEY.name -> {
                this.takeMoneyFromMachine()
            }

            order == CoffeMachineStatus.BUYING_COFFEE.name ||
                    this.machineStatus == CoffeMachineStatus.BUYING_COFFEE ->
            {
                this.machineStatus = this.manageBuyingStep(order)

                return this.machineStatus
            }

            order == CoffeMachineStatus.FILLING.name ||
                    this.machineStatus == CoffeMachineStatus.FILLING_WATER ||
                    this.machineStatus == CoffeMachineStatus.FILLING_MILK ||
                    this.machineStatus == CoffeMachineStatus.FILLING_BEANS  ||
                    this.machineStatus == CoffeMachineStatus.FILLING_CUPS ->
            {
                this.machineStatus = this.manageFillingStep(order)

                return this.machineStatus
            }

            order == CoffeMachineStatus.FINISHING.name -> {
                return CoffeMachineStatus.FINISHING
            }
        }

        this.machineStatus = CoffeMachineStatus.MAIN

        return this.machineStatus
    }
}

fun main() {
    val reader = Scanner(System.`in`)
    val coffeeMachine = CoffeMachine()

    do {
        var machineStatus = coffeeMachine.processAction(CoffeMachineStatus.MAIN.name)

        val userInput = reader.nextLine()

        machineStatus = when(userInput) {
            "fill" -> {
                coffeeMachine.processAction(CoffeMachineStatus.FILLING.name)
                var newStatus: CoffeMachineStatus

                do {
                    val fillingInput = reader.nextLine()
                    newStatus = coffeeMachine.processAction(fillingInput)
                } while (newStatus != CoffeMachineStatus.MAIN)

                newStatus
            }

            "take" -> coffeeMachine.processAction(CoffeMachineStatus.TAKING_MONEY.name)

            "buy" -> {
                coffeeMachine.processAction(CoffeMachineStatus.BUYING_COFFEE.name)

                val choosenOption = reader.nextLine()
                coffeeMachine.processAction(choosenOption)
            }

            "remaining" -> coffeeMachine.processAction(CoffeMachineStatus.CHECKING_LEVELS.name)

            "exit" -> coffeeMachine.processAction(CoffeMachineStatus.FINISHING.name)

            else -> {
                println("Not valid option...")
                CoffeMachineStatus.MAIN
            }
        }
    } while (machineStatus != CoffeMachineStatus.FINISHING)
}
