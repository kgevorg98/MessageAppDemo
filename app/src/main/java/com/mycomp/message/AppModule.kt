package com.mycomp.message

import android.content.Context
import com.mycomp.database.MessagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMessagesDatabase(
        @ApplicationContext context: Context
    ): MessagesDatabase {
        return MessagesDatabase(context)
    }
}