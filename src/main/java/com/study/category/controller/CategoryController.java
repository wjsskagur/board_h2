package com.study.category.controller;

import com.study.category.entity.Category;
import com.study.category.service.CategoryService;
import com.study.common.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/list")
    public String categorySearchList(Model model) {

        List<Category> lists = null;

        lists = categoryService.categorySearchList(0);

        model.addAttribute("lists", lists);

        return "categorylist";
    }

    @ResponseBody
    @PostMapping("/category/listchild")
    public JSONResponse<Object> categoryChildList(Integer id) {
        List<Category> lists = null;
        lists = categoryService.categoryChildList(id);

        JSONResponse<Object> jsonResponse = new JSONResponse<>();
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("count", lists.size());

        jsonResponse.setMessage("SUCCESS");
        jsonResponse.setInfo(hashMap);
        jsonResponse.setData(lists);

        return jsonResponse;
    }
}
