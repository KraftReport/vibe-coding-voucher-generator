package com.example.vouchergenerator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                VoucherScreen()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun VoucherScreen(vm: VoucherViewModel = viewModel()) {
    val context = LocalContext.current
    val money = remember { DecimalFormat("#,##0.00") }
    val myanmarFont = remember { FontFamily.Default }
    val subtleCardColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
    val accent = Color(0xFF0E5E6F)
    val warm = Color(0xFF2E8A99)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Voucher") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFEAF5F7), Color(0xFFF7FBFC))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 12.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = accent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color.White, shape = MaterialTheme.shapes.medium),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = "Voucher icon",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Column {
                                Text("Voucher", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Generate PNG quickly", color = Color.White.copy(alpha = 0.9f))
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = subtleCardColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Customer Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = vm.customerName,
                            onValueChange = vm::updateCustomerName,
                            label = { Text("Customer Name") },
                            singleLine = true
                        )

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = vm.address,
                            onValueChange = vm::updateAddress,
                            label = { Text("Address") },
                            minLines = 2
                        )

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = vm.voucherDate,
                            onValueChange = vm::updateVoucherDate,
                            label = { Text("Date (YYYY-MM-DD)") },
                            singleLine = true
                        )
                    }
                }

                Text("Items", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                    vm.items.forEach { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = subtleCardColor)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = item.itemName,
                                    onValueChange = { vm.updateItemName(item.id, it) },
                                    label = { Text("Item") },
                                    singleLine = true
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        modifier = Modifier.weight(1f),
                                        value = item.priceText,
                                        onValueChange = { vm.updateItemPrice(item.id, it) },
                                        label = { Text("Price") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        modifier = Modifier.weight(1f),
                                        value = item.quantityText,
                                        onValueChange = { vm.updateItemQuantity(item.id, it) },
                                        label = { Text("Qty") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Line Total: ${money.format(vm.itemSubtotal(item))}", fontWeight = FontWeight.Medium)
                                    TextButton(onClick = { vm.removeItem(item.id) }) {
                                        Text("Remove")
                                    }
                                }
                            }
                        }
                    }

                Button(
                    onClick = vm::addItem,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = warm),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text("+ Add Item")
                }

                HorizontalDivider()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = subtleCardColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Voucher Preview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("Name: ${vm.customerName.ifBlank { "-" }}", fontFamily = myanmarFont)
                        Text("Date: ${vm.voucherDate}", fontFamily = myanmarFont)
                        Text("Address: ${vm.address.ifBlank { "-" }}", fontFamily = myanmarFont)
                        vm.items.forEach { item ->
                            Text(
                                "${item.itemName.ifBlank { "-" }} | ${item.priceText.ifBlank { "0" }} x ${item.quantityText.ifBlank { "0" }} = ${money.format(vm.itemSubtotal(item))}",
                                fontFamily = myanmarFont
                            )
                        }
                    }
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFE7F3F5))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "Grand Total: ${money.format(vm.total())}",
                            style = MaterialTheme.typography.titleLarge,
                            color = accent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B8A75)),
                    contentPadding = PaddingValues(vertical = 13.dp),
                    onClick = {
                        val result = VoucherPngExporter.export(context, vm.buildVoucherData())
                        result.onSuccess {
                            vm.resetForm()
                            Toast.makeText(context, "Voucher PNG saved to gallery", Toast.LENGTH_LONG).show()
                        }.onFailure {
                            Toast.makeText(context, "Failed: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Text("Generate PNG Voucher")
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
