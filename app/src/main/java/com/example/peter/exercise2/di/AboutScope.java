package com.example.peter.exercise2.di;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.inject.Scope;

@Scope
@Retention(RUNTIME)
public @interface AboutScope {

}
