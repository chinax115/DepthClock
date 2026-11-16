package com.depthclock.app

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextCompat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DepthClockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onApplyWallpaper = {
                            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                                putExtra(
                                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                    ComponentName(this@MainActivity, DepthWallpaperService::class.java)
                                )
                            }
                            try {
                                startActivity(intent)
                            } catch (e: Exception) {
                                val fallbackIntent = Intent(WallpaperManager.ACTION_SET_WALLPAPER)
                                startActivity(fallbackIntent)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DepthClockTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF00E676),
            secondary = Color(0xFF10B981),
            background = Color(0xFF0A0F0D),
            surface = Color(0xFF121A16),
            onPrimary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        ),
        content = content
    )
}

@Composable
fun MainScreen(onApplyWallpaper: () -> Unit) {
    var currentTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            kotlinx.coroutines.delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A0F0D), Color(0xFF052E16))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "DepthClock",
                fontSize = 32.sp,
                fontWeight = Bold,
                color = Color(0xFF00E676)
            )
            Text(
                text = "iOS 26 & 27 Spatial Wallpaper Engine",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Live Preview Box simulating Depth Effect
        Box(
            modifier = Modifier
                .width(220.dp)
                .height(400.dp)
                .background(Color.DarkGray, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Simulated Background Gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF10B981), Color(0xFF0A0F0D))
                        ),
                        RoundedCornerShape(24.dp)
                    )
            )
            // Simulated Clock in Middle Layer
            Text(
                text = currentTime.ifEmpty { "12:48" },
                fontSize = 48.sp,
                fontWeight = Bold,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onApplyWallpaper,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
            ) {
                Text(
                    text = "Apply Live Wallpaper",
                    fontSize = 16.sp,
                    fontWeight = Bold,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "100% Free • Works on Vivo Y22, T1, Y200 & All Devices",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
