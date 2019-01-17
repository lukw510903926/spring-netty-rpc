package com.spring.netty.api;


import com.spring.netty.entity.Person;

import java.util.List;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/14 15:50
 **/
public interface PersonService {

    List<Person> list();

    Person getByName(String name);
}
