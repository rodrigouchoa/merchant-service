package com.merchant.service;

import com.merchant.domain.Transaction;
import com.merchant.domain.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TransactionSpecification implements Specification<Transaction> {
    private static final String ATTR_DESCRIPTION = "description";
    private static final String ATTR_CURRENCY = "currency";
    private static final String ATTR_AMOUNT = "amount";
    private static final String ATTR_STATUS = "status";

    private Map<String, String> params;

    public TransactionSpecification(Map<String, String> filterParams) {
        if (filterParams == null) {
            params = new HashMap<>();
        } else {
            this.params = filterParams;
        }
    }

    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey(ATTR_CURRENCY)) {
            String value = params.get(ATTR_CURRENCY);
            if (StringUtils.isNotBlank(value)) {
                predicates.add(cb.equal(root.get(ATTR_CURRENCY), value));
            }
        }
        if (params.containsKey(ATTR_STATUS)) {
            String value = params.get(ATTR_STATUS);
            if (StringUtils.isNotBlank(value)) {
                try {
                    TransactionStatus txStatus = TransactionStatus.valueOf(value.toUpperCase());
                    predicates.add(cb.equal(root.get(ATTR_STATUS), txStatus));
                } catch (IllegalArgumentException ex) {
                    log.debug("Ignoring filter '{}' as it's not a valid enum constant for Transaction Status", value);
                    predicates.add(root.get(ATTR_STATUS).isNull());
                }
            }
        }
        if (params.containsKey(ATTR_AMOUNT)) {
            String value = params.get(ATTR_AMOUNT);
            if (StringUtils.isNotBlank(value)) {
                predicates.add(cb.equal(root.get(ATTR_AMOUNT), value));
            }
        }
        if (params.containsKey(ATTR_DESCRIPTION)) {
            String value = params.get(ATTR_DESCRIPTION);
            EntityType<Transaction> txType = root.getModel();
            if (StringUtils.isNotBlank(value)) {
                predicates.add(cb.like(
                        cb.lower(
                                root.get(
                                        txType.getDeclaredSingularAttribute(ATTR_DESCRIPTION, String.class)
                                )
                        ), "%" + value.toLowerCase() + "%"
                ));
            }
        }
        query.orderBy(cb.asc(root.get("id")));
        return cb.and(predicates.toArray(new Predicate[0]));
    }

}
