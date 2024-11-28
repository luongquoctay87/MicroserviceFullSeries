package vn.tayjava;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.tayjava.controller.AuthenticationController;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthenticationServiceApplicationTests {

    @Autowired
    private AuthenticationController authenticationController;

    @Test
    void contextLoads() {
        Assertions.assertThat(authenticationController).isNotNull();
    }
}
