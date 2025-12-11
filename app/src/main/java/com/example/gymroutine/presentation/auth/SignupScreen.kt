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

// 회원가입 화면
@Composable
fun SignupScreen(
    viewModel: SignupViewModel = hiltViewModel(),
    onSignupSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val signupState by viewModel.signupState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // 회원가입 상태 처리
    LaunchedEffect(signupState) {
        when (signupState) {
            is Resource.Success -> {
                onSignupSuccess()
                viewModel.resetState()
            }
            is Resource.Error -> {
                errorMessage = (signupState as Resource.Error).message
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
            // 제목
            Text(
                text = "회원가입",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // 이메일 입력 필드
            OutlinedTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("이메일") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = signupState !is Resource.Loading
            )

            // 비밀번호 입력 필드
            OutlinedTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("비밀번호 (6자 이상)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = signupState !is Resource.Loading
            )

            // 비밀번호 확인 입력 필드
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = { Text("비밀번호 확인") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = signupState !is Resource.Loading
            )

            // 오류 메시지
            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 회원가입 버튼
            Button(
                onClick = viewModel::signup,
                modifier = Modifier.fillMaxWidth(),
                enabled = signupState !is Resource.Loading
            ) {
                if (signupState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("가입하기")
                }
            }

            // 뒤로가기 버튼
            TextButton(
                onClick = onNavigateBack,
                enabled = signupState !is Resource.Loading
            ) {
                Text("이미 계정이 있으신가요? 로그인")
            }
        }
    }
}
