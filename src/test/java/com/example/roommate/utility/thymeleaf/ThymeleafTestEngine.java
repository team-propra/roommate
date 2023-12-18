package com.example.roommate.utility.thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafTestEngine {

    public String render(String file, TestModel model){
        TemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/templates/");
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        model.asMap().forEach(context::setVariable);
        return templateEngine.process(file, context);
    }
    public String render(String file){
        return render(file,new TestModel());
    }


}
