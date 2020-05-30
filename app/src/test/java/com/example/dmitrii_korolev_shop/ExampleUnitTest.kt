package com.example.dmitrii_korolev_shop

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.roundToInt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        print(formatPrice(price = 102310.0, dataType = "kg", discount = 17))
    }

    private fun formatPrice(price: Double, dataType:String = "шт", discount: Int) : String
    {
        val resultPriceString = configurePriceString(price, discount)
        val discountString = configureDiscountString(discount)

        return "$resultPriceString/$dataType$discountString"
    }

    private fun configurePriceString(price: Double, discount: Int) : String
    {
        val calculatedPrice = price - price * discount / 100
        val roundedPrice = calculatedPrice.roundToInt()

        return if(calculatedPrice - roundedPrice == 0.0) {
            calculatedPrice.roundToInt().toString()
        }
        else {
            String.format("%.2f", calculatedPrice)
        }
    }

    private fun configureDiscountString(discount: Int) : String
    {
        var discountString = ""
        if(discount != 0) discountString = " (скидка $discount%)"
        return discountString
    }
}
