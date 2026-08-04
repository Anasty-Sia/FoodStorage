package com.example.foodstorage.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodstorage.domain.Product
import com.example.foodstorage.ui.theme.FoodStorageTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProductsScreen(products: List<Product> = emptyList()) {
    FoodStorageTheme() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Food Storage", Modifier.padding(start =16.dp))

                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                )
            },
            floatingActionButton = {}
        ){
            Text(" Test")
        }

           // Column() {
                 // SearchBarBlock(){}
                //  FilterBlock(){}
              //  Box() { }
           // }


    }

}