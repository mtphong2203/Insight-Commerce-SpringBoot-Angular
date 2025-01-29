package com.maiphong.insightcommerce.mappers;

import java.util.Collection;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomPageResponse<T> {

    private Collection<T> data;

    private Link links;

    private PagedModel.PageMetadata page;

}
