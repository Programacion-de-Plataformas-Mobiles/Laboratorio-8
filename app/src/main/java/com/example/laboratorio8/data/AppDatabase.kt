package com.example.laboratorio8.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [PhotoEntity::class, RecentQueryEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun recentQueryDao(): RecentQueryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE photos ADD COLUMN authorUrl TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "photo_gallery_database"
                )
                .addMigrations(MIGRATION_2_3)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
