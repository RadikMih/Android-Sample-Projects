package com.radikmih.preferences_datastore_example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.radikmih.datastore_example.ui.theme.DataStoreExampleTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


private const val USER_SETTINGS = "settings"

class MainActivity : ComponentActivity() {

    private val dataStore by preferencesDataStore(
        name = USER_SETTINGS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DataStoreExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Greeting(
                        save = { key, value ->
                            lifecycleScope.launch { save(key, value) }
                        },
                        read = { key ->
                            read(key)
                        }
                    )
                }
            }
        }
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { setting ->
            setting[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}


@Composable
fun Greeting(
    save: suspend (String, String) -> Unit,
    read: suspend (String) -> String?
) {

    val keyTextState = remember { mutableStateOf(TextFieldValue()) }
    val valueTextState = remember { mutableStateOf(TextFieldValue()) }

    val readKeyState = remember { mutableStateOf(TextFieldValue()) }
    val readValueState = remember { mutableStateOf("") }

    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = keyTextState.value,
            onValueChange = { keyTextState.value = it },
            label = { Text("Enter key") },
            modifier = Modifier.padding(top = 20.dp)
        )

        TextField(
            value = valueTextState.value,
            onValueChange = { valueTextState.value = it },
            label = { Text("Enter value") },
            modifier = Modifier.padding(top = 20.dp)
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                lifecycleOwner.lifecycleScope.launch {
                    save(keyTextState.value.text, valueTextState.value.text)
                }
            }
        ) {
            Text(text = "SAVE")
        }

        Spacer(modifier = Modifier.padding(30.dp))

        TextField(
            value = readKeyState.value,
            onValueChange = { readKeyState.value = it },
            label = { Text("Enter key") },
            modifier = Modifier.padding(top = 20.dp)
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                lifecycleOwner.lifecycleScope.launch {
                    val result = read(readKeyState.value.text)
                    readValueState.value = result ?: "Key not found"
                }
            }) {
            Text(text = "READ")
        }

        Text(
            text = readValueState.value,
            modifier = Modifier.padding(top = 16.dp)
        )
    }

}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun GreetingPreview() {
    DataStoreExampleTheme {
        Greeting(
            save = { _, _ -> /* Do nothing */ },
            read = { _ ->
                runBlocking {
                    "Value"
                }
            }
        )
    }
}