package com.chadmarchand.kutna.persistence.repository.action

import com.chadmarchand.kutna.persistence.repository.Repository

class AddAndSetKeyAction<T : Any>(
    val repository: Repository<T>
) : RepositoryAction {
    fun execute(entity: T): T {
        val key = repository.table.addAndReturnKey(entity)
        repository.entityMapper.setEntityId(entity, key)
        return repository.get(key)
    }
}
