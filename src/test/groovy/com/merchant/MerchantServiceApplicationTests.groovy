package com.merchant

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //disabling security filters
@DirtiesContext
class MerchantServiceApplicationTests extends Specification {

    @Autowired
    private ApplicationContext applicationContext

    @Autowired
    private MockMvc mockMvc

    def "should load context"() {
        expect:
            applicationContext != null
    }

    def "health endpoint should be enabled"() {
        expect:
            mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health")).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }
}
