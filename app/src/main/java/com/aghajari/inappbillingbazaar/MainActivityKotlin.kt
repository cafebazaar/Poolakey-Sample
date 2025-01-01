package com.aghajari.inappbillingbazaar

import android.os.Bundle
import android.widget.Toast
import com.aghajari.inappbillingbazaar.poolakey.Configurations
import com.aghajari.inappbillingbazaar.ui.content.BasePoolakeyActivity
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.ConnectionState
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import ir.cafebazaar.poolakey.request.PurchaseRequest

class MainActivityKotlin : BasePoolakeyActivity() {

    private lateinit var payment: Payment
    private lateinit var paymentConnection: Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // UI handled on BasePoolakeyActivity

        val securityCheck = SecurityCheck.Enable(rsaPublicKey = Configurations.RSA_KEY)
        val paymentConfig = PaymentConfiguration(localSecurityCheck = securityCheck)
        payment = Payment(context = this, config = paymentConfig)

        paymentConnection = payment.connect {
            connectionSucceed {
                updateStatus(status = "Connected")
            }
            connectionFailed { throwable ->
                updateStatus(status = "Failed: ${throwable.message}")
            }
            disconnected {
                updateStatus(status = "Disconnected")
            }
        }
    }

    override fun onClickSKU10KIRR() {
        purchase(
            request = PurchaseRequest(
                productId = Configurations.PRODUCT_ID_1,
                payload = "SKU10",
            ),
        )
    }

    override fun onClickSKU20KIRR() {
        purchase(
            request = PurchaseRequest(
                productId = Configurations.PRODUCT_ID_2,
                payload = "SKU20",
            ),
        )
    }

    private fun purchase(request: PurchaseRequest) {
        if (paymentConnection.getState() != ConnectionState.Connected) {
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show()
            return
        }

        payment.purchaseProduct(
            registry = activityResultRegistry,
            request = request,
        ) {
            purchaseFlowBegan {
                updateStatus(status = "Purchase Began")
            }
            failedToBeginFlow { throwable ->
                updateStatus(status = "Failed to Begin: ${throwable.message}")
            }
            purchaseSucceed { purchaseEntity ->
                updateStatus(status = "Purchase Succeed: ${purchaseEntity.payload}")
                // You can validate the purchase here using a server-side request
                // if it's one time purchase, give user the item here,
                // if it's not one time, you must consume it
                consume(purchaseEntity.purchaseToken, purchaseEntity.payload)
            }
            purchaseCanceled {
                updateStatus(status = "Purchase Canceled")
            }
            purchaseFailed { throwable ->
                updateStatus(status = "Purchase Failed: ${throwable.message}")
            }
        }
    }

    private fun consume(purchaseToken: String, payload: String) {
        payment.consumeProduct(purchaseToken = purchaseToken) {
            consumeSucceed {
                updateStatus(status = "$payload Consume Succeed")
                // Give user the purchased item here
            }
            consumeFailed { throwable ->
                updateStatus(status = "$payload Consume Failed: " + throwable.message)
            }
        }
    }

    override fun onDestroy() {
        paymentConnection.disconnect()
        super.onDestroy()
    }
}
