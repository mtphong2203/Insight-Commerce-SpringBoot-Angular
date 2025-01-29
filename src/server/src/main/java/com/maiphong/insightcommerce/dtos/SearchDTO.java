package com.maiphong.insightcommerce.dtos;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDTO {
    private String keyword;
    private String sortBy;
    private String order;
    private int page;
    private int size;

    public Pageable toPageable() {
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(page, page, Sort.by(sortDirection, sortBy));
    }
}
