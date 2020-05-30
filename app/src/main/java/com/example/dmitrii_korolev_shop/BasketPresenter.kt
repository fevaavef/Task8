package com.example.dmitrii_korolev_shop

class BasketPresenter
{

    private val assaultRifle = Product(5000.0, "Assault rifle", 10)
    private val pumpShotgun = Product (3000.0, "Pump-action shotgun", 5)
    private val SMG = Product (4000.0, "Submachine gun",20)

    private val allProducts = listOf(assaultRifle, pumpShotgun, SMG)

    fun totalPrice() : Double{
        var result = 0.0
        allProducts.forEach{product -> result+=product.getPrice()}
        return result
    }

    fun totalPriceWithDiscount() : Double
    {
        var total = 0.0
        allProducts.forEach{product -> total+=product.calcDiscountPrice()}
        return total
    }

    fun totalDiscount() : Double = "%.2f".format((1.0-totalPriceWithDiscount()/totalPrice()) * 100.0).toDouble()
}
