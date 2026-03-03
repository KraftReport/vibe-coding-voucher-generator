package com.example.vouchergenerator

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.IOException
import java.text.DecimalFormat
import java.util.Locale

object VoucherPngExporter {
    private val money = DecimalFormat("#,##0.00")

    fun export(context: Context, voucher: VoucherData): Result<Uri> {
        return runCatching {
            val bitmap = render(voucher)
            saveBitmap(context, bitmap)
        }
    }

    private fun render(voucher: VoucherData): Bitmap {
        val width = 1400
        val baseHeight = 1120
        val rowHeight = 74
        val dynamicHeight = voucher.items.size * rowHeight
        val bitmap = Bitmap.createBitmap(width, baseHeight + dynamicHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor("#F6FBFC"))

        val myanmarTypeface = Typeface.create("sans-serif", Typeface.NORMAL)

        val headerBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0E5E6F")
        }

        val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
        }

        val accentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1A8AA0")
        }

        val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 52f
            typeface = Typeface.create(myanmarTypeface, Typeface.BOLD)
            textLocale = Locale("my", "MM")
        }

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 32f
            typeface = myanmarTypeface
            textLocale = Locale("my", "MM")
        }

        val tableHeaderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#F0F8FA")
        }

        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#C7D8DD")
            strokeWidth = 2f
        }

        val headerRect = RectF(0f, 0f, width.toFloat(), 200f)
        canvas.drawRect(headerRect, headerBgPaint)
        canvas.drawRoundRect(RectF(36f, 42f, 132f, 142f), 16f, 16f, badgePaint)
        canvas.drawRect(RectF(56f, 66f, 112f, 74f), accentPaint)
        canvas.drawRect(RectF(56f, 86f, 112f, 94f), accentPaint)
        canvas.drawRect(RectF(56f, 106f, 96f, 114f), accentPaint)
        canvas.drawText("VOUCHER", 170f, 122f, headerPaint)

        var y = 270f
        canvas.drawText("Name: ${voucher.customerName}", 50f, y, textPaint)
        y += 52f
        canvas.drawText("Date: ${voucher.voucherDate}", 50f, y, textPaint)
        y += 52f
        canvas.drawText("Address: ${voucher.address}", 50f, y, textPaint)
        y += 52f
        canvas.drawText("Phone: ${voucher.phoneNumber}", 50f, y, textPaint)
        y += 52f
        canvas.drawText("Delivery Fee: ${money.format(voucher.deliveryFee)}", 50f, y, textPaint)
        y += 58f

        canvas.drawRoundRect(RectF(40f, y - 34f, width - 40f, y + 20f), 8f, 8f, tableHeaderPaint)
        canvas.drawLine(40f, y + 24f, width - 40f, y + 24f, linePaint)
        canvas.drawText("Item", 50f, y, textPaint)
        canvas.drawText("Price", 670f, y, textPaint)
        canvas.drawText("Qty", 880f, y, textPaint)
        canvas.drawText("Total", 1040f, y, textPaint)
        y += 36f

        voucher.items.forEach { item ->
            y += 52f
            canvas.drawText(item.itemName, 50f, y, textPaint)
            canvas.drawText(money.format(item.price), 670f, y, textPaint)
            canvas.drawText(item.quantity.toString(), 880f, y, textPaint)
            canvas.drawText(money.format(item.subtotal), 1040f, y, textPaint)
            y += 20f
            canvas.drawLine(40f, y, width - 40f, y, linePaint)
        }

        y += 72f
        canvas.drawRoundRect(RectF(650f, y - 56f, width - 40f, y + 18f), 10f, 10f, headerBgPaint)
        canvas.drawText("Grand Total: ${money.format(voucher.total)}", 680f, y, headerPaint)
        y += 64f
        canvas.drawText("Note: ${voucher.note}", 50f, y, textPaint)

        return bitmap
    }

    private fun saveBitmap(context: Context, bitmap: Bitmap): Uri {
        val name = "voucher_${System.currentTimeMillis()}.png"
        val resolver = context.contentResolver

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Vouchers")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: throw IOException("Unable to create MediaStore record")

        resolver.openOutputStream(uri)?.use { stream ->
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                throw IOException("Unable to compress bitmap")
            }
        } ?: throw IOException("Unable to open output stream")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val complete = ContentValues().apply {
                put(MediaStore.Images.Media.IS_PENDING, 0)
            }
            resolver.update(uri, complete, null, null)
        }

        return uri
    }
}
