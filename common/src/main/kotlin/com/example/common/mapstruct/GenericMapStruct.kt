package com.example.common.mapstruct

interface GenericMapStruct<Entity, Dto> {
    fun toDto(entity: Entity): Dto
    fun toEntity(dto: Dto): Entity

    fun toDtos(entities: List<Entity>): List<Dto>
    fun toEntities(dtos: List<Dto>): List<Entity>
}

