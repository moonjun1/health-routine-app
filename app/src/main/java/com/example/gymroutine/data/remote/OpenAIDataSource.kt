package com.example.gymroutine.data.remote

import android.util.Log
import com.example.gymroutine.BuildConfig
import com.example.gymroutine.data.model.AIRoutineRequest
import com.example.gymroutine.data.model.AIRoutineResponse
import com.example.gymroutine.data.model.GymEquipment
import com.example.gymroutine.data.model.openai.Message
import com.example.gymroutine.data.model.openai.OpenAIRequest
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

// AI 루틴 생성을 위한 OpenAI API 데이터 소스
@Singleton
class OpenAIDataSource @Inject constructor(
    private val openAIApiService: OpenAIApiService
) {
    companion object {
        private const val TAG = "OpenAIDataSource"
    }

    private val gson = Gson()

// GPT를 사용하여 운동 루틴 생성
    suspend fun generateRoutine(request: AIRoutineRequest): AIRoutineResponse {
        // 확인: API key is configured
        val apiKey = BuildConfig.OPENAI_API_KEY
        if (apiKey.isEmpty() || apiKey == "YOUR_OPENAI_API_KEY_HERE" || apiKey == "") {
            val errorMsg = "OpenAI API 키가 설정되지 않았습니다. local.properties에 OPENAI_API_KEY를 추가해주세요."
            Log.e(TAG, errorMsg)
            throw Exception(errorMsg)
        }

        Log.d(TAG, "generateRoutine: Generating routine with OpenAI API")
        Log.d(TAG, "Request - Goal: ${request.goal}, Level: ${request.experienceLevel}, Equipment: ${request.equipment.size}")
        val equipmentList = if (request.equipment.isNotEmpty()) {
            """
            **이 헬스장의 보유 기구 목록 (반드시 이 목록에 있는 기구만 사용):**
            ${request.equipment.joinToString("\n") { "- $it" }}
            """.trimIndent()
        } else {
            GymEquipment.getEquipmentDescription()
        }

        val systemPrompt = """
            당신은 전문 퍼스널 트레이너입니다. 사용자의 목표와 조건에 맞는 최적의 운동 루틴을 생성해주세요.

            **중요: 반드시 아래 헬스장 보유 기구 목록에 있는 기구만 사용하여 루틴을 생성하세요.**

            $equipmentList

            ${GymEquipment.getExperienceLevelGuidelines()}

            응답은 반드시 다음 JSON 형식으로만 제공해주세요:
            {
              "routineName": "루틴 이름",
              "description": "루틴 설명 (1-2문장)",
              "category": "카테고리 (상체/하체/전신/코어 중 하나)",
              "exercises": [
                {
                  "exerciseId": "운동 ID (아래 목록에서 선택)",
                  "exerciseName": "운동 이름",
                  "equipment": "사용 기구 (위 헬스장 보유 기구 목록에서 선택)",
                  "sets": 세트 수 (숫자),
                  "reps": 반복 횟수 (숫자),
                  "weight": 0,
                  "restTime": 휴식 시간(초, 숫자),
                  "notes": "운동 설명 (한 문장)"
                }
              ]
            }

            사용 가능한 운동 목록 (exerciseId와 exerciseName):
            가슴: ex001(벤치프레스), ex002(인클라인 벤치프레스), ex003(덤벨 플라이), ex004(푸시업), ex005(체스트 프레스 머신), ex006(펙 덱 플라이), ex007(케이블 크로스오버)
            등: ex008(데드리프트), ex009(바벨 로우), ex010(랫 풀다운), ex011(풀업), ex012(케이블 로우), ex013(로우 머신)
            어깨: ex014(숄더 프레스), ex015(덤벨 숄더 프레스), ex016(사이드 레터럴 레이즈), ex017(페이스 풀), ex018(래터럴 레이즈 머신)
            팔: ex019(바벨 컬), ex020(덤벨 컬), ex021(해머 컬), ex022(트라이셉 익스텐션), ex023(케이블 컬), ex024(프리처 컬)
            하체: ex025(바벨 스쿼트), ex026(레그 프레스), ex027(레그 컬), ex028(레그 익스텐션), ex029(칼프 레이즈)
            코어: ex030(플랭크), ex031(크런치), ex032(레그 레이즈), ex033(사이드 플랭크)
            유산소: ex034(런닝머신), ex035(사이클), ex036(로잉 머신)

            주의사항:
            - weight는 항상 0으로 설정 (사용자가 직접 입력)
            - 운동 순서는 큰 근육 → 작은 근육, 복합운동 → 고립운동
            - 각 운동마다 equipment 필드에 헬스장 보유 기구 목록에서 실제 사용할 기구를 명시하세요
            - 경험 수준에 따라 운동 개수, 세트, 반복, 휴식 시간을 위 가이드라인에 맞춰 조정하세요
        """.trimIndent()

        val userPrompt = """
            다음 조건에 맞는 운동 루틴을 생성해주세요:
            - 목표: ${request.goal}
            - 경력: ${request.experienceLevel}
            - 주당 운동 횟수: ${request.workoutsPerWeek}회
            - 운동 시간: ${request.workoutDuration}분
            - 선호 부위: ${request.preferredCategories.joinToString(", ")}
            ${if (request.additionalInfo.isNotEmpty()) "- 추가 정보: ${request.additionalInfo}" else ""}

            위 JSON 형식으로만 응답해주세요. 다른 텍스트는 포함하지 마세요.
        """.trimIndent()

        val openAIRequest = OpenAIRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                Message(role = "system", content = systemPrompt),
                Message(role = "user", content = userPrompt)
            ),
            temperature = 0.7,
            maxTokens = 2000
        )

        return try {
            val response = openAIApiService.createChatCompletion(
                authorization = "Bearer $apiKey",
                request = openAIRequest
            )

            Log.d(TAG, "OpenAI API response received successfully")

            val content = response.choices.firstOrNull()?.message?.content
                ?: throw Exception("GPT 응답이 비어있습니다")

            Log.d(TAG, "Response content: ${content.take(200)}...")

            // Parse JSON response
            try {
                gson.fromJson(content, AIRoutineResponse::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse GPT response: ${e.message}")
                Log.e(TAG, "Response content: $content")
                throw Exception("GPT 응답 파싱 실패: ${e.message}\n응답: $content")
            }
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e(TAG, "HTTP ${e.code()} error: $errorBody")
            when (e.code()) {
                401 -> throw Exception("OpenAI API 키가 유효하지 않습니다. (401 Unauthorized)")
                429 -> throw Exception("API 요청 한도를 초과했습니다. (429 Too Many Requests)")
                500, 502, 503 -> throw Exception("OpenAI 서버 오류입니다. 잠시 후 다시 시도해주세요.")
                else -> throw Exception("API 요청 실패 (${e.code()}): ${e.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to generate routine", e)
            throw e
        }
    }
}
