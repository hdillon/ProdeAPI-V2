package com.solution.prode.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.solution.prode.BaseTests
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
abstract class BaseControllerTests : BaseTests() {

    @Autowired
    protected
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper
}