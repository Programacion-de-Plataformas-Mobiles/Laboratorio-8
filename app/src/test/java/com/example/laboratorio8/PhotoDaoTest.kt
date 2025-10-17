package com.example.laboratorio8

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.laboratorio8.data.AppDatabase
import com.example.laboratorio8.data.PhotoDao
import com.example.laboratorio8.data.PhotoEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class PhotoDaoTest {
    private lateinit var photoDao: PhotoDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        photoDao = db.photoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writePhotoAndReadInList() = runBlocking {
        val photo = PhotoEntity("1", "query", "small.jpg", "large.jpg", "author", "author.url")
        photoDao.insertPhotos(listOf(photo))
        val byQuery = photoDao.getPhotosByQuery("query")
        assertEquals(byQuery[0], photo)
    }
}
