package hu.ait.weatherapp.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import hu.ait.weatherapp.ui.data.CityItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CityListScreen(navController: NavHostController, cityViewModel: CityViewModel = viewModel()) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var newCityName by rememberSaveable { mutableStateOf("") }
    var showErrorPrompt by rememberSaveable { mutableStateOf(false) }
    var errorPromptText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather App") },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add City")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                    newCityName = ""
                    showErrorPrompt = false
                    errorPromptText = ""
                },
                content = { Icon(Icons.Default.Add, contentDescription = "Add City") }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                item {
                    // Header item
                    WeatherAppHeader()
                }
                items(cityViewModel.getAllCities().size) { index ->
                    CityListItem(
                        city = cityViewModel.getAllCities()[index],
                        onRemoveClick = { cityViewModel.removeCity(cityViewModel.getAllCities()[index]) },
                        navigateToDetails = { city ->
                            // Navigate to WeatherDetailsScreen when "Click for Details" is clicked
                            navController.navigate("weatherDetails/${city.name}")
                        }
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    newCityName = ""
                    showErrorPrompt = false
                    errorPromptText = ""
                },
                title = { Text("Add City") },
                text = {
                    Column {
                        TextField(
                            value = newCityName,
                            onValueChange = { newCityName = it },
                            label = { Text("City Name") },
                            isError = showErrorPrompt && newCityName.isBlank(),
                            singleLine = true,
                        )

                        if (showErrorPrompt) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Error")
                                Text(errorPromptText, color = Color.Red)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        handleAddCity(
                            newCityName = newCityName,
                            cityViewModel = cityViewModel,
                            onErrorHandle = {
                                showErrorPrompt = true
                                errorPromptText = it
                            },
                            onSucces = { showDialog = false }
                        )
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialog = false
                        newCityName = ""
                        showErrorPrompt = false
                        errorPromptText = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun WeatherAppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Weather App",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun CityListItem(
    city: CityItem,
    onRemoveClick: () -> Unit,
    navigateToDetails: (CityItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // City name in bold
        Text(
            text = city.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp) // Add spacing at the bottom
        )

        // "Click for Details" button
        Button(
            onClick = {
                // Navigate to details screen
                navigateToDetails(city)
            },
            modifier = Modifier
                .fillMaxWidth() // Expand the button to the full width
                .padding(bottom = 8.dp) // Add spacing at the bottom
        ) {
            Text(text = "Click for Details")
        }

        // Delete button to the right end
        Button(
            onClick = { onRemoveClick() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Del")
        }

        // Divider to create a border line
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun handleAddCity(
    newCityName: String,
    cityViewModel: CityViewModel,
    onErrorHandle: (String) -> Unit,
    onSucces: () -> Unit
) {
    if (newCityName.isNotBlank()) {
        if (newCityName.matches(Regex("[a-zA-Z ]+"))) {
            // Allow letters and spaces in the city name
            cityViewModel.addCity(CityItem(newCityName))

            // Reset the city name field after adding a city
            onSucces()
        } else {
            onErrorHandle("Error: city name can only contain letters and spaces")
        }
    } else {
        onErrorHandle("Error: city name is blank")
    }
}