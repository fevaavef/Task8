package com.example.dmitrii_korolev_shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CheckoutView {

    private val presenter = CheckoutPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attachView(this)
        formLayout()
    }

    private fun formLayout()
    {
        var basket = BasketPresenter()
        totalPriceValue.text = basket.totalPrice().toString()
        discountValue.text = basket.totalDiscount().toString()+'%'
        resultPriceValue.text = basket.totalPriceWithDiscount().toString()
        setListeners()
    }

    private fun setListeners(){
        checkoutPhone.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.checkPhoneNumber(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){}
        })
    }

    private fun EditText.showError(visible: Boolean){
        val drawable = if (visible) R.drawable.ic_error
        else 0
        this.setCompoundDrawablesWithIntrinsicBounds(0,0,drawable, 0)
    }

    override fun showErrorForPhone(visible: Boolean) {
        checkoutPhone.showError(visible)
    }
}
