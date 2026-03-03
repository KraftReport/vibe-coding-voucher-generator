package com.example.vouchergenerator

data class VoucherItemInput(
    val id: Long,
    val itemName: String,
    val priceText: String,
    val quantityText: String
)

data class VoucherItem(
    val itemName: String,
    val price: Double,
    val quantity: Int,
    val subtotal: Double
)

data class VoucherData(
    val customerName: String,
    val address: String,
    val phoneNumber: String,
    val voucherDate: String,
    val items: List<VoucherItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val note: String,
    val total: Double
)
