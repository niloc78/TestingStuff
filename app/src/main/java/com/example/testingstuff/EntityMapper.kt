package com.example.testingstuff

interface EntityMapper<Entity, DomainModel> {
    fun mapFromEntity(entity: Entity) : DomainModel
    fun mapToEntity(domainModel: DomainModel): Entity
}