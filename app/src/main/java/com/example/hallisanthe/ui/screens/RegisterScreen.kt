package com.example.hallisanthe.ui.screens

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
import com.example.hallisanthe.data.UserRole
import com.example.hallisanthe.ui.components.AppLogo
import com.example.hallisanthe.ui.theme.DeepGreen
import com.example.hallisanthe.ui.theme.EarthWhite
import com.example.hallisanthe.ui.theme.TextMuted

@Composable
fun RegisterScreen(
    error: String? = null,
    onRegister: (String, String, String, String, String, UserRole) -> Unit,
    onClearError: () -> Unit = {},
    onBack: () -> Unit
) {
    var role by rememberSaveable { mutableStateOf(UserRole.Buyer) }
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var locationError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Clear ViewModel errors when the user changes any input
    LaunchedEffect(name, email, phone, location, password, role) {
        onClearError()
        // Also clear local validation errors when user types
        nameError = null
        emailError = null
        phoneError = null
        locationError = null
        passwordError = null
    }

    // Stop loading state if a ViewModel error is received
    LaunchedEffect(error) {
        if (error != null) {
            isLoading = false
        }
    }

    fun validate(): Boolean {
        var isValid = true
        
        if (name.isBlank()) {
            nameError = "Name is required"
            isValid = false
        } else if (name.any { it.isDigit() }) {
            nameError = "Name cannot contain numbers"
            isValid = false
        }

        if (email.isBlank() || !email.contains("@")) {
            emailError = "Valid email is required"
            isValid = false
        }

        if (phone.length != 10 || !phone.all { it.isDigit() }) {
            phoneError = "Phone must be exactly 10 digits"
            isValid = false
        }

        if (location.isBlank()) {
            locationError = "Location is required"
            isValid = false
        }

        if (password.length < 4) {
            passwordError = "Password must be at least 4 characters"
            isValid = false
        }
        
        return isValid
    }

    Scaffold(containerColor = EarthWhite) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            IconButton(onClick = onBack, enabled = !isLoading) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = DeepGreen)
            }
            AppLogo(size = 64.dp)
            Column {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepGreen
                )
                Text(
                    text = "Join our digital village market today.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted
                )
            }

            if (error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                UserRole.entries.forEach { item ->
                    FilterChip(
                        selected = role == item,
                        onClick = { role = item },
                        label = { Text(item.label) },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepGreen.copy(alpha = 0.1f),
                            selectedLabelColor = DeepGreen
                        )
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Full Name") },
                        placeholder = { Text("Enter your name") },
                        leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null) },
                        isError = nameError != null,
                        supportingText = nameError?.let { { Text(it) } },
                        singleLine = true,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email Address") },
                        placeholder = { Text("example@mail.com") },
                        leadingIcon = { Icon(Icons.Rounded.Email, contentDescription = null) },
                        isError = emailError != null,
                        supportingText = emailError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Phone Number") },
                        placeholder = { Text("10-digit number") },
                        leadingIcon = { Icon(Icons.Rounded.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError != null,
                        supportingText = phoneError?.let { { Text(it) } },
                        singleLine = true,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Location") },
                        placeholder = { Text("Village / City") },
                        leadingIcon = { Icon(Icons.Rounded.LocationOn, contentDescription = null) },
                        isError = locationError != null,
                        supportingText = locationError?.let { { Text(it) } },
                        singleLine = true,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Password") },
                        placeholder = { Text("Min 4 characters") },
                        leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        isError = passwordError != null,
                        supportingText = passwordError?.let { { Text(it) } },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        singleLine = true,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            
            Button(
                onClick = {
                    if (validate()) {
                        isLoading = true
                        onRegister(name, email, phone, location, password, role)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreen),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Create Account", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
