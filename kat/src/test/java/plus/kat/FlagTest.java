package plus.kat;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kraity
 */
public class FlagTest {

    static class Meta {
        public Date date;
        public Instant instant;
    }

    @Test
    public void test_date_as_timestamp() throws IOException {
        Meta meta = new Meta();
        meta.date = new Date(1641871353000L);
        meta.instant = Instant.ofEpochMilli(1641871353123L);

        assertEquals(
            "{\"date\":\"2022-01-11 11:22:33\",\"instant\":\"2022-01-11T03:22:33.123Z\"}", Json.encode(meta)
        );

        assertEquals(
            "{\"date\":1641871353000,\"instant\":\"2022-01-11T03:22:33.123Z\"}",
            Json.encode(meta, Flag.DATE_AS_TIMESTAMP)
        );

        assertEquals(
            "{\"date\":\"2022-01-11 11:22:33\",\"instant\":1641871353123}",
            Json.encode(meta, Flag.INSTANT_AS_TIMESTAMP)
        );
    }

    enum Role {
        A, B, C
    }

    static class Bean0 {
        public Role role;
    }

    @Test
    public void test_enum_as_index() {
        Bean0 bean = new Bean0();

        bean.role = Role.A;
        assertEquals("{\"role\":\"A\"}", Json.encode(bean));
        assertEquals("{\"role\":0}", Json.encode(bean, Flag.ENUM_AS_INDEX));

        bean.role = Role.B;
        assertEquals("{\"role\":\"B\"}", Json.encode(bean));
        assertEquals("{\"role\":1}", Json.encode(bean, Flag.ENUM_AS_INDEX));


        bean.role = Role.C;
        assertEquals("{\"role\":\"C\"}", Json.encode(bean));
        assertEquals("{\"role\":2}", Json.encode(bean, Flag.ENUM_AS_INDEX));
    }
}
