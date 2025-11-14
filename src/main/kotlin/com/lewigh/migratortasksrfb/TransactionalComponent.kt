package com.lewigh.migratortasksrfb;

import org.springframework.stereotype.Component
import org.springframework.transaction.*
import org.springframework.transaction.support.*

@Component
public class TransactionalComponent(private val txManager: PlatformTransactionManager) {

    fun executeWithoutResult(operation: () -> Unit) {
        val tx = TransactionTemplate(txManager)
        tx.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRED
        tx.executeWithoutResult { operation() }
    }

    fun executeInSeparated(operation: () -> Unit) {
        val tx = TransactionTemplate(txManager)
        tx.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
        tx.executeWithoutResult { operation() }
    }
}
