package com.spring.netty.server.service;

import com.spring.netty.api.PersonService;
import com.spring.netty.entity.Person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/17 17:12
 **/
public class PersonServiceImpl implements PersonService {

    @Override
    public List<Person> list() {

        List<Person> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Person person = new Person();
            person.setName("name" + ThreadLocalRandom.current().nextInt(100));
            person.setLocalDateTime(LocalDateTime.now());
            list.add(person);
        }
        return list;
    }

    @Override
    public Person getByName(String name) {

        Person person = new Person();
        person.setName(name + ThreadLocalRandom.current().nextInt(100));
        person.setLocalDateTime(LocalDateTime.now());
        return person;
    }
}
