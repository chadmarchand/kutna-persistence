package com.chadmarchand.kutna.persistence.repository.action

import com.chadmarchand.kutna.persistence.repository.Repository
import org.json.JSONObject
import java.util.*

class PatchAction<T: Any>(val repository: Repository<T>) : RepositoryAction {
    fun execute(updatedInfo: Map<String, Any?>, id: Long): T {
        return execute(id, updatedInfo, mapOf(Pair(repository.entityMapper.idFieldName, id.toString())))
    }

    fun execute(id: Long, updatedInfo: Map<String, Any?>, whereClauses: Map<String, String>): T {
        repository.table.patch(repository.get(id), updatedInfo, whereClauses)
        return repository.get(id)
    }

    fun execute(id: Long, partialEntityJson: String): T {
        val jsonObject = JSONObject(partialEntityJson)

        val updatedInfo = HashMap<String, Any?>()
        val nonLinkedFields = repository.entityMapper.fieldNames
        nonLinkedFields.forEach { field ->
            if (jsonObject.has(field)) {
                if (jsonObject.isNull(field)) {
                    updatedInfo[field] = null
                } else {
                    updatedInfo[field] = jsonObject.get(field)
                }
            }
        }

        return execute(updatedInfo, id)
    }
}
