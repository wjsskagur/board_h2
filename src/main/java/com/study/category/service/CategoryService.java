package com.study.category.service;

import com.study.category.entity.Category;
import com.study.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> categorySearchList(Integer id) {

        return categoryRepository.findByIdfk(id);
    }

    public List<Category> categoryChildList(Integer id) {

        return categoryRepository.findByIdfk(id);
    }
}
