package net.a8pade8.passwordsaver

import android.content.Context
import android.os.Build.VERSION_CODES
import net.a8pade8.passwordsaver.data.*
import net.a8pade8.passwordsaver.data.PasswordSaverContract.DATA_BASE
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [VERSION_CODES.LOLLIPOP], packageName = "net.a8pade8.passwordsaver")
class DbServiceTests {

    private lateinit var context: Context

    @Before
    fun createDb() {
        context = RuntimeEnvironment.application
        loading(context)
    }

    @Test
    @Throws(EmptyDataException::class, ResourceLoginRepeatException::class)
    fun addRecordToPasswordsTest() {
        val id = addRecordToPasswords("vk.com", "unknown", "pass")
        Assert.assertTrue(isRecordExistInPasswords(id))
    }

    @Test
    fun deleteRecordFromPasswordsTest() {
        val id = addRecordToPasswords("vk.com", "unknown2", "pass")
        Assert.assertTrue(isRecordExistInPasswords(id))
        deleteRecordFromPasswords(id)
        Assert.assertFalse(isRecordExistInPasswords(id))
    }

    @Test
    fun isResourceExistTest() {
        addRecordToPasswords("vk.com", "unknown3", "pass")
        Assert.assertTrue(isContainResourceInPasswords("vk.com"))
    }

    @Test
    fun updateRecordInPasswordsTest() {
        val id = addRecordToPasswords("vk.com", "unknown4", "pass")
        Assert.assertTrue(isRecordExistInPasswords(id))
        updateRecordInPasswords(Record(
                id,
                "1vk.com1",
                "unknown33",
                "qwerty",
                "comment",
                false
        ))
        val recordFromPasswords = getRecordFromPasswords(id)
        Assert.assertEquals(recordFromPasswords.login, "unknown33")
        Assert.assertEquals(recordFromPasswords.resourceName, "1vk.com1")
        Assert.assertEquals(recordFromPasswords.password, "qwerty")
    }

    @After
    fun deleteDb() {
        context.deleteDatabase(DATA_BASE)
    }
}