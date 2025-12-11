package com.example.gymroutine.di

import com.example.gymroutine.data.remote.FirebaseAuthDataSource
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.data.remote.KakaoLocalDataSource
import com.example.gymroutine.data.remote.OpenAIDataSource
import com.example.gymroutine.data.repository.AIRoutineRepositoryImpl
import com.example.gymroutine.data.repository.AuthRepositoryImpl
import com.example.gymroutine.data.repository.ExerciseRepositoryImpl
import com.example.gymroutine.data.repository.GymRepositoryImpl
import com.example.gymroutine.data.repository.RoutineRepositoryImpl
import com.example.gymroutine.data.repository.UserRepositoryImpl
import com.example.gymroutine.data.repository.WorkoutRecordRepositoryImpl
import com.example.gymroutine.domain.repository.AIRoutineRepository
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.domain.repository.ExerciseRepository
import com.example.gymroutine.domain.repository.GymRepository
import com.example.gymroutine.domain.repository.RoutineRepository
import com.example.gymroutine.domain.repository.UserRepository
import com.example.gymroutine.domain.repository.WorkoutRecordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for repository dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataSource: FirebaseAuthDataSource,
        firestoreDataSource: FirestoreDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(authDataSource, firestoreDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firestoreDataSource: FirestoreDataSource
    ): UserRepository {
        return UserRepositoryImpl(firestoreDataSource)
    }

    @Provides
    @Singleton
    fun provideGymRepository(
        kakaoLocalDataSource: KakaoLocalDataSource,
        firestoreDataSource: FirestoreDataSource,
        gymLocalDataSource: com.example.gymroutine.data.local.GymLocalDataSource,
        authRepository: AuthRepository
    ): GymRepository {
        return GymRepositoryImpl(kakaoLocalDataSource, firestoreDataSource, gymLocalDataSource, authRepository)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(): ExerciseRepository {
        return ExerciseRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideRoutineRepository(
        firestoreDataSource: FirestoreDataSource,
        localRoutineDataSource: com.example.gymroutine.data.local.LocalRoutineDataSource
    ): RoutineRepository {
        return RoutineRepositoryImpl(firestoreDataSource, localRoutineDataSource)
    }

    @Provides
    @Singleton
    fun provideAIRoutineRepository(
        openAIDataSource: OpenAIDataSource
    ): AIRoutineRepository {
        return AIRoutineRepositoryImpl(openAIDataSource)
    }

    @Provides
    @Singleton
    fun provideWorkoutRecordRepository(): WorkoutRecordRepository {
        return WorkoutRecordRepositoryImpl()
    }
}
