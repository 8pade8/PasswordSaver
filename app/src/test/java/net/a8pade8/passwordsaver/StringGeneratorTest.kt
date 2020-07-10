package net.a8pade8.passwordsaver

import net.a8pade8.passwordsaver.util.generateAlthaNumericString
import org.junit.Assert
import org.junit.Test
import java.util.regex.Pattern


class StringGeneratorTest {

    @Test
    fun generateAlthaNumericStringTest() {
        val althaNumericString = generateAlthaNumericString(6)
        Assert.assertEquals(6, althaNumericString.length)
        val regex = "[0-9a-zA-Z]*((\\d+[a-zA-Z]+)|([a-zA-Z]+\\d+))[0-9a-zA-Z]*"
        Assert.assertTrue(Pattern.matches(regex, althaNumericString))
    }
}


