package com.eduardo.crudspringproduct.dto;

import com.eduardo.crudspringproduct.model.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryRequestDto {
    private String name;

    public CategoryRequestDto(String name){
        this.name = name;
    }

}
