package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import com.study.category.entity.Category;
import com.study.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/board/write")
    public String boardWriteForm(Model model) {

        Integer id = 1;

        List<Category> categoryList = null;

        try {
            categoryList = categoryService.categorySearchList(0);
        } catch(Exception e) {
            System.out.println("BoardController : boardWriteForm");
        }

        model.addAttribute("categoryList", categoryList);

        return "board/notice/boardwrite";
    }

    @PostMapping("/board/writeproc")
    public String boardWriteProc(Board board, Model model, MultipartFile file) throws Exception {

        boardService.write(board, file);

//        model.addAttribute("message", "글 작성이 완료되었습니다.");
//        model.addAttribute("searchUrl", "/board/list");

        return "redirect:/board/list";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = DESC) Pageable pagerable,
                            String searchKeyword) {

        Page<Board> list = null;

        if (searchKeyword == null) {
            list = boardService.boardList(pagerable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pagerable);
        }

//        getTotalPages() // 총 몇 페이지
//        getTotalElements() // 전체 개수
//        getNumber() // 현재 페이지 번호 0부터 시작
//        getSize() // 페이지당 데이터 개수
//        hasNext() // 다음 페이지 존재 여부
//        isFirst() // 시작 페이지(0) 여부
//      15 - (15 -1) % 10


        int nowPage = 0;
        int sizePage = 0;
        int startPage = 0;
        int endPage  = 0;
        int prevPage = 0;
        int nextPage = 0;
        int totalPages = 0;
        long totalNum = 0;
        long totalElements = list.getTotalElements();

        if(totalElements > 0) {
            nowPage = list.getNumber();
            sizePage = list.getSize();
            startPage = Math.max(nowPage + 1 - (nowPage) % 10, 1);
            endPage = Math.min(startPage + 9, list.getTotalPages());
            prevPage = startPage - 2;
            nextPage = endPage;
            totalPages = (int) list.getTotalPages();
            totalNum = totalElements - nowPage * sizePage;
        }

//        System.out.println("=========================");
//        System.out.println("nowPage : " + nowPage);
//        System.out.println("sizePage : " + sizePage);
//        System.out.println("startPage : " + startPage);
//        System.out.println("endPage : " + endPage);
//        System.out.println("prevPage : " + prevPage);
//        System.out.println("totalPage : " + totalPages);
//        System.out.println("totalElements : " + totalElements);

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalNum", totalNum);

        return "board/notice/boardlist";
    }


    @GetMapping("/board/view")
    public String boardView(Model model, Integer id) {

        model.addAttribute("board", boardService.boardView(id));
        return "board/notice/boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {

        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("board", boardService.boardView(id));
        return "board/notice/boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, MultipartFile file) throws Exception {

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        boardTemp.setFilename(board.getFilename());
        boardTemp.setFilepath(board.getFilepath());

        boardService.write(boardTemp, file);

        return "redirect:/board/list";
    }

}