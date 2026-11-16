package com.depthclock.app

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import java.text.SimpleDateFormat
import java.util.*

class DepthWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine {
        return DepthEngine()
    }

    inner class DepthEngine : Engine() {
        private val handler = Handler(Looper.getMainLooper())
        private var visible = false
        private val paint = Paint().apply {
            isAntialias = true
            textAlign = Paint.Align.CENTER
        }

        private val drawRunner = Runnable { drawFrame() }

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                drawFrame()
            } else {
                handler.removeCallbacks(drawRunner)
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            visible = false
            handler.removeCallbacks(drawRunner)
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            val canvas: Canvas? = holder.lockCanvas()
            if (canvas != null) {
                try {
                    drawWallpaperContent(canvas)
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
            handler.removeCallbacks(drawRunner)
            if (visible) {
                handler.postDelayed(drawRunner, 1000)
            }
        }

        private fun drawWallpaperContent(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()

            // 1. Background Layer (Deep Emerald Gradient)
            paint.color = Color.parseColor("#0A0F0D")
            canvas.drawRect(0f, 0f, width, height, paint)

            paint.color = Color.parseColor("#052E16")
            canvas.drawCircle(width / 2f, height / 3f, width * 0.8f, paint)

            // 2. Middle Layer: Live Clock (iOS 26 Style)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeString = timeFormat.format(Date())

            paint.textSize = 140f
            paint.color = Color.WHITE
            paint.isFakeBoldText = true
            
            // Draw clock centered horizontally, positioned in upper third
            canvas.drawText(timeString, width / 2f, height * 0.3f, paint)

            // 3. Foreground Hint / Label
            paint.textSize = 40f
            paint.color = Color.parseColor("#00E676")
            canvas.drawText("DepthClock Live", width / 2f, height * 0.85f, paint)
        }
    }
}
