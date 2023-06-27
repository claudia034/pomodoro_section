package com.example.pomodorosec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTimer()
        }
    }
}

@Composable
fun PomodoroTimer() {
    var timerRunning by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(25 * 60) }

    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            while (timeRemaining > 0) {
                delay(1000)
                timeRemaining--
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatTime(timeRemaining),
            //style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Button(
                onClick = { timerRunning = true },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Start")
            }

            Button(
                onClick = { timerRunning = false },
                enabled = timerRunning,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Pause")
            }

            Button(
                onClick = {
                    timerRunning = false
                    timeRemaining = 25 * 60
                }
            ) {
                Text(text = "Reset")
            }
        }

        if (timerRunning) {
            Text(
                text = "Reminder: Take a break",
                //style = MaterialTheme.typography.h5,
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PomodoroTimer()
}

fun formatTime(timeInSeconds: Int): String {
    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

