package com.leomilano.diamond.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.leomilano.diamond.data.dao.DiamondDao
import com.leomilano.diamond.data.entity.DiamondEntity

@Database(entities = [DiamondEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diamondDao(): DiamondDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE diamonds ADD COLUMN stock INTEGER NOT NULL DEFAULT 1")
            }
        }


        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "diamond_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
