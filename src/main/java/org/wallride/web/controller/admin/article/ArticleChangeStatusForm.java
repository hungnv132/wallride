/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.wallride.web.controller.admin.article;

import java.util.List;
import org.wallride.core.service.ArticleChangeStatusRequest;


public class ArticleChangeStatusForm {
    private List<Long> ids;
    private String language;
    private String status;
    
    public ArticleChangeStatusForm(){};

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
//    public ArticleChangeStatusRequest buildArticleChangeStatusRequest(){
//         ArticleChangeStatusRequest.Builder builder = new ArticleChangeStatusRequest.Builder();
//        return builder
//                .id(ids)
//                .language(language)
//                .status(status)
//                .build();
//    }
    
}