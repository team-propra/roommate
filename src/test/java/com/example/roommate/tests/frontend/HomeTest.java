package com.example.roommate.tests.frontend;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.utility.thymeleaf.ThymeleafTestEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import static org.assertj.core.api.Assertions.assertThat;

@TestClass
public class HomeTest {
    
    @Test
    @DisplayName("Check, that home.html contains the string 'Homepage'")
    @Description("this is a bad test for illustration purposes, as we dont use thymeleaf yet")
    public void test() {
        //Arrange
        ThymeleafTestEngine thymeleafTestEngine = new ThymeleafTestEngine();
        
        //Act
        String render = thymeleafTestEngine.render("home.html");
        
        //Assert
        assertThat(render).contains("Homepage");
    }
}
