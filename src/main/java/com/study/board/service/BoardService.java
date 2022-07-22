package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepositiry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepositiry boardRepositiry;

    // 글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception {

        if(file.getSize() > 0) {
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";

            UUID uuId = UUID.randomUUID();

            String fileName = uuId + "_" + file.getOriginalFilename();

            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
        } else {
            board.setFilename(null);
            board.setFilepath(null);
        }

        boardRepositiry.save(board);
    }

    // 게시글 목록 처리
    public Page<Board> boardList(Pageable pageable) {

        return boardRepositiry.findAll(pageable);
    }

    // 게시글 상세 처리
    public Board boardView(Integer id) {

        return boardRepositiry.findById(id).get();
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepositiry.findByTitleContaining(searchKeyword, pageable);
    }

    // 게시물 삭제
    public void boardDelete(Integer id) {

        boardRepositiry.deleteById(id);
    }
}
