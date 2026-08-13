package com.example.foodstorage.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.foodstorage.domain.Product
import com.example.foodstorage.ui.theme.FoodStorageTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    selectedLocation: String,
    onLocationChange: (String) -> Unit,
    onSave: (Product) -> Unit = {},
    onBack: () -> Unit = {},
    ) {

    var productName by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var productShelfLife by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val options =  listOf("Холодильник", "Полка")
    lateinit var productShelfLifeLD: LocalDate
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    FoodStorageTheme() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Food Storage",
                            color = MaterialTheme.colorScheme.background,
                            style = MaterialTheme.typography.titleLarge
                        )

                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        actionIconContentColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.background
                    ),
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 8.dp)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Название продукта") },
                    placeholder = { Text("Введите название продукта") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true

                )

                Spacer(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(all = 1.dp)
                        .fillMaxWidth()
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    OutlinedTextField(
                        value = productQuantity,
                        onValueChange = { productQuantity = it },
                        label = { Text("Количество") },
                        placeholder = { Text("Введите количество продукта") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(end = 8.dp),
                        singleLine = true,

                        )

                    OutlinedTextField(
                        value = productShelfLife,
                        onValueChange = { productShelfLife = it },
                        label = { Text("Срок годности") },
                        placeholder = { Text("Введите срок годности продукта") },
                        modifier = Modifier
                            .weight(0.5f),
                        singleLine = true

                    )


                }

                Spacer(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(all = 1.dp)
                        .fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                    ) {
                        OutlinedTextField(
                            value = selectedLocation,
                            onValueChange = {},
                            label = { Text("Место хранения") },
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = "Выбрать место хранения"
                                    )
                                }
                            },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        onLocationChange(option)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }


                Button(

                    onClick = {

                        if (productName.isBlank()) {
                            Error("Заполните название")
                            return@Button
                        }

                        val productQuantityDouble = productQuantity.toDoubleOrNull()
                        if (productQuantity.isBlank() || productQuantityDouble == null
                            || productQuantityDouble <= 0
                        ) {
                            Error("Введите кол-во ")
                            return@Button
                        }

                        if (productShelfLife.isBlank()) {
                            Error("Заполните срок годности")
                            return@Button
                        } else {
                            productShelfLifeLD = try {
                                LocalDate.parse(productShelfLife, formatter)
                            } catch (e: DateTimeParseException) {
                                Error("Заполните дату в формате дд.мм.гггг")
                                return@Button
                            }
                        }

                        if (selectedLocation.isBlank()) {
                            Error("Выберите место хранения")
                            return@Button
                        }

                        onSave(
                            Product(
                                0,
                                name = productName,
                                quantity = productQuantityDouble,
                                storagePlace = selectedLocation,
                                shelfLife = productShelfLifeLD
                            )
                        )
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Сохранить")

                }

            }
        }
    }


}

