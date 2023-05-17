package ru.clevertec.loggerstarter.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.clevertec.loggerstarter.aop.RequestResponseAspect;
import ru.clevertec.loggerstarter.exceptions.NewsSystemExceptionHandler;


@Configuration
@EnableAspectJAutoProxy
@ConditionalOnClass({RequestResponseAspect.class, NewsSystemExceptionHandler.class})
@ComponentScan("ru.clevertec.loggerstarter.exceptions")
public class RequestResponseAspectConfig {

    @Bean
    @ConditionalOnMissingBean
    public RequestResponseAspect requestResponseAspect() {
        return new RequestResponseAspect();
    }
}
