package com.example.common.mapstruct.domain

import com.example.common.domain.user.entity.User
import com.example.common.dto.UserDto
import com.example.common.mapstruct.GenericMapStruct
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface UserMapStruct : GenericMapStruct<User, UserDto>
