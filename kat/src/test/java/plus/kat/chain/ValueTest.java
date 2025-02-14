package plus.kat.chain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValueTest {

    @Test
    public void test() {
        String name = "Value";
        Value value = new Value(name);
        assertSame(name, value.toString());
        assertSame(value.toString(), value.toString());
    }

    @Test
    public void test_digest() {
        String text = "User{i:id(1)s:name(kraity)}";
        Value value = new Value(text);
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", new Value(0).digest());
        assertEquals("d04f45fd1805ea7a98821bdad6894cb4", value.digest());
        assertEquals("21707be3777f237901b7edcdd73dc8288a81a4d2", value.digest("SHA1"));
    }

    @Test
    public void test_toInt() {
        Value v1 = new Value("1400");
        assertEquals(1400, v1.toInt());

        Value v2 = new Value("2147483647");
        assertEquals(Integer.MAX_VALUE, v2.toInt());


        Value v3 = new Value("-2147483648");
        assertEquals(Integer.MIN_VALUE, v3.toInt());
    }

    @Test
    public void test_toLong() {
        Value v1 = new Value("1400");
        assertEquals(1400L, v1.toLong());

        Value v2 = new Value("9223372036854775807");
        assertEquals(Long.MAX_VALUE, v2.toLong());


        Value v3 = new Value("-9223372036854775808");
        assertEquals(Long.MIN_VALUE, v3.toLong());
    }

    @Test
    public void test_toFloat() {
        Value v3 = new Value("1.23");
        assertEquals(1.23F, v3.toFloat());

        Value v4 = new Value("123.456");
        assertEquals(123.456F, v4.toFloat());
    }

    @Test
    public void test_toDouble() {
        Value v3 = new Value("1234.6789");
        assertEquals(1234.6789D, v3.toDouble());

        Value v4 = new Value("123.456789");
        assertEquals(123.456789D, v4.toDouble());
    }
}
