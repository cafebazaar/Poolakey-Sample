package com.aghajari.inappbillingbazaar.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aghajari.inappbillingbazaar.R
import com.aghajari.inappbillingbazaar.ui.theme.InappbillingbazaarTheme


@Composable
fun MainScreen(
    status: String,
    modifier: Modifier = Modifier,
    onClickSKU10KIRR: () -> Unit = {},
    onClickSKU20KIRR: () -> Unit = {},
) {
    InappbillingbazaarTheme {
        Scaffold(
            topBar = { AppBar() },
            modifier = modifier,
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Text(
                    text = "IAB Status:",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = Color(70, 157, 86),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                Text(
                    text = status,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp),
                )

                SKUButton(text = "10,000 IRR", onClick = onClickSKU10KIRR)
                SKUButton(text = "20,000 IRR", onClick = onClickSKU20KIRR)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(175, 225, 175),
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Unspecified
        ),
        title = {
            Text(
                "In-App-Billing Bazaar",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bazaar_logo),
                    contentDescription = "Localized description",
                    tint = Color.Unspecified,
                )
            }
        },
    )
}

@Composable
fun SKUButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier.fillMaxWidth(0.6f),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color(70, 157, 86),
        ),
        onClick = onClick,
    ) {
        Text(
            text = text,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(status = "Not Connected")
}