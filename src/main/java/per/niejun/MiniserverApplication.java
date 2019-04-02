package per.niejun;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("per.niejun.dao")
public class MiniserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniserverApplication.class, args);
		System.out.println("ヾ(◍°∇°◍)ﾉﾞ    bootdo启动成功      ヾ(◍°∇°◍)ﾉﾞ\n" +
			" __                              .__                              \n" +
			"|  | ______   _____ _____ _______|__| _________.__._____    ____  \n" +
			"|  |/ /  _ \\ /     \\\\__  \\\\_  __ \\  |/ ____<   |  |\\__  \\  /    \\ \n" +
			"|    <  <_> )  Y Y  \\/ __ \\|  | \\/  < <_|  |\\___  | / __ \\|   |  \\\n" +
			"|__|_ \\____/|__|_|  (____  /__|  |__|\\__   |/ ____|(____  /___|  /\n" +
			"     \\/           \\/     \\/             |__|\\/          \\/     \\/"
		);
	}

}
