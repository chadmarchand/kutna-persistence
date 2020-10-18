package com.chadmarchand.kutna.persistence.repository.action

import com.chadmarchand.kutna.persistence.repository.Repository
import java.util.stream.Collectors

class GetListAction<T: Any>(
    val repository: Repository<T>
) : RepositoryAction {
    private val conditions: HashSet<Condition> = HashSet()

    fun execute(): List<T> {
        val entities = repository.table.selectList(getConditionsMap())

        if (repository.isLinked) {
            repository.fillEntitiesWithLinkedData(entities)
        }

        return entities
    }

    fun getConditionsMap(): Map<String, Any> {
        return conditions.stream()
            .map(this::conditionToPair)
            .collect(Collectors.toMap(
                    { pair: Pair<String, Any> -> pair.first },
                    { pair: Pair<String, Any> -> pair.second }
                )
            )
    }

    fun addConditions(conditionMap: Map<String, Any>): GetListAction<T> {
        conditionMap.keys.forEach{ key -> conditions.add(Condition(key, conditionMap.get(key)!!)) }
        return this
    }

    fun conditionToPair(condition: Condition): Pair<String, Any> {
        return Pair(condition.fieldName, condition.conditionValue)
    }
}
