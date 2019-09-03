package com.spring.netty.server.service;

import com.spring.netty.api.PersonService;
import com.spring.netty.common.annotation.Provider;
import com.spring.netty.entity.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email lkw510903926@163.com
 * @since 2019/1/17 17:12
 **/
@Service
@Provider
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
