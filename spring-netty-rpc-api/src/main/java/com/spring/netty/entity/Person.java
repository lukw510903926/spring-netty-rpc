package com.spring.netty.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/14 15:49
 **/
@Data
public class Person implements Serializable {

    private static final long serialVersionUID = 3190277918110938562L;

    private String name;

    private LocalDateTime localDateTime;
}
