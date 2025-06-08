package org.manudev.drivingschoolservice.controller;

import org.manudev.drivingschoolservice.services.SecretKeyGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xd")
public class TestController {

    public TestController(SecretKeyGenerator secretKeyGenerator) {
        this.secretKeyGenerator = secretKeyGenerator;
    }

    private SecretKeyGenerator secretKeyGenerator;

    @GetMapping("/xdxd")
    public String xd() {
        return secretKeyGenerator.xd();
    }
}
