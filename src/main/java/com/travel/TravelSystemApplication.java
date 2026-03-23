package com.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用启动入口。
 *
 * <p>
 * 本类负责启动 Spring Boot 应用，后端采用分层架构：
 * <ul>
 *     <li>controller：对外暴露 REST API，进行请求路由与参数校验</li>
 *     <li>service：封装业务逻辑，确保高内聚、低耦合</li>
 *     <li>mapper：使用 MyBatis-Plus 访问数据库</li>
 *     <li>model：领域实体与数据传输对象</li>
 *     <li>config / security / common：统一配置、鉴权、安全、公共工具</li>
 * </ul>
 * </p>
 */
@SpringBootApplication
public class TravelSystemApplication
{

    /**
     * 主函数，应用启动入口。
     *
     * @param args 启动参数
     */
    public static void main(String[] args)
    {
        SpringApplication.run(TravelSystemApplication.class, args);
    }
}

