package com.example.dmitrii_korolev_shop

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.junit.Test

import org.junit.Assert.*
import kotlin.math.roundToInt

class PricePrinterTest : ProductsView{
    @Test
    fun addition_isCorrect() {
        var presenter = BasketPresenter(this)
        presenter.nameAndPricePrint()
        presenter.totalPricePrint()
    }

    override fun print(price: Double) {
        println("$price")
    }

    override fun print(name: String) {
        println("$name")
    }
}

class BasketPresenter (
    private val view: ProductsView
) {

    private val assaultRifle = Product(5000.0, "Assault rifle", 10)
    private val pumpShotgun = Product (3000.0, "Pump-action shotgun", 5)
    private val SMG = Product (4000.0, "Submachine gun",20)

    private val allProducts = listOf(assaultRifle, pumpShotgun, SMG)

    fun pricePrint(){
        allProducts.forEach{product->
            view.print(product.calcDiscountPrice())}
    }

    fun namePrint(){
        allProducts.forEach{product->
            view.print(product.calcDiscountPrice())}
    }

    fun nameAndPricePrint(){
        allProducts.forEach{product->
            view.print("${product.getName()}: ${product.calcDiscountPrice()}")}
    }

    fun totalPricePrint()
    {
        var total = 0.0
        allProducts.forEach{product -> total+=product.calcDiscountPrice()}
        view.print("Total price: $total")
    }
}

class Product(
    private val price: Double,
    private val name: String,
    private val salePercent: Int = 0
) {
    fun calcDiscountPrice(): Double = price * (1 - salePercent / 100.0)
    fun getName() = name
}

interface ProductsView{
    fun print(price: Double)
    fun print(name: String)
}