package com.example.vouchergenerator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class VoucherViewModel : ViewModel() {
    var customerName by mutableStateOf("")
        private set

    var address by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    var voucherDate by mutableStateOf(defaultDate())
        private set

    var deliveryFeeText by mutableStateOf("")
        private set

    var note by mutableStateOf("")
        private set

    private val _items = mutableStateListOf(
        newBlankItem()
    )

    val items: List<VoucherItemInput> get() = _items

    fun updateCustomerName(value: String) {
        customerName = value
    }

    fun updateAddress(value: String) {
        address = value
    }

    fun updatePhoneNumber(value: String) {
        phoneNumber = value
    }

    fun updateVoucherDate(value: String) {
        voucherDate = value
    }

    fun updateDeliveryFee(value: String) {
        deliveryFeeText = value
    }

    fun updateNote(value: String) {
        note = value
    }

    fun addItem() {
        _items.add(newBlankItem())
    }

    fun removeItem(id: Long) {
        if (_items.size == 1) return
        _items.removeAll { it.id == id }
    }

    fun updateItemName(id: Long, value: String) {
        updateItem(id) { copy(itemName = value) }
    }

    fun updateItemPrice(id: Long, value: String) {
        updateItem(id) { copy(priceText = value) }
    }

    fun updateItemQuantity(id: Long, value: String) {
        updateItem(id) { copy(quantityText = value) }
    }

    fun itemSubtotal(item: VoucherItemInput): Double {
        val price = item.priceText.toDoubleOrNull() ?: 0.0
        val qty = item.quantityText.toDoubleOrNull()?.roundToInt() ?: 0
        return price * qty
    }

    fun subtotal(): Double = items.sumOf(::itemSubtotal)

    fun total(): Double = subtotal() + deliveryFee()

    fun deliveryFee(): Double = deliveryFeeText.toDoubleOrNull() ?: 0.0

    fun resetForm() {
        customerName = ""
        address = ""
        phoneNumber = ""
        voucherDate = defaultDate()
        deliveryFeeText = ""
        note = ""
        _items.clear()
        _items.add(newBlankItem())
    }

    fun buildVoucherData(): VoucherData {
        val voucherItems = items.map {
            val price = it.priceText.toDoubleOrNull() ?: 0.0
            val qty = it.quantityText.toDoubleOrNull()?.roundToInt() ?: 0
            VoucherItem(
                itemName = it.itemName.ifBlank { "-" },
                price = price,
                quantity = qty,
                subtotal = price * qty
            )
        }

        val allSubtotal = voucherItems.sumOf { it.subtotal }
        val fee = deliveryFee()
        return VoucherData(
            customerName = customerName.ifBlank { "-" },
            address = address.ifBlank { "-" },
            phoneNumber = phoneNumber.ifBlank { "-" },
            voucherDate = voucherDate.ifBlank {
                LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            },
            items = voucherItems,
            subtotal = allSubtotal,
            deliveryFee = fee,
            note = note.ifBlank { "-" },
            total = allSubtotal + fee
        )
    }

    private fun updateItem(id: Long, transform: VoucherItemInput.() -> VoucherItemInput) {
        val idx = _items.indexOfFirst { it.id == id }
        if (idx >= 0) {
            _items[idx] = _items[idx].transform()
        }
    }

    private fun newBlankItem() = VoucherItemInput(
        id = System.nanoTime(),
        itemName = "",
        priceText = "",
        quantityText = "1"
    )

    private fun defaultDate(): String = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
}
