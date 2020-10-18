package com.chadmarchand.kutna.persistence.repository.action

import com.chadmarchand.kutna.persistence.repository.Repository

class UpdateAction<T: Any>(
    val repository: Repository<T>
) : RepositoryAction {
    fun execute(entity: T) {
        if (repository.isLinked) {
            repository.updateLinkedEntities(entity)
        }

        repository.table.update(
            entity,
            mapOf(
                Pair(repository.entityMapper.idFieldName, repository.entityMapper.getEntityId(entity)!!.toString())
            )
        )
    }
}
