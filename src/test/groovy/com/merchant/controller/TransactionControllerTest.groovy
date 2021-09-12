package com.merchant.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.merchant.domain.Transaction
import com.merchant.domain.TransactionSaveDTO
import com.merchant.domain.TransactionStatus
import com.merchant.domain.TransactionUpdateDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager

import static com.merchant.domain.TransactionStatus.CREATED
import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //disables security filter
@DirtiesContext
class TransactionControllerTest extends Specification {
    private static final String URL = "/api/v1/transactions"

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    private EntityManager entityManager

    def "should find all transactions"() {
        expect:
            mockMvc.perform(get(URL)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$', hasSize(6)))
    }

    def "should filter transaction by description"() {
        expect:
            mockMvc.perform(get(URL)
                    .param("description", "transaction")
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$', hasSize(5)))
    }

    def "should filter transaction by amount"() {
        expect:
            mockMvc.perform(get(URL)
                    .param("amount", "8590.87")
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$', hasSize(1)))
    }

    def "should filter transaction by currency"() {
        expect:
            mockMvc.perform(get(URL)
                    .param("currency", "USD")
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$', hasSize(2)))
    }

    def "should filter transaction by status"() {
        expect:
            mockMvc.perform(get(URL)
                    .param("status", "REFUNDED")
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$', hasSize(1)))
    }

    def "should find transaction by uuid"() {
        given:
            def uuid = "efe97b06-1ab8-415a-9418-e422830abf21"
        expect:
            mockMvc.perform(get(URL + "/" + uuid)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.uuid').value(uuid))
    }

    def "should return 404 if transaction doesn't exist"() {
        given:
            def uuid = "efe97b06-1ab8-415a-9418-e422830abf66"
        expect:
            mockMvc.perform(get(URL + "/" + uuid)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
    }

    def "should save transaction"() {
        given:
            def amount = "666.78"
            def description = "test transaction"
            def merchantUuid = "daccd4ef-e34c-4494-bece-292eafdc2786"
            def merchantName = "Michael Jordan"
            TransactionSaveDTO dto = TransactionSaveDTO.builder()
                    .amount(amount)
                    .currency("USD")
                    .description(description)
                    .merchantUuid(merchantUuid)
                    .merchantName(merchantName)
                    .status(TransactionStatus.REFUNDED)
                    .build()
        expect:
            mockMvc.perform(post(URL)
                    .content(toJson(dto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated()).andExpect(header().exists("Location"))
        and:
            Transaction tx = entityManager.find(Transaction.class, new Long(7))
            tx.getAmountAsString() == amount
            tx.getDescription() == description
            tx.getStatus() == TransactionStatus.REFUNDED
            tx.getMerchantUuid() == merchantUuid
            tx.getMerchantName() == merchantName
    }

    @Unroll
    def "should return BAD REQUEST if mandatory attributes are missing when saving transaction"(TransactionStatus txStatus,
                                                                                             String amount,
                                                                                             String currency,
                                                                                             String merchantUuid,
                                                                                             String merchantName) {
        given:
            TransactionSaveDTO dto = TransactionSaveDTO.builder()
                    .amount(amount)
                    .currency(currency)
                    .description("Some description")
                    .merchantUuid(merchantUuid)
                    .merchantName(merchantName)
                    .status(txStatus)
                    .build()
        expect:
            mockMvc.perform(post(URL)
                    .content(toJson(dto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
        where:
            txStatus | amount | currency | merchantUuid | merchantName
            null    | "100.01"  | "BRL" | "54910338-7d12-4f96-9832-6013c4aaa142" | "John Doe"
            CREATED | null      | "BRL" | "54910338-7d12-4f96-9832-6013c4aaa142" | "John Doe"
            CREATED | "dummy"   | "BRL" | "54910338-7d12-4f96-9832-6013c4aaa142" | "John Doe"
            CREATED | "100.01"  | null  | "54910338-7d12-4f96-9832-6013c4aaa142" | "John Doe"
            CREATED | "100.01"  | "BRL" | null                                   | "John Doe"
            CREATED | "100.01"  | "BRL" | "54910338-7d12-4f96-9832-6013c4aaa142" | null
    }

    def "should delete a transaction"() {
        expect:
            def uuid = "9826868c-a9c8-415a-9b51-2a6f2c76f28a"
            mockMvc.perform(delete(URL + "/" + uuid)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
    }

    def "should return NOT FOUND if deleting a transaction that doesn't exist"() {
        expect:
            def uuid = "f9207188-b510-4ce1-b634-045a8aa23aeb"
            mockMvc.perform(delete(URL + "/" + uuid)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
    }

    def "should update a transaction"() {
        given:
            def uuid = "22613e22-0276-47a5-ae24-02e33d269b21"
            def merchantUuid = "daccd4ef-e34c-4494-bece-292eafdc2786"
            def merchantName = "Mariah"
            def description = "new description"
            def newStatus = TransactionStatus.REFUNDED
            def amount = "999.99"
            def currency = "EUR"
            TransactionUpdateDTO dto = TransactionUpdateDTO.builder()
                    .merchantUuid(merchantUuid)
                    .merchantName(merchantName)
                    .description(description)
                    .status(newStatus)
                    .amount(amount)
                    .currency(currency)
                    .build()
        expect:
            mockMvc.perform(put(URL + "/" + uuid)
                    .content(toJson(dto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
        and:
            Transaction tx = entityManager.find(Transaction.class, new Long(4))
            tx != null
            tx.getMerchantUuid() == merchantUuid
            tx.getMerchantName() == merchantName
            tx.getDescription() == description
            tx.getStatus() == newStatus
            tx.getAmountAsString() == amount
            tx.getCurrency() == currency
    }

    def "should return NOT FOUND if transaction doesn't exist when updating"() {
        given:
            def dummyTransactionUuid = "e197556f-fc38-48cf-a081-d65e5fa7c5c6"
            def merchantUuid = "daccd4ef-e34c-4494-bece-292eafdc2786"
            def merchantName = "Mariah"
            def description = "new description"
            def newStatus = TransactionStatus.REFUNDED
            def amount = "999.99"
            def currency = "EUR"
            TransactionUpdateDTO dto = TransactionUpdateDTO.builder()
                    .merchantUuid(merchantUuid)
                    .merchantName(merchantName)
                    .description(description)
                    .status(newStatus)
                    .amount(amount)
                    .currency(currency)
                    .build()
        expect:
            mockMvc.perform(put(URL + "/" + dummyTransactionUuid)
                    .content(toJson(dto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
    }

    private String toJson(Object dto) {
        objectMapper.writeValueAsString(dto)
    }
}
