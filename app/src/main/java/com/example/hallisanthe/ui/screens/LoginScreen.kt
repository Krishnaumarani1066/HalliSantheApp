package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.hallisanthe.ui.components.AppLogo
import com.example.hallisanthe.ui.theme.DeepGreen
import com.example.hallisanthe.ui.theme.SoftMint
import com.example.hallisanthe.ui.theme.TextMuted

@Composable
fun LoginScreen(
    initialEmail: String = "",
    initialPassword: String = "",
    error: String? = null,
    onLogin: (String, String, Boolean) -> Unit,
    onClearError: () -> Unit = {},
    onNavigateToRegister: () -> Unit,
    onBack: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf(initialEmail) }
    var password by rememberSaveable { mutableStateOf(initialPassword) }
    var rememberMe by rememberSaveable { mutableStateOf(initialEmail.isNotBlank()) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(initialEmail, initialPassword) {
        if (email.isBlank()) email = initialEmail
        if (password.isBlank()) password = initialPassword
        if (initialEmail.isNotBlank()) rememberMe = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftMint)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo(size = 80.dp)
            Spacer(Modifier.height(24.dp))
            
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = DeepGreen
            )
            Text(
                text = "Login with your Firebase account",
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted
            )
            
            Spacer(Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (error != null) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; onClearError() },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email Address") },
                        placeholder = { Text("Enter your email") },
                        leadingIcon = { Icon(Icons.Rounded.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; onClearError() },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Password") },
                        placeholder = { Text("Enter your password") },
                        leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(checkedColor = DeepGreen)
                        )
                        Text("Remember me", style = MaterialTheme.typography.bodyMedium)
                    }

                    Button(
                        onClick = { onLogin(email, password, rememberMe) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepGreen),
                        enabled = email.isNotBlank() && password.isNotBlank()
                    ) {
                        Text("Login", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account?", color = TextMuted)
                TextButton(onClick = onNavigateToRegister) {
                    Text("Create now", color = DeepGreen, fontWeight = FontWeight.Bold)
                }
            }
            
            TextButton(onClick = onBack) {
                Text("Back to Start", color = TextMuted)
            }
        }
    }
}
