package com.example.gymroutine.data.remote

import com.example.gymroutine.BuildConfig
import com.example.gymroutine.data.model.AIRoutineRequest
import com.example.gymroutine.data.model.AIRoutineResponse
import com.example.gymroutine.data.model.openai.Message
import com.example.gymroutine.data.model.openai.OpenAIRequest
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OpenAI API data source for AI routine generation
 */
@Singleton
class OpenAIDataSource @Inject constructor(
    private val openAIApiService: OpenAIApiService
) {
    private val gson = Gson()

    /**
     * Generate workout routine using GPT
     */
    suspend fun generateRoutine(request: AIRoutineRequest): AIRoutineResponse {
        val systemPrompt = """
            당신은 전문 퍼스널 트레이너입니다. 사용자의 목표와 조건에 맞는 최적의 운동 루틴을 생성해주세요.

            응답은 반드시 다음 JSON 형식으로만 제공해주세요:
            {
              "routineName": "루틴 이름",
              "description": "루틴 설명 (1-2문장)",
              "category": "카테고리 (상체/하체/전신/코어 중 하나)",
              "exercises": [
                {
                  "exerciseId": "운동 ID (아래 목록에서 선택)",
                  "exerciseName": "운동 이름",
                  "sets": 세트 수 (숫자),
                  "reps": 반복 횟수 (숫자),
                  "weight": 0,
                  "restTime": 휴식 시간(초, 숫자),
                  "notes": "운동 설명 (한 문장)"
                }
              ]
            }

            사용 가능한 운동 목록 (exerciseId와 exerciseName):
            가슴: ex001(벤치프레스), ex002(인클라인 벤치프레스), ex003(덤벨 플라이), ex004(푸시업)
            등: ex005(데드리프트), ex006(바벨 로우), ex007(랫 풀다운), ex008(풀업)
            어깨: ex009(숄더 프레스), ex010(사이드 레터럴 레이즈), ex011(페이스 풀)
            팔: ex012(바벨 컬), ex013(트라이셉 익스텐션), ex014(해머 컬)
            하체: ex015(스쿼트), ex016(레그 프레스), ex017(레그 컬), ex018(레그 익스텐션)
            코어: ex019(플랭크), ex020(크런치), ex021(레그 레이즈)
            유산소: ex022(러닝), ex023(사이클)

            주의사항:
            - 초보자: 기본 운동 3-4개, 세트 3, 반복 10-12, 휴식 60-90초
            - 중급자: 다양한 운동 4-5개, 세트 3-4, 반복 8-12, 휴식 60-120초
            - 고급자: 고급 운동 5-6개, 세트 4-5, 반복 6-12, 휴식 90-180초
            - weight는 항상 0으로 설정 (사용자가 직접 입력)
            - 운동 순서는 큰 근육 → 작은 근육, 복합운동 → 고립운동
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

        val response = openAIApiService.createChatCompletion(
            authorization = "Bearer ${BuildConfig.OPENAI_API_KEY}",
            request = openAIRequest
        )

        val content = response.choices.firstOrNull()?.message?.content
            ?: throw Exception("GPT 응답이 비어있습니다")

        // Parse JSON response
        return try {
            gson.fromJson(content, AIRoutineResponse::class.java)
        } catch (e: Exception) {
            throw Exception("GPT 응답 파싱 실패: ${e.message}\n응답: $content")
        }
    }
}
