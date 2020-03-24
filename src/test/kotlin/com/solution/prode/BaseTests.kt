package com.solution.prode

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.jdbc.JdbcTestUtils
import javax.sql.DataSource

@SpringBootTest
@ActiveProfiles("test")
abstract class BaseTests {

    @Autowired
    private lateinit var dataSource: DataSource

    protected fun deleteDataFromTables(vararg tableNames: String) {
        JdbcTestUtils.deleteFromTables(JdbcTemplate(dataSource), *tableNames)
    }
}