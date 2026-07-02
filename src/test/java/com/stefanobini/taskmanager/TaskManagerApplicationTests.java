package com.stefanobini.taskmanager;

import com.stefanobini.taskmanager.config.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
class TaskManagerApplicationTests extends PostgresTestContainer {

	@Test
	void contextLoads() {
	}

}
