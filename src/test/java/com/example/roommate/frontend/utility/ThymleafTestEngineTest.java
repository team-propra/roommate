package com.example.roommate.frontend.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ThymleafTestEngineTest {
    
    @Test
    @DisplayName("ThymeleafTestEngine correctly inserts the model")
    public void test(){
        ThymeleafTestEngine thymeleafTestEngine = new ThymeleafTestEngine();
        TestModel testModel = new TestModel();
        String testValue = "77";
        testModel.addAttribute("test",testValue);

        String render = thymeleafTestEngine.render("tests/thymeleafTestEngineTest.html", testModel);
        
        assertThat(render).contains(testValue);
    }
}
