package com.example.myapplication.data.remote;

/**
 * 중앙에서 스프링 서버 주소를 관리한다. 에뮬레이터에서는 10.0.2.2가 로컬호스트를 가리킨다.
 */
public final class ServerConfig {

    private ServerConfig() {
    }

    public static final String BASE_URL = "http://10.0.2.2:8080";
}

