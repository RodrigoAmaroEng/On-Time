package dev.amaro.on_time


import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.amaro.on_time.network.Value
import dev.amaro.on_time.network.withJql
import org.junit.Test

class JQLGrammarTest {

    @Test
    fun `Generate initial query`() {
        val query = withJql { }.toString()
        assertThat(query).isEqualTo("jql=")
    }

    @Test
    fun `One simple equals condition`() {
        val query = withJql {
            condition {
                field("name").eq("rodrigo")
            }
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+%22rodrigo%22")
    }

    @Test
    fun `Two simple equals conditions`() {
        val query = withJql {
            condition {
                field("name").eq("rodrigo")
            }
            and {
                field("project").eq("CST")
            }
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+%22rodrigo%22+AND+project+%3D+%22CST%22")
    }

    @Test
    fun `Condition with scalar value`() {
        val query = withJql {
            condition {
                field("name").eq("rodrigo")
            }
            and {
                field("project").set("CST")
            }
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+%22rodrigo%22+AND+project+%3D+CST")
    }

    @Test
    fun `Condition with in group of values`() {
        val query = withJql {
            condition {
                field("name").eq("rodrigo")
            }
            and {
                field("project").`in`("CST", "CAT")
            }
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+%22rodrigo%22+AND+project+IN+(%22CST%22,%22CAT%22)")
    }

    @Test
    fun `Condition with in group of scalar values`() {
        val query = withJql {
            condition {
                field("name").eq("rodrigo")
            }
            and {
                field("project").any("CST", "CAT")
            }
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+%22rodrigo%22+AND+project+IN+(CST,CAT)")
    }

    @Test
    fun `Condition with in group of defined values`() {
        val query = withJql {
            condition {
                field("name").eq("rodrigo")
            }
            and {
                field("project").any(Value.EMPTY, Value.NULL)
            }
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+%22rodrigo%22+AND+project+IN+(EMPTY,NULL)")
    }

    @Test
    fun `Condition with one order by`() {
        val query = withJql {
            condition {
                field("name").eq("rodrigo")
            }
            orderBy("priority").asc()
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+%22rodrigo%22+ORDER+BY+priority+ASC")
    }

    @Test
    fun `Condition with two order by`() {
        val query = withJql {
            condition {
                field("name").eq("rodrigo")
            }
            orderBy("priority").asc()
            orderBy("updated").desc()
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+%22rodrigo%22+ORDER+BY+priority+ASC%2C+updated+DESC")
    }

    @Test
    fun `Condition field is empty`() {
        val query = withJql {
            condition {
                field("name").empty()
            }
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+EMPTY")
    }

    @Test
    fun `Condition field is null`() {
        val query = withJql {
            condition {
                field("name").`null`()
            }
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=name+%3D+NULL")
    }

    @Test
    fun `Condition field is current user`() {
        val query = withJql {
            condition {
                field("assignee").user()
            }
        }.queryString('+', true)
        assertThat(query).isEqualTo("jql=assignee+%3D+currentUser()")
    }

    @Test
    fun `Full condition`() {
        val query = withJql {
            condition {
                project().eq("CST")
            }
            and {
                field("resolution").set("Unresolved")
            }
            and {
                field("Platform").set("Android")
            }
            and {
                assignee().any(Value.EMPTY, Value.USER)
            }
            orderBy("priority").asc()
            orderBy("updated").desc()
        }.queryString('+', true)
        assertThat(query)
            .isEqualTo("jql=project+%3D+%22CST%22+AND+resolution+%3D+Unresolved+AND+Platform+%3D+Android+AND+assignee+IN+(EMPTY,currentUser())+ORDER+BY+priority+ASC%2C+updated+DESC")
    }

    @Test
    fun testRestApi() {
//        val configuration = Resources.getConfiguration()
//            val response = JiraRequester(
//                configuration.getProperty("host"),
//                configuration.getProperty("token")
//            ).post("/rest/api/2/issue/CST-505/transitions", """
//                {
//                    "transition": {
//                        "id": "31"
//                    }
//                }
//            """.trimIndent())
//        println("Response:\n'$response'")
    }
}