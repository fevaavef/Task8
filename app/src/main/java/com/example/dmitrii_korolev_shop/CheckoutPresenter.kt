package com.example.dmitrii_korolev_shop

import moxy.MvpPresenter

class CheckoutPresenter : MvpPresenter<CheckoutView>() {

    private val model = CreateOrderModel()

    fun checkPhoneNumber(text: String) {
        if(checkPhone(text)) model.phone = text
        viewState.showErrorForPhone(!checkPhone(text))
    }

    private fun checkPhone(phone: String): Boolean = phone.matches(Regex("((\\+7|8)([0-9]){10})"))
}