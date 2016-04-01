package ch.yvu.teststore

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAutoConfiguration(exclude = arrayOf(DataSourceAutoConfiguration::class))
@EnableAsync
open class Application

fun main(args:Array<String>){
    SpringApplication.run(Application::class.java, *args)
}
