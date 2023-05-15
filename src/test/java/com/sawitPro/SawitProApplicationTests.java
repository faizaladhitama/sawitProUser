package com.sawitPro;

import com.sawitPro.helpers.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class SawitProApplicationTests {
    @MockBean
    private JwtUtil jwtUtil;

	@Test
	void contextLoads() {
	}

}
