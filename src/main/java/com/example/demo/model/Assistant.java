package com.example.demo.model;
 


import dev.langchain4j.service.SystemMessage;

public interface Assistant {

    @SystemMessage("You are a professional IT support assistant. Be polite and concise.")
    String chat(String message);
}