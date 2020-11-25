package com.chadmarchand.kutna.persistence.service

import com.chadmarchand.kutna.common.types.Id
import com.chadmarchand.kutna.persistence.repository.Repository
import com.chadmarchand.kutna.persistence.repository.action.Condition
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.springframework.beans.factory.annotation.Autowired
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.stream.Collectors

const val REPOSITORY_QUALIFIER_SUFFIX = "Repository"

fun <T, E: Any> getRepositoryQualifier(serviceClass: Class<T>): Qualifier {
    return named(
((serviceClass
        .genericSuperclass as ParameterizedType)
        .actualTypeArguments[0] as Class<E>).simpleName + REPOSITORY_QUALIFIER_SUFFIX
    )
}

fun <T> getRepositoryQualifierFromEntity(entityClass: Class<T>): Qualifier {
    return named(
        entityClass.simpleName + REPOSITORY_QUALIFIER_SUFFIX
    )
}

inline fun <reified T: Any> getRepositoryQualifier(): Qualifier {
    return named(T::class.simpleName + REPOSITORY_QUALIFIER_SUFFIX)
}

abstract class PersistentService<T: Any> @Autowired constructor(
    private val supportedOperations: ServiceOperations = ServiceOperations.ALL
) : KoinComponent {
    private val repository: Repository<T> by inject(getRepositoryQualifier<PersistentService<T>, T>(this.javaClass))

    companion object {
        private const val ADD_OPERATION_NAME = "Add"
        private const val GET_OPERATION_NAME = "Get"
        private const val UPDATE_OPERATION_NAME = "Update"
        private const val PATCH_OPERATION_NAME = "Patch"
        private const val DELETE_OPERATION_NAME = "Delete"
    }

    fun getAll(): List<T> {
        if (this.supportedOperations.isGetEnabled) {
            return repository.getAll()
        } else {
            throw UnsupportedOperationException(getDisabledOperationMessage(GET_OPERATION_NAME))
        }
    }

    fun get(entityId: Id): T {
        return if (this.supportedOperations.isGetEnabled) {
            repository.get(entityId)
        } else {
            throw UnsupportedOperationException(getDisabledOperationMessage(GET_OPERATION_NAME))
        }
    }

    fun getSome(condition: Condition): Collection<T> {
        return getSome(listOf(condition))
    }

    fun getSome(conditions: Collection<Condition>): Collection<T> {
        val conditionParams: Map<String, Any> = conditions.stream().collect(
            Collectors.toMap(
                Condition::fieldName,
                Condition::conditionValue
            )
        )
        return repository.get(
            conditionParams
        )
    }

    fun get(params: Map<String, Any>): List<T> {
        return if (this.supportedOperations.isGetEnabled) {
            repository.get(params)
        } else {
            throw UnsupportedOperationException(getDisabledOperationMessage(GET_OPERATION_NAME))
        }
    }

    fun getFirst(params: Map<String, Any>): Optional<T> {
        return if (this.supportedOperations.isGetEnabled) {
            repository.getFirst(params)
        } else {
            throw UnsupportedOperationException(getDisabledOperationMessage(GET_OPERATION_NAME))
        }
    }

    fun count(): Int {
        return this.count(emptyList())
    }

    fun count(condition: Condition): Int {
        return count(listOf(condition))
    }

    fun count(conditions: Collection<Condition>): Int {
        return getSome(conditions).count()
    }

    open fun add(entity: T): T {
        if (this.supportedOperations.isPostEnabled) {
            val addedEntity = addInternal(entity)
            return modifyEntityAfterAdd(addedEntity)
        } else {
            throw UnsupportedOperationException(getDisabledOperationMessage(ADD_OPERATION_NAME))
        }
    }

    open fun update(entity: T): T {
        if (this.supportedOperations.isUpdateEnabled) {
            repository.update(entity)
            return refreshEntity(entity)
        } else {
            throw UnsupportedOperationException(getDisabledOperationMessage(UPDATE_OPERATION_NAME))
        }
    }

    fun patch(id: Long, partialNodeJson: String): T {
        return if (this.supportedOperations.isPatchEnabled) {
            repository.patch(id, partialNodeJson)
        } else {
            throw UnsupportedOperationException(getDisabledOperationMessage(PATCH_OPERATION_NAME))
        }
    }

    fun patch(updatedInfo: Map<String, Any?>, id: Long): T {
        return if (this.supportedOperations.isPatchEnabled) {
            repository.patch(updatedInfo, id)
        } else {
            throw UnsupportedOperationException(getDisabledOperationMessage(PATCH_OPERATION_NAME))
        }
    }

    fun delete(id: Id) {
        if (this.supportedOperations.isDeleteEnabled) {
            repository.delete(id)
        } else {
            throw UnsupportedOperationException(getDisabledOperationMessage(DELETE_OPERATION_NAME))
        }
    }

    protected open fun modifyEntityAfterAdd(entity: T): T {
        return entity
    }

    protected fun refreshEntity(entity: T): T {
        return repository.getRefreshedEntity(entity)
    }

    private fun addInternal(entity: T): T {
        repository.addAndSetKey(entity, null)

        return refreshEntity(entity)
    }

    private fun getDisabledOperationMessage(operationType: String): String {
        return "The '$operationType' operation is disabled for this CRUD service instance"
    }
}
