package com.chadmarchand.kutna.persistence.repository.action

import com.chadmarchand.kutna.persistence.repository.Repository

class DeleteAction<T: Any>(val repository: Repository<T>) : RepositoryAction {
    fun execute(id: Long) {
        val entity = repository.get(id)
        repository.table.delete(id)

        if (repository.isLinked) {
            repository.deleteLinkedEntities(entity)
        }
    }

    fun execute(entity: T) {
        execute(repository.entityMapper.getEntityId(entity)!!)
    }

    fun execute(whereClauses: Map<String, Any>) {
        val entities = repository.get(whereClauses)
        repository.table.delete(whereClauses)

        if (repository.isLinked) {
            repository.deleteLinkedEntities(entities)
        }
    }
}
