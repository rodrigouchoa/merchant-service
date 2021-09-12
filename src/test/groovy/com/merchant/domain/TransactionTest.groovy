package com.merchant.domain

import spock.lang.Specification

class TransactionTest extends Specification {

    def "getAmountAsString should use bankers rounding"() {
        given:
            Transaction transaction = new Transaction()
            transaction.setAmount(BigDecimal.valueOf(13.12999))
        when:
            def result = transaction.getAmountAsString()
        then:
            "13.13" == result
    }

    def "setAmountAsString should use bankers rounding"() {
        given:
            def amount = "13.129999"
            Transaction transaction = new Transaction()
        when:
            transaction.setAmountAsString(amount)
        then:
            transaction.getAmount().equals(BigDecimal.valueOf(13.13))
            transaction.getAmountAsString().equals(new String("13.13"))
    }
}
