package com.example.apparchitectureexample.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apparchitectureexample.viewmodel.PetsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PetList(modifier: Modifier) {
    // 没有从上层UI中，拿任何数据
    // get object from DI(koin)
    //- `koinViewModel()` **自动从 Koin 容器里获取 `PetsViewModel`**。
    //- `PetsViewModel` 里的 `PetsRepository` **已经被 Koin 注入**，
    // 所以我们可以直接调用 `getPets()`。
    val petsViewModel: PetsViewModel = koinViewModel()
    LazyColumn(
        modifier = modifier
    ) {
       items(petsViewModel.getPets()) { pet ->
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(10.dp),
               horizontalArrangement = Arrangement.SpaceBetween
           ) {
               Text(text = "Name: ${pet.name}")
               Text(text = "Species: ${pet.species}")
           }
       }
    }
}