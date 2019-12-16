package com.example.demo.controller;

import com.example.demo.service.api.TagService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tag")
public class TagController {

  @Autowired
  private TagService tagService;

  @Data
  private static class EditTagRequest {
    private String word;
    private String editTag;
  }

  @PutMapping
  public void editTag(@RequestBody EditTagRequest editTagRequest) {
    tagService.editTag(editTagRequest.getWord(), editTagRequest.getEditTag());
  }

}
