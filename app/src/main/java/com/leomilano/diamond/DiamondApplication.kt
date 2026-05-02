package com.leomilano.diamond

import android.app.Application
import com.leomilano.diamond.data.database.AppDatabase

class DiamondApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
