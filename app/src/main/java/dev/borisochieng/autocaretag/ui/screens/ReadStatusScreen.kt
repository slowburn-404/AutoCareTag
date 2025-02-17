package dev.borisochieng.autocaretag.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.borisochieng.autocaretag.R
import dev.borisochieng.autocaretag.nfcreader.data.State
import dev.borisochieng.autocaretag.nfcreader.ui.NFCReaderViewModel
import dev.borisochieng.autocaretag.ui.navigation.Screens
import dev.borisochieng.autocaretag.ui.navigation.ShouldScan
import dev.borisochieng.autocaretag.ui.theme.AutoCareTheme.colorScheme
import dev.borisochieng.autocaretag.ui.theme.AutoCareTheme.shape
import dev.borisochieng.autocaretag.ui.theme.AutoCareTheme.typography
import kotlinx.coroutines.delay

@Composable
fun ReadStatusScreen(
    viewModel: NFCReaderViewModel,
    scanNFC: (ShouldScan) -> Unit,
    navigate: (Screens) -> Unit
) {
    LaunchedEffect(Unit) { scanNFC(true) }

    DisposableEffect(Unit) {
        onDispose { scanNFC(false) }
    }

    var readyToScan by remember { mutableStateOf("") }
    var supportingText by remember { mutableStateOf("") }

    val uiState by viewModel.clientUiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .size(width = 280.dp, height = 280.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.background
            ),
            shape = shape.card
        ) {
            // Add your UI elements here
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sorry Toby. I had to refactor this

                val nfcReadState by viewModel.nfcReadState.collectAsState()
                when (nfcReadState) {
                    is State.Success -> {
                        // Show success UI
                        readyToScan = "Successful"
                        supportingText = "Details imported successfully"
                        Text(
                            text = readyToScan,
                            style = typography.title,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(
                            text = supportingText,
                            style = typography.bodyLarge,
                            modifier = Modifier.padding(4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Green, shape = CircleShape)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(100.dp),
                                imageVector = Icons.Rounded.Check,
                                tint = Color.White,
                                contentDescription = "Successful"
                            )
                        }
                    }

                    is State.Error -> {
                        // Show error UI
                        supportingText = (nfcReadState as State.Error).errorMessage

                        Text(
                            text = supportingText,
                            style = typography.bodyLarge,
                            modifier = Modifier.padding(4.dp)
                        )

                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Green, shape = CircleShape)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(100.dp),
                                imageVector = Icons.Rounded.Close,
                                tint = colorScheme.error,
                                contentDescription = "Error"
                            )
                        }
                    }

                    is State.Loading -> {
                        // Show loading UI
                        //CircularProgressIndicator()
                        readyToScan = "Scanning"
                        supportingText = "Hold your device near the NFC Tag"
                        Text(
                            text = readyToScan,
                            style = typography.title,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(
                            text = supportingText,
                            style = typography.bodyLarge,
                            modifier = Modifier.padding(4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier.clip(CircleShape),
                                painter = painterResource(id = R.drawable.scanning),
                                contentDescription = "Scanning",
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                LaunchedEffect(nfcReadState) {
                    if (nfcReadState is State.Error) {
                        delay(2000)
                        navigate(Screens.Back)
                    }
                    if (nfcReadState !is State.Success) return@LaunchedEffect
                    delay(2000)
                    val screen = if (viewModel.tagIsEmpty) {
                        Screens.AddScreen
                    } else uiState.client?.clientId?.let {
                        Screens.ClientDetailsScreen(
                            it
                        )
                    }
                    if (screen != null) {
                        navigate(screen)
                    }
                }
            }


        }

    }
}