package org.ziegelbauer.homepage.utility;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.ziegelbauer.homepage.controllers.api.BlogApiController;
import org.ziegelbauer.homepage.models.Blog;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BlogModelAssembler implements RepresentationModelAssembler<Blog, EntityModel<Blog>> {

    @NotNull
    @Override
    public EntityModel<Blog> toModel(@NotNull Blog blog) {

        return EntityModel.of(blog, //
                linkTo(methodOn(BlogApiController.class).get(blog.getId())).withSelfRel(),
                linkTo(methodOn(BlogApiController.class).get()).withRel("blogs"));
    }
}