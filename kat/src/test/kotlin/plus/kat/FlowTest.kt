package plus.kat

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FlowTest {

    @Test
    fun test_flow_kat() {
        assertEquals(
            "Story{i:id(100001)s:title(KAT+)M:meta{i:view(99)}User:author{i:id(1)s:name(kraity)}}",
            kat("Story") {
                it["id"] = 100001
                it["title"] = "KAT+"
                it["meta"] = { meta ->
                    meta["view"] = 99
                }
                it["author", "User"] = { user ->
                    user["id"] = 1
                    user["name"] = "kraity"
                }
            }
        )
    }

    @Test
    fun test_flow_json() {
        assertEquals(
            """{"id":100001,"title":"KAT+","author":{"id":1,"name":"kraity"}}""",
            json {
                it["id"] = 100001
                it["title"] = "KAT+"
                it["author"] = { user ->
                    user["id"] = 1
                    user["name"] = "kraity"
                }
            }
        )
    }

    @Test
    fun test_flow_mark() {
        assertEquals(
            "<Story><id>100001</id><title>KAT+</title><author><id>1</id><name>kraity</name></author></Story>",
            doc("Story") {
                it["id"] = 100001
                it["title"] = "KAT+"
                it["author"] = { user ->
                    user["id"] = 1
                    user["name"] = "kraity"
                }
            }
        )
    }
}
