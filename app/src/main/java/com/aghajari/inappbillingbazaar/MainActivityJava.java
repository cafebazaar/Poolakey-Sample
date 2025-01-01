package com.aghajari.inappbillingbazaar;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aghajari.inappbillingbazaar.poolakey.Configurations;
import com.aghajari.inappbillingbazaar.ui.content.BasePoolakeyActivity;

import ir.cafebazaar.poolakey.Connection;
import ir.cafebazaar.poolakey.ConnectionState;
import ir.cafebazaar.poolakey.Payment;
import ir.cafebazaar.poolakey.config.PaymentConfiguration;
import ir.cafebazaar.poolakey.config.SecurityCheck;
import ir.cafebazaar.poolakey.request.PurchaseRequest;
import kotlin.Unit;

public class MainActivityJava extends BasePoolakeyActivity {

    private Payment payment;
    private Connection paymentConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // UI handled on BasePoolakeyActivity

        SecurityCheck securityCheck = new SecurityCheck.Enable(Configurations.RSA_KEY);
        PaymentConfiguration paymentConfig = new PaymentConfiguration(securityCheck);
        payment = new Payment(this, paymentConfig);

        paymentConnection = payment.connect(connectionCallback -> {
            connectionCallback.connectionSucceed(() -> {
                updateStatus("Connected");
                return Unit.INSTANCE;
            });
            connectionCallback.connectionFailed(throwable -> {
                updateStatus("Failed: " + throwable.getMessage());
                return Unit.INSTANCE;
            });
            connectionCallback.disconnected(() -> {
                updateStatus("Disconnected");
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        });
    }

    @Override
    public void onClickSKU10KIRR() {
        purchase(
                new PurchaseRequest(
                        Configurations.PRODUCT_ID_1,
                        "SKU10",
                        null
                )
        );
    }

    @Override
    public void onClickSKU20KIRR() {
        purchase(
                new PurchaseRequest(
                        Configurations.PRODUCT_ID_2,
                        "SKU20",
                        null
                )
        );
    }

    private void purchase(PurchaseRequest request) {
        if (paymentConnection.getState() != ConnectionState.Connected.INSTANCE) {
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        payment.purchaseProduct(getActivityResultRegistry(), request, purchaseCallback -> {
            purchaseCallback.purchaseFlowBegan(() -> {
                updateStatus("Purchase Began");
                return Unit.INSTANCE;
            });
            purchaseCallback.failedToBeginFlow(throwable -> {
                updateStatus("Failed to Begin: " + throwable.getMessage());
                return Unit.INSTANCE;
            });
            purchaseCallback.purchaseSucceed(purchaseEntity -> {
                updateStatus("Purchase Succeed: " + purchaseEntity.getPayload());
                // You can validate the purchase here using a server-side request
                // if it's one time purchase, give user the item here,
                // if it's not one time, you must consume it
                consume(purchaseEntity.getPurchaseToken(), purchaseEntity.getPayload());
                return Unit.INSTANCE;
            });
            purchaseCallback.purchaseCanceled(() -> {
                updateStatus("Purchase Canceled");
                return Unit.INSTANCE;
            });
            purchaseCallback.purchaseFailed(throwable -> {
                updateStatus("Purchase Failed: " + throwable.getMessage());
                return Unit.INSTANCE;
            });

            return Unit.INSTANCE;
        });
    }

    private void consume(String purchaseToken, String payload) {
        payment.consumeProduct(purchaseToken, consumeCallback -> {
            consumeCallback.consumeSucceed(() -> {
                updateStatus(payload + " Consume Succeed");
                // Give user the purchased item here
                return Unit.INSTANCE;
            });
            consumeCallback.consumeFailed(throwable -> {
                updateStatus(payload + " Consume Failed: " + throwable.getMessage());
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        });
    }

    @Override
    public void onDestroy() {
        paymentConnection.disconnect();
        super.onDestroy();
    }
}
