package com.lewigh.migratortasksrfb;

import org.springframework.stereotype.Component
import org.springframework.transaction.*
import org.springframework.transaction.support.*

@Component
public class TransactionalComponent(private val txManager: PlatformTransactionManager, private val transactionTemplate: TransactionTemplate) {

    fun with(operation: () -> Unit) {
        transactionTemplate.executeWithoutResult { operation() }
    }

    fun withNew(operation: () -> Unit) {
        val tx = TransactionTemplate(txManager)
        tx.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
        tx.executeWithoutResult { operation() }
    }
}
