package ru.dzalba.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(jakarta.servlet.ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SecurityConfig.class);

        servletContext.addListener(new ContextLoaderListener(context));

        servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy())
                .addMappingForUrlPatterns(null, false, "/*");
    }
}
