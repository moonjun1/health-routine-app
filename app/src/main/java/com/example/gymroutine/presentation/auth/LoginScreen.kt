package com.example.gymroutine.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymroutine.util.Resource

/**
 * Login screen
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onSkipLogin: () -> Unit = {}
) {
    val loginState by viewModel.loginState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Handle login state
    LaunchedEffect(loginState) {
        when (loginState) {
            is Resource.Success -> {
                onLoginSuccess()
                viewModel.resetState()
            }
            is Resource.Error -> {
                errorMessage = (loginState as Resource.Error).message
                showError = true
            }
            is Resource.Loading -> {
                showError = false
            }
            is Resource.Idle -> {
                showError = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = "헬스 루틴",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("이메일") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is Resource.Loading
            )

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("비밀번호") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is Resource.Loading
            )

            // Error message
            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Login button
            Button(
                onClick = viewModel::login,
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is Resource.Loading
            ) {
                if (loginState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("로그인")
                }
            }

            // Signup button
            TextButton(
                onClick = onNavigateToSignup,
                enabled = loginState !is Resource.Loading
            ) {
                Text("회원가입")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Skip login button
            OutlinedButton(
                onClick = onSkipLogin,
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is Resource.Loading
            ) {
                Text("로그인 없이 계속하기")
            }

            Text(
                text = "로그인하지 않으면 루틴은 기기에만 저장됩니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
