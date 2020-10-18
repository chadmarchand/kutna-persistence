package com.chadmarchand.kutna.persistence.repository.action

import com.chadmarchand.kutna.persistence.repository.Repository
import java.util.*

class GetByIdAction<T: Any>(
    val repository: Repository<T>
) : RepositoryAction {

    fun execute(id: Long): T {
        val whereClauses = HashMap<String, Any>()
        whereClauses[repository.entityMapper.idFieldName] = id.toString()

        val entity = repository.table.selectObject(whereClauses)

        if (repository.isLinked) {
            repository.fillEntityWithLinkedData(entity)
        }

        return entity
    }
}