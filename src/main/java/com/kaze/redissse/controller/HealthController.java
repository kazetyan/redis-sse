package com.kaze.redissse.controller;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/sse")
@Slf4j
public class HealthController {
    @Autowired
    private RedisClient redisClient;
    @Value("${sse.channel:test}")
    private String channel;

    @GetMapping(value = "/subcribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> subcribe() {
        StatefulRedisPubSubConnection<String, String> connection = redisClient.connectPubSub();
        connection.sync().subscribe(channel);
        return Flux.create(sink -> {
            connection.addListener(new RedisPubSubAdapter<>() {
                @Override
                public void message(String channel, String msg) {
                    log.info("channel:{}, msg:{}", channel, msg);
                    sink.next(msg);
                }
            });
            sink.onCancel(connection::close);
        });
    }

    @GetMapping(value = "/publish")
    public String publish(String msg) {
        try (StatefulRedisPubSubConnection<String, String> connection = redisClient.connectPubSub()) {
            connection.sync().publish(channel, msg);
            log.info("推送数据成功! msg:{}", msg);
        }
        return "ok";
    }
}
