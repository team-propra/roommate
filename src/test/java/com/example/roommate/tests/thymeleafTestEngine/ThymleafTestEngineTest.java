package com.example.roommate.tests.thymeleafTestEngine;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.utility.thymeleaf.TestModel;
import com.example.roommate.utility.thymeleaf.ThymeleafTestEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@TestClass
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
