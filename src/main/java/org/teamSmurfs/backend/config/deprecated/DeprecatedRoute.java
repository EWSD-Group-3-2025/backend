package org.teamSmurfs.backend.config.deprecated;

import org.springframework.web.bind.annotation.Mapping;
import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface DeprecatedRoute {
    String message() default "This API is deprecated and no longer available.";
}
